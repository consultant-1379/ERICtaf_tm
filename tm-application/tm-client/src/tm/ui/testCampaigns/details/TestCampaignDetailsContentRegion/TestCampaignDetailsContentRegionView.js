/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignDetailsContentRegion.html',
    'styles!./TestCampaignDetailsContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.messagesBlock = element.find('.eaTM-TestCampaignDetailsContentRegion-messages');
            this.detailsBlock = element.find('.eaTM-TestCampaignDetailsContentRegion-details');
            this.testCaseList = element.find('.eaTM-TestCampaignDetailsContentRegion-testCasesTable');
            this.stackedProgressBar = element.find('.eaTM-TestCampaignDetailsContentRegion-stackedProgressBarHolder');
            this.totalCount = element.find('.eaTM-TestCampaignDetailsContentRegion-totalCount');
            this.slidingPanels = element.find('.eaTM-TestCampaignDetailsContentRegion-slidingPanels');
            this.settingsButton = element.find('.eaTM-TestCampaignDetailsContentRegion-settingsButton');
            this.hideSettingsButton = element.find('.eaTM-TestCampaignDetailsContentRegion-hideSettingsButton');
            this.settingsContent = element.find('.eaTM-TestCampaignDetailsContentRegion-settingsContent');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getMessagesBlock: function () {
            return this.messagesBlock;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        },

        getTestCaseList: function () {
            return this.testCaseList;
        },

        getStackedProgressBarHolder: function () {
            return this.stackedProgressBar;
        },

        getTotalCount: function () {
            return this.totalCount;
        },

        getSlidingPanels: function () {
            return this.slidingPanels;
        },

        getSettingsContent: function () {
            return this.settingsContent;
        },

        getSettingsButton: function () {
            return this.settingsButton;
        },

        getHideSettingsButton: function () {
            return this.hideSettingsButton;
        }

    });

});
