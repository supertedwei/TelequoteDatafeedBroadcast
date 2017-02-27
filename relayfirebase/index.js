var config = require('./config');
var firebase = require("firebase");

var cache = {};

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
        var encodedSymbol = encode(quote.symbol);
        var quoteChildRef = quoteRef.child(encodedSymbol);
        quoteChildRef.set(quote);
    });
    socket.on('disconnect', function(){
        console.log("on disconnect");
    });
    
});

var encode = function(input) {
    var output = cache[input];
    if (output == null) {
        output = input.replace(".", "_");
        output = output.replace("#", "_");
        output = output.replace("$", "_");
        output = output.replace("[", "_");
        output = output.replace("]", "_");
        cache[input] = output;
    }
    return output;
}