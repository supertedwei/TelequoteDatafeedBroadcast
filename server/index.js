
var server = require('http').createServer();
var io = require('socket.io')(server);
io.on('connection', function(client){
    console.log("on connection : " + client);
    client.on('event', function(data){
        console.log("on event");
    });
    client.on('disconnect', function(){
        console.log("on disconnect");
    });
});
server.listen(3000);