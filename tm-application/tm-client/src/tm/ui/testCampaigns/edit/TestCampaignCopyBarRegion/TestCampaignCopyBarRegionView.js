/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignCopyBarRegion.html',
    'styles!./TestCampaignCopyBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.createLinkHolder = element.find('.eaTM-CopyTestCampaignBar-createLinkHolder');
            this.cancelLinkHolder = element.find('.eaTM-CopyTestCampaignBar-cancelLinkHolder');
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
