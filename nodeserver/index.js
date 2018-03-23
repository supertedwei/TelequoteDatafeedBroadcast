require('console-stamp')(console, '[HH:MM:ss.l]');
var schedule = require('node-schedule');

console.log("Starting NodeServer ...")

var accountQuoteCache = require('./account-quote-cache');
accountQuoteCache.refreshCache();

schedule.scheduleJob('*/3 * * * * *', accountQuoteCache.refreshCache);

var auth = require('./auth');
auth.resetOnlinestatus();

var pushService = require('./push-service');

