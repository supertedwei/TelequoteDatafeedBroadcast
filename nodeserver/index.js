
var server = require('http').createServer();
var io = require('socket.io')(server);
io.on('connection', function(client){
    console.log("on connection : " + client);
    client.on('new message', function(data){
        console.log("on new message : " + data);
        client.broadcast.emit("new broadcast data", data);
        io.sockets.emit("event", data);
    });
    client.on('event', function(data){
        console.log("on event : " + data);
    });
    client.on('disconnect', function(){
        console.log("on disconnect");
    });
});

server.listen(3000);