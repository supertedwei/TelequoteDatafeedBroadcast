var schedule = require('node-schedule');

var accountQuoteCache = require('./account-quote-cache');
accountQuoteCache.refreshCache();

schedule.scheduleJob('*/3 * * * * *', accountQuoteCache.refreshCache);

var auth = require('./auth');
auth.resetOnlinestatus();

var pushService = require('./push-service');

