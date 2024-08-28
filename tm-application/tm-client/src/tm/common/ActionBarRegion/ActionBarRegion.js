define([
    'jscore/core'
], function (core) {
    'use strict';

    return core.Region.extend({

        init: function () {
            this.itemId = '';
            this.flags = {};
        },

        hide: function () {
            if (this.view) {
                this.view.getElement().setStyle('display', 'none');
            }
        },

        show: function () {
            if (this.view) {
                this.view.getElement().setStyle('display', 'block');
            }
        },

        setUrlItemId: function (itemId) {
            this.itemId = itemId;
            this.onUrlItemIdReceived();
        },

        onUrlItemIdReceived: function () {
        },

        getUrlItemId: function () {
            return this.itemId;
        },

        getPrimaryIndex: function () {
            return this.itemId.split(':')[0];
        },

        getFlag: function (name) {
            return this.flags[name];
        },

        setFlags: function (flags) {
            this.flags = flags || {};
        }

    });

});
