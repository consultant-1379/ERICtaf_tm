/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignExecutionContentRegion.html',
    'styles!./TestCampaignExecutionContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-TestCampaignExecutionContentRegion-details');
            this.slidingPanels = element.find('.eaTM-TestCampaignExecutionContentRegion-slidingPanels');
            this.filterContent = element.find('.eaTM-TestCampaignExecutionContentRegion-filterContent');
            this.settingsContent = element.find('.eaTM-TestCampaignExecutionContentRegion-settingsContent');
            this.testCasesTable = element.find('.eaTM-TestCampaignExecutionContentRegion-testCasesTable');
            this.testExecutions = element.find('.eaTM-TestCampaignExecutionContentRegion-testExecutions');
            this.csvExportButton = element.find('.eaTM-TestCampaignExecutionContentRegion-exportButton');
            this.csvExportAllButton = element.find('.eaTM-TestCampaignExecutionContentRegion-exportAllButton');
            this.gatExportAllButton = element.find('.eaTM-TestCampaignExecutionContentRegion-gatexportAllButton');
            this.filterButton = element.find('.eaTM-TestCampaignExecutionContentRegion-filterButton');
            this.hideButton = element.find('.eaTM-TestCampaignExecutionContentRegion-hideButton');
            this.settingsButton = element.find('.eaTM-TestCampaignExecutionContentRegion-settingsButton');
            this.hideSettingsButton = element.find('.eaTM-TestCampaignExecutionContentRegion-hideSettingsButton');
            this.executeMultipleButton = element.find('.eaTM-TestCampaignExecutionContentRegion-executeMultipleButton');
            this.stackedProgessBarHolder = element.find('.eaTM-TestCampaignExecutionContentRegion-stackedProgressBarHolder');
            this.totalCount = element.find('.eaTM-TestCampaignExecutionContentRegion-totalCount');
            this.hideButton.setModifier('hidden');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        },

        getSlidingPanels: function () {
            return this.slidingPanels;
        },

        getFilterContent: function () {
            return this.filterContent;
        },

        getTestCasesTable: function () {
            return this.testCasesTable;
        },

        getTestExecutions: function () {
            return this.testExecutions;
        },

        getCSVExportButton: function () {
            return this.csvExportButton;
        },

        getCSVExportAllButton: function () {
            return this.csvExportAllButton;
        },

         getGATExportAllButton: function () {
            return this.gatExportAllButton;
                },

        getFilterButton: function () {
            return this.filterButton;
        },

        getHideButton: function () {
            return this.hideButton;
        },

        getSettingsButton: function () {
            return this.settingsButton;
        },

        getHideSettingsButton: function () {
            return this.hideSettingsButton;
        },

        getExecuteMultipleButton: function () {
            return this.executeMultipleButton;
        },

        getSettingsContent: function () {
            return this.settingsContent;
        },

        getStackedProgessBarHolder: function () {
            return this.stackedProgessBarHolder;
        },

        getTotalCount: function () {
            return this.totalCount;
        }

    });

});
