var auth = require('./auth');
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
socket.on('counter', function(data){
    ////console.log("on counter : " + JSON.stringify(data));
    io.sockets.emit("counter", data);
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});