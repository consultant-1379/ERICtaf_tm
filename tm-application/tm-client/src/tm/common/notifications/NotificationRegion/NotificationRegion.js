define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/Notification',
    './NotificationRegionView',
    '../models/NotificationModel',
    '../models/NotificationsCollection',
    '../../Constants',
    '../../ModelHelper',
    '../../ServerEvents',
    '../../LocalStorage'
], function (core, _, Notification, View, NotificationModel, NotificationsCollection,
             Constants, ModelHelper, ServerEvents, LocalStorage) {
    'use strict';
    /* jshint validthis: true */

    var NotificationRegion = core.Region.extend({

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.notifications = new NotificationsCollection();
            this.persistent = {};
            this.messagesWidgets = [];
        },

        onViewReady: function () {
            this.view.afterRender();

            this.eventBus.subscribe(Constants.events.NOTIFICATION, this.notify, this);
            this.eventBus.subscribe(Constants.events.SHOW_ERROR_BLOCK, this.onShowErrorBlock, this);
            this.eventBus.subscribe(Constants.events.HIDE_ERROR_BLOCK, this.onHideErrorBlock, this);
            ServerEvents.subscribe('notification', handlePushNotification, this);

            this.update();
        },

        update: function () {
            this.notifications.setTimestamp(Date.now());
            this.notifications.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: updateAllNotifications.bind(this)
                })
            });
        },

        notify: function (text, options) {
            options = options || {};
            var canClose = options.canClose != null ? options.canClose : true;
            var canDismiss = options.canDismiss != null ? options.canDismiss : false;
            var notification = new Notification({
                label: text,
                color: options.color,
                icon: options.icon,
                showCloseButton: canClose,
                showAsToast: false,
                autoDismiss: canDismiss
            });
            notification.attachTo(this.view.getHolder());
            if (canDismiss) {
                this.messagesWidgets.push(notification);
            }
            return notification;
        },

        onShowErrorBlock: function (params) {
            [].concat(params).forEach(function (messageObj) {
                var options = NotificationRegion.NOTIFICATION_TYPES.error;
                options.canDismiss = true;
                options.canClose = true;
                this.eventBus.publish(Constants.events.NOTIFICATION, messageObj.message, options);
            }.bind(this));

        },

        onHideErrorBlock: function () {
            this.messagesWidgets.forEach(function (messageWidget, index) {
                messageWidget.destroy();
                delete this.messagesWidgets[index];
            }.bind(this));
        }

    }, {
        LOCAL_STORAGE_KEY: 'notificationsClosed',
        NOTIFICATION_TYPES: {
            info: {color: 'paleBlue', icon: 'dialogInfo', canClose: true},
            success: {color: 'green', icon: 'tick', canClose: true},
            warning: {color: 'yellow', icon: 'warning', canClose: false},
            invalid: {color: 'red', icon: 'invalid', canClose: true},
            error: {color: 'red', icon: 'error', canClose: false}
        },
        create: function (text, options) {
            options = options || {};
            var canClose = options.canClose != null ? options.canClose : true;
            var canDismiss = options.canDismiss != null ? options.canDismiss : false;
            var notification = new Notification({
                label: text,
                color: options.color,
                icon: options.icon,
                showCloseButton: canClose,
                showAsToast: false,
                autoDismiss: canDismiss
            });
            return notification;
        }
    });

    var handlePushNotification = function (data) {
        updateNotification.call(this, new NotificationModel(data));
    };

    var updateNotification = function (model) {
        var id = model.getId();
        var widget = this.persistent[id];
        if (widget != null) {
            // Update
            widget.setLabel(model.getText());
        } else if (!isClosedNotification(id)) {
            // Create new
            var typeTitle = model.getType().title;
            var type = (typeTitle != null ? typeTitle.toLowerCase() : null);
            var options = NotificationRegion.NOTIFICATION_TYPES[type];
            widget = this.notify(model.getText(), options);
            if (id != null) {
                this.persistent[id] = widget;
                widget.addEventHandler('close', closeNotification.bind(this, id));
            }
        }
    };

    var updateAllNotifications = function () {
        var expired = _.clone(this.persistent);

        this.notifications.each(function (model) {
            updateNotification.call(this, model);
            var id = model.getId();
            if (id != null) {
                delete expired[id]; // Mark as alive
            }
        }.bind(this));

        // Remove all that weren't marked as alive
        _.each(expired, function (widget) {
            widget.close();
        });
    };

    var getClosedNotifications = function () {
        return LocalStorage.get(NotificationRegion.LOCAL_STORAGE_KEY, {});
    };

    var isClosedNotification = function (id) {
        var closed = getClosedNotifications();
        return closed[id];
    };

    var closeNotification = function (id) {
        delete this.persistent[id];
        var closed = getClosedNotifications();
        closed[id] = true;
        LocalStorage.set(NotificationRegion.LOCAL_STORAGE_KEY, closed);
    };

    return NotificationRegion;

});
