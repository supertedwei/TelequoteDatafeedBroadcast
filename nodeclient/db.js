var Bookshelf = require('bookshelf');
var config = require('./config');

var realtimeKnex = require('knex')({
  client: 'mysql',
  connection: {
    host: config.realtimeDb.host,
    user: config.realtimeDb.user,
    password: config.realtimeDb.password,
    database: config.realtimeDb.database,
    "timezone": "UTC"
  }
});

var realtimeDB = Bookshelf(realtimeKnex);

module.exports.realtimeDB = realtimeDB;
