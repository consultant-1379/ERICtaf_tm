define([
    'jscore/core',
    'text!./UserInboxBarRegion.html',
    'styles!./UserInboxBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getBackButton: function () {
            return this.getElement().find('.ebQuickActionBar-back');
        },

        getBackIcon: function () {
            return this.getElement().find('.ebQuickActionBar-backIcon');
        }

    });

});
