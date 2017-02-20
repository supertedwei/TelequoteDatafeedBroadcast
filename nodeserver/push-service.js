var auth = require('./auth');
var config = require('./config');
var accountQuoteCache = require('./account-quote-cache');

// Push Server
var pushServer = require('http').createServer();
var io = require('socket.io')(pushServer);
io.on('connection', function(client){
    console.log("[pushServer] on connection : " + client);
    client.on('disconnect', function(){
        console.log("on disconnect");
    });
});

require('socketio-auth')(io, {
    authenticate: auth.authenticate,
    postAuthenticate: auth.postAuthenticate,
    disconnect: disconnect,
    timeout: 1000
});

function disconnect(socket) {
  console.log(socket.id + ' disconnected');
  if (socket.username != null) {
      console.log('Disconnect user : ' + socket.username);
      auth.setOffline(socket.username);
  }
}


pushServer.listen(config.pushServer.port);
console.log("Push Server started at port : " + config.pushServer.port);


// Datafeed Client
var socket = require('socket.io-client')(config.datafeedServer.url);
socket.on('connect', function(){
    console.log("on connect");
});
socket.on('counter', function(counter){
    ////console.log("on counter : " + JSON.stringify(data));
    // io.sockets.emit("counter", counter);

    Object.keys(io.sockets.sockets).forEach(id => {
        // console.log("id : " + id);
        socket = io.sockets.sockets[id];
        console.log("socket.username : " + socket.username);
        key1 = socket.username + "_All"
        key2 = socket.username + "_" + counter.symbol;
        if (accountQuoteCache.has(key1) || accountQuoteCache.has(key2)) {
            socket.emit("counter", counter);
        }
    });

});
socket.on('disconnect', function(){
    console.log("on disconnect");
});