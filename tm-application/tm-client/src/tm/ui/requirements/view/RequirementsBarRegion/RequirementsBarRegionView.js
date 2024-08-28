define([
    'jscore/core',
    'text!./RequirementsBarRegion.html',
    'styles!./RequirementsBarRegion.less'
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
        },

        getSelectProjectBox: function () {
            return this.getElement().find('.eaTM-RequirementsBar-selectProjectBox');
        }

    });

});
