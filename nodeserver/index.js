
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
    authenticate: authenticate,
    postAuthenticate: postAuthenticate,
    disconnect: disconnect,
    timeout: 1000
});

function authenticate(socket, data, callback) {
    var username = data.username;
    var password = data.password;
 
//   if (err || !user) {
//     return callback(new Error("User not found"));
//   } else {
    return callback(null, username == password);
//   };
}

function postAuthenticate(socket, data) {
    // var username = data.username;
    
    // db.findUser('User', {username:username}, function(err, user) {
    //     socket.client.user = user;
    // });
}

function disconnect(socket) {
  console.log(socket.id + ' disconnected');
}


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