var config = require('./config');
var firebase = require("firebase");

firebase.initializeApp({
    serviceAccount: "firebase-admin.json",
    databaseURL: config.firebase.databaseURL
});
var socket = require('socket.io-client')(config.pushClient.url);
socket.on('connect', function(){
    console.log("on connect");
    socket.on('quote', function(quote) {
        console.log("quote - " + JSON.stringify(quote));
        var quoteRef = firebase.database().ref("quote");
        var quoteChildRef = quoteRef.child(quote.symbol);
        quoteChildRef.set(quote);
    });
    socket.on('disconnect', function(){
        console.log("on disconnect");
    });
    
});