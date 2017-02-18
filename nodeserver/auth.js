var Model = require('./model');

var setOnline = function(username) {
    new Model.Account({username: username})
        .save({onlinestatus: "online"}, {patch: true});
}

var setOffline = function(username) {
    new Model.Account({username: username})
        .save({onlinestatus: "offline"}, {patch: true});
}

var resetOnlinestatus = function() {
    new Model.Account().where({onlinestatus: "online"})
        .save({onlinestatus: "offline"}, {patch: true})
        .catch(function(e) {
            console.log("no online rows to update");
        });
}

var authenticate = function (socket, data, callback) {
    var username = data.username;
    var password = data.password;
    console.log("[authenticate] username : " + username);

    new Model.Account({username: username})
        .fetch({columns: ['username', 'password', 'active', 'onlinestatus']})
        .then(function(account) {
            var strAccount = JSON.stringify(account);
            console.log("[authenticate] account : " + strAccount);

            if (account == null) {
                return callback(new Error("User not found"));
            }

            if (password != account.get("password")) {
                return callback(new Error("Password is incorrect"));
            }

            if (account.get("active") != '1') {
                return callback(new Error("Account is inactive"));
            }

            if (account.get("onlinestatus") != "offline") {
                return callback(new Error("Only one connection for each account is allowed"));
            }

            setOnline(username);
            socket.username = username;
            return callback(null, true);
    });

}

var postAuthenticate = function (socket, data) {
    // var username = data.username;
    
    // db.findUser('User', {username:username}, function(err, user) {
    //     socket.client.user = user;
    // });
}

module.exports = {
   authenticate: authenticate,
   setOffline: setOffline,
   resetOnlinestatus: resetOnlinestatus,
   postAuthenticate: postAuthenticate,
};