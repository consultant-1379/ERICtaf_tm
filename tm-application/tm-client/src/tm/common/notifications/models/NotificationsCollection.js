/*global define*/
define([
    '../../../ext/mvpCollection',
    './NotificationModel'
], function (Collection, NotificationModel) {
    'use strict';

    return Collection.extend({

        Model: NotificationModel,

        init: function () {
            this.timestamp = Date.now();
        },

        url: function () {
            return '/tm-server/api/notifications?timestamp=' + this.timestamp;
        },

        getTimestamp: function () {
            return this.timestamp;
        },

        setTimestamp: function (timestamp) {
            this.timestamp = timestamp;
        }

    });

});
