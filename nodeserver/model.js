var realtimeDB = require('./db').realtimeDB;

var Account = realtimeDB.Model.extend({
   tableName: 'datafeed_accounts',
   idAttribute: 'username',
});

module.exports = {
   Account: Account,
};