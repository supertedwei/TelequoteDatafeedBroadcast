var config = require('./config');
var Model = require('./model');
var realtimeDB = require('./db').realtimeDB;

var iteration = 0; // Flag for pruning
var limit = 1000; // Prune every XXX iteration
var pruneduration = 5; // minutes

var socket = require('socket.io-client')(config.pushServer.url);
socket.on('connect', function(){
    console.log("on connect");
    socket.emit('authentication', {username: config.pushServer.username, password: config.pushServer.password});
    socket.on('authenticated', function() {
        console.log("authenticated");
    });
    socket.on('unauthorized', function(data){
        console.log("unauthorized : " + data.message);
    });
});
socket.on('counter', function(data){
    // console.log("on counter : " + JSON.stringify(data));

/*
  
    "time":"09:59:12",

   
                    + " time = NOW()";
*/
    var quote = {};
    quote.symbol = data.symbol;
    quote.bid = data.bid;
    quote.ask = data.ask;
    quote.last = data.last;
    quote.change = data.change;
    quote.high = data.high;
    quote.low = data.low;
    quote.open = data.open;
    quote.prev_close = data.prev_close;
    quote.time = data.time;
    // console.log("quote : " + JSON.stringify(quote));

    new Model.Quote({symbol: quote.symbol}).save(quote, {patch: true}).then(function(model) {
        // console.log("model : " + JSON.stringify(model));
    }).catch(error => {
        console.log("error : " + JSON.stringify(error));
        new Model.Quote().save(quote).catch(error => {
            console.log("error 2 : " + JSON.stringify(error));
        });
    });

    iteration++;
    console.log("iteration : " + iteration);
    var counter = quote;
    var query = "INSERT INTO quotelog"
                    + " SET"
                    + " symbol = '"+counter.symbol+"',"
                    + " bid = '"+counter.bid+"',"
                    + " ask = '"+counter.ask+"',"
                    + " last = '"+counter.last+"',"
                    + " `change` = '"+counter.change+"',"
                    + " high = '"+counter.high+"',"
                    + " low = '"+counter.low+"',"
                    + " open = '"+counter.open+"',"
                    + " prev_close = '"+counter.prev_close+"',"
                    + " time = NOW(),"
                    + " timestamp = UNIX_TIMESTAMP()";
    realtimeDB.knex.raw(query).then(function (response) {
        // console.log("response : " +  JSON.stringify(response));
    }).catch(error => {
        console.log("error : " + JSON.stringify(error));
    });

    if (iteration % limit == 0) {
        pruneQuoteLog();
    }
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});

function pruneQuoteLog() {
    console.log("pruneQuoteLog");
    var query = "DELETE FROM quotelog WHERE timestamp < (UNIX_TIMESTAMP(NOW()-INTERVAL ? MINUTE))";
    realtimeDB.knex.raw(query, [pruneduration]).then(function (response) {
        console.log("[pruneQuoteLog] response : " +  JSON.stringify(response));
    }).catch(error => {
        console.log("[pruneQuoteLog] error : " + JSON.stringify(error));
    });
}

console.log("Connecting to push server : " + config.pushServer.url);