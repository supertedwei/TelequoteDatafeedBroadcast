var HashMap = require('hashmap');
var Model = require('./model');

var map = new HashMap();

var refreshCache = function() {
    new Model.AccountQuote().fetchAll().then(function (list) {
        newMap = new HashMap();
        list.forEach(function (model) {
            console.log(model.attributes)
            username = model.get("username");
            symbol = model.get("symbol");
            newMap.set(username + "_" + symbol, true);
        })  

        
        map = newMap;
        console.log("replaced newMap : " + JSON.stringify(map));
        // _.each(list.models, function (model) {
        //     console.log(JSON.stringify(model));
        // })
    });
}

var has = function(key) {
    console.log("key : " + key);
    return map.has(key);
}

module.exports = {
   refreshCache: refreshCache,
   has: has,
};
