define([
    'jscore/core',
    'text!./CreateTestExecutionBarRegion.html',
    'styles!./CreateTestExecutionBarRegion.less'
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

        getSaveLinkHolder: function () {
            return this.getElement().find('.eaTM-CreateTestExecutionBar-saveLinkHolder');
        },

        getCancelLinkHolder: function () {
            return this.getElement().find('.eaTM-CreateTestExecutionBar-cancelLinkHolder');
        },

        getProjectHolder: function () {
            return this.getElement().find('.eaTM-CreateTestExecutionBar-projectSelect');
        }

    });

});
