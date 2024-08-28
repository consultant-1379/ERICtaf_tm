define([
    'jscore/core',
    'text!./TestCampaignCreateBarRegion.html',
    'styles!./TestCampaignCreateBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.createLinkHolder = element.find('.eaTM-CreateTestCampaignBar-createLinkHolder');
            this.cancelLinkHolder = element.find('.eaTM-CreateTestCampaignBar-cancelLinkHolder');
        },

        getStyle: function () {
            return styles;
        },

        getTemplate: function () {
            return template;
        },

        getBackButton: function () {
            return this.backButton;
        },
        getBackIcon: function () {
            return this.backIcon;
        },

        getCreateLinkHolder: function () {
            return this.createLinkHolder;
        },

        getCancelLinkHolder: function () {
            return this.cancelLinkHolder;
        }

    });

});
