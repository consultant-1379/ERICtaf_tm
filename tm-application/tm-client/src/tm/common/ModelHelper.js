define([
    'jscore/ext/utils/base/underscore',
    './Logger',
    './Constants'
], function (_, Logger, Constants) {
    'use strict';

    var logger = new Logger('ModelHelper');

    var ModelHelper = {};

    ModelHelper.authenticationHandler = function (eventBus, codes) {
        codes = codes || {};
        return _.extend({
            401: function () {
                eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
            }
        }, codes);
    };

    ModelHelper.statusCodeHandler = function (eventBus, codes, promise) {
        codes = codes || {};
        return _.extend({
            200: function () {
                if (promise != null) {
                    promise.resolve();
                }
            },
            400: function (data) {
                eventBus.publish(Constants.events.SHOW_ERROR_BLOCK, data.responseJSON);
                if (promise != null) {
                    promise.reject(data.responseJSON);
                }
            },
            401: function () {
                eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                if (promise != null) {
                    promise.reject();
                }
            },
            500: function (data) {
                logger.error(data);
                var arg = [{
                    message: 'Some errors appeared on server when saving data.',
                    developerMessage: data
                }];
                eventBus.publish(Constants.events.SHOW_ERROR_BLOCK, arg);
                if (promise != null) {
                    promise.reject(arg);
                }
            },
            503: function (data) {
                logger.error(data);
                var arg = [{
                    message: 'Unable to communicate with server at this time.'
                }];
                eventBus.publish(Constants.events.SHOW_ERROR_BLOCK, arg);
                if (promise != null) {
                    promise.reject(arg);
                }
            }
        }, codes);
    };

    return ModelHelper;
});
