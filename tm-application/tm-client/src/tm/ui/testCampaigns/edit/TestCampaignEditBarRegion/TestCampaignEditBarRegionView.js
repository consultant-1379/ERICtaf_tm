/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignEditBarRegion.html',
    'styles!./TestCampaignEditBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.saveLinkHolder = element.find('.eaTM-EditTestCampaignBar-saveLinkHolder');
            this.cancelLinkHolder = element.find('.eaTM-EditTestCampaignBar-cancelLinkHolder');
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

        getSaveLinkHolder: function () {
            return this.saveLinkHolder;
        },

        getCancelLinkHolder: function () {
            return this.cancelLinkHolder;
        }
    });

});
