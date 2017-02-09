
var socket = require('socket.io-client')('http://localhost:3000');
socket.on('connect', function(){
    console.log("on connect");
});
socket.on('event', function(data){
    console.log("on event");
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});