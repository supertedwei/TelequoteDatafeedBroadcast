var Model = require('./model');

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




        // var dbPassword = user.get("password");
        // console.log("dbPassword : " + dbPassword);

        // if (dbPassword != md5Password) {
        // res.end();
        // return;
        // } else {
        // // save user info in session
        // var session = req.session;
        // session.user = user.toJSON();
        // console.log("req.session :" + JSON.stringify(session));
        // // create data sent back to client
        // var outUser = {};
        // outUser.userid = session.user.userid;
        // outUser.username = session.user.username;
        // outUser.email = session.user.email;
        // outUser.fullname = session.user.fullname;
        // outUser.countersetid = session.user.account.countersetid;
        // res.end(JSON.stringify(outUser));
        return callback(null, username == password);
    });
 
//   if (err || !user) {
//     return callback(new Error("User not found"));
//   } else {
    
//   };
}

module.exports = {
   authenticate: authenticate,
};