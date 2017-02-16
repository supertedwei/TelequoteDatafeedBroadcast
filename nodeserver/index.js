
var config = require('./config');

// Push Server
var pushServer = require('http').createServer();
var io = require('socket.io')(pushServer);
io.on('connection', function(client){
    console.log("[pushServer] on connection : " + client);
    client.on('disconnect', function(){
        console.log("on disconnect");
    });
});
pushServer.listen(config.pushServer.port);
console.log("Push Server started at port : " + config.pushServer.port);


// Datafeed Client
var socket = require('socket.io-client')(config.datafeedServer.url);
socket.on('connect', function(){
    console.log("on connect");
});
socket.on('counter', function(data){
    console.log("on counter : " + JSON.stringify(data));
    io.sockets.emit("counter", data);
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});