var config = require('./config');

var server = require('http').createServer();
var io = require('socket.io')(server);

io.on('connection', function(client){
    console.log("[pushServer] on connection : " + client);
    client.on('disconnect', function(){
        console.log("on disconnect");
    });
});
server.listen(config.relayServer.port);

module.exports = {
   io: io,
};