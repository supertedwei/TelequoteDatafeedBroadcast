var config = require('./config');

var socket = require('socket.io-client')(config.pushClient.url);
socket.on('connect', function(){
    console.log("on connect");
    socket.on('quote', function(quote) {
        console.log("quote - " + JSON.stringify(quote));
    });
    socket.on('disconnect', function(){
        console.log("on disconnect");
    });
    
});