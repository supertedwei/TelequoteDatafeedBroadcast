var config = require('./config');

var socket = require('socket.io-client')(config.pushServer.url);
socket.on('connect', function(){
    console.log("on connect");
    socket.emit('authentication', {username: config.pushServer.username, password: config.pushServer.password});
    socket.on('authenticated', function() {
        console.log("authenticated");
    });
});
socket.on('counter', function(data){
    console.log("on counter : " + JSON.stringify(data));
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});

console.log("Connecting to push server : " + config.pushServer.url);