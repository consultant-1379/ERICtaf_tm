/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignGroupWidget.html',
    'styles!./TestCampaignGroupWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        afterRender: function () {
            var element = this.getElement();
            this.content = element.find('.eaTM-TestCampaignGroup-content');
            this.totalCount = element.find('.eaTM-TestCampaignGroup-totalCount');
            this.resultHolder = element.find('.eaTM-TestCampaignGroup-resultsHolder');
            this.treeHolder = element.find('.eaTM-TestCampaignGroup-treeHolder');
            this.tableHolder = element.find('.eaTM-TestCampaignGroup-tableHolder');
            this.totalTestCases = element.find('.eaTM-TestCampaignGroup-totalTestCases');
            this.paginationHolder = element.find('.eaTM-TestCampaignGroup-paginationHolder');
            this.percentage = element.find('.eaTM-TestCampaignGroup-percentage');
            this.chart = element.find('.eaTM-TestCampaignGroup-chart');
            this.search = element.find('.eaTM-TestCampaignGroup-searchInput');
            this.editGroup = element.find('.eaTM-TestCampaignGroup-editButton');
            this.deleteGroup = element.find('.eaTM-TestCampaignGroup-deleteButton');
            this.detailedProgressBar = element.find('.eaTM-TestCampaignGroup-detailedProgressBar');
            this.urlGroup = element.find('.eaTM-TestCampaignGroup-urlButton');
            this.csvExportButton = element.find('.eaTM-TestCampaignGroup-exportButton');
            this.sprintValidationReportButton = element.find('.eaTM-TestCampaignGroup-sprintValidationReportButton');
            this.sovStatusReportButton = element.find('.eaTM-TestCampaignGroup-sovStatusReportButton');

        },

        getStyle: function () {
            return styles;
        },

        getContent: function () {
            return this.content;
        },

        getTotalCount: function () {
            return this.totalCount;
        },

        getResultHolder: function () {
            return this.resultHolder;
        },

        getTreeHolder: function () {
            return this.treeHolder;
        },

        getTableHolder: function () {
            return this.tableHolder;
        },

        getTotalTestCases: function () {
            return this.totalTestCases;
        },

        getPaginationHolder: function () {
            return this.paginationHolder;
        },

        getPercentageHolder: function () {
            return this.percentage;
        },

        getSearch: function () {
            return this.search;
        },

        getChart: function () {
            return this.chart;
        },

        getEditButton: function () {
            return this.editGroup;
        },

        getDeleteButton: function () {
            return this.deleteGroup;
        },

        getDetailedProgressBar: function () {
            return this.detailedProgressBar;
        },

        getUrlButton: function () {
            return this.urlGroup;
        },

        getExportButton: function () {
            return this.csvExportButton;
        },

        showExportButton: function () {
             this.csvExportButton.setModifier('show');
        },

        hideExportButton: function () {
            this.csvExportButton.removeModifier('show');
        },

        getSprintValidationReportButton: function () {
            return this.sprintValidationReportButton;
        },

        getSovStatusReportButton: function () {
            return this.sovStatusReportButton;
        },

        showSprintValidationReportButton: function () {
             this.sprintValidationReportButton.setModifier('show');
        },

        showSovStatusReportButton: function () {
             this.sovStatusReportButton.setModifier('show');
        },

        hideSprintValidationReportButton: function () {
             this.sprintValidationReportButton.removeModifier('show');
       },

        hideSovStatusReportButton: function () {
             this.sovStatusReportButton.removeModifier('show');
        }

    });

});
