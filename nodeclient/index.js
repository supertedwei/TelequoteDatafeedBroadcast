var config = require('./config');
var Model = require('./model');

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
    quote.time = new Date();//data.time;
    console.log("quote : " + JSON.stringify(quote));

    new Model.Quote({symbol: quote.symbol}).save(quote, {patch: true}).then(function(model) {
        console.log("model : " + JSON.stringify(model));
    }).catch(error => {
        console.log("error : " + JSON.stringify(error));
        new Model.Quote().save(quote).catch(error => {
            console.log("error 2 : " + JSON.stringify(error));
        });
    });
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});

console.log("Connecting to push server : " + config.pushServer.url);