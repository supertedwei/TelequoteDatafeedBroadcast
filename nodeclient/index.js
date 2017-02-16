var config = require('./config');

var socket = require('socket.io-client')(config.pushServer.url);
socket.on('connect', function(){
    console.log("on connect");
    // socket.emit('new message', "Hello World!");
});
// socket.on('new broadcast data', function(data){
//     console.log("on new broadcast data : " + data);
// });
// socket.on('event', function(data){
//     console.log("on event : " + data);
// });
socket.on('counter', function(data){
    console.log("on counter : " + JSON.stringify(data));
});
socket.on('disconnect', function(){
    console.log("on disconnect");
});

console.log("Connecting to push server : " + config.pushServer.url);