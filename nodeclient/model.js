var realtimeDB = require('./db').realtimeDB;

/*
on counter : {
    "counterid":"JPY",
    "symbol":"JPY A0-FX",
    "bid":"113.19",
    "ask":"113.0",
    "last":"113.19",
    "change":"-0.13",
    "high":"113.46",
    "low":"113.10",
    "prev_close":"113.32",
    "open":"113.31",
    "time":"09:59:12",
    "hasupdate":false
}
*/
var Quote = realtimeDB.Model.extend({
    tableName: 'quote',
    idAttribute: 'symbol',
});

module.exports = {
   Quote: Quote,
};