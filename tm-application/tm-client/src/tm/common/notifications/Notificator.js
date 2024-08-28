define([
    'jscore/ext/utils/base/underscore',
    '../Constants',
    './NotificationRegion/NotificationRegion'
], function (_, Constants, NotificationRegion) {
    'use strict';

    var Notificator = function (eventBus) {
        this.eventBus = eventBus;
    };

    _.each(NotificationRegion.NOTIFICATION_TYPES, function (options, type) {
        Notificator.prototype[type] = function (text) {
            this.eventBus.publish(Constants.events.NOTIFICATION, text, options);
        };
    });

    return Notificator;

});
