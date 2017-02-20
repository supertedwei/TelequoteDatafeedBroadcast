var realtimeDB = require('./db').realtimeDB;

var Account = realtimeDB.Model.extend({
    tableName: 'datafeed_accounts',
    idAttribute: 'username',
});

var AccountQuote = realtimeDB.Model.extend({
    tableName: 'datafeed_accounts_quotes',
});

module.exports = {
   Account: Account,
   AccountQuote: AccountQuote,
};