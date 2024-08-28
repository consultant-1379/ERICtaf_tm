define([
    'jscore/ext/utils/base/underscore',
    'text!../appInfo.json',
    './Logger'
], function (_, appInfoJson, Logger) {
    /* global EventSource */
    'use strict';

    var ServerEvents = {
        eventSource: null
    };
    var logger = new Logger('ServerEvents');

    ServerEvents.subscribe = function (name, fn, context) {
        context = context || this;
        this.eventSource.addEventListener(name, function (e) {
            var object = JSON.parse(e.data);
            fn.call(context, object);
        }, false);
    };

    var appInfo = JSON.parse(appInfoJson);
    if (appInfo.sse) {
        // SSE enabled: connect to event source
        ServerEvents.eventSource = new EventSource('/tm-server/api/notifications/events');
    } else {
        // SSE disabled: make all methods no-op
        var noop = function () {
        };
        _.each(ServerEvents, function (_, key) {
            ServerEvents[key] = noop;
        });
        logger.warn('SSE disabled in appInfo.json!');
    }

    return ServerEvents;

});
