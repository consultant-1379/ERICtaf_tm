define([
    'jscore/core',
    'text!./TestCampaignFormWidget.html',
    'styles!./TestCampaignFormWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.filterHolder = element.find('.eaTM-EditTestCampaignDetails-filterHolder');
            this.testPlanDescription = element.find('.eaTM-EditTestCampaignDetails-description');
            this.testPlanName = element.find('.eaTM-EditTestCampaignDetails-name');
            this.environment = element.find('.eaTM-EditTestCampaignDetails-environment');
            this.startDate = element.find('.eaTM-EditTestCampaignDetails-startDate');
            this.endDate = element.find('.eaTM-EditTestCampaignDetails-endDate');
            this.autoUpdateHolder = element.find('.eaTM-EditTestCampaignDetails-autoUpdate');
            this.autoUpdateCheckbox = element.find('.eaTM-EditTestCampaignDetails-autoUpdateCheckbox');
            this.group = element.find('.eaTM-EditTestCampaignDetails-group');
            this.psFrom = element.find('.eaTM-EditTestCampaignDetails-psFrom');
            this.psTo = element.find('.eaTM-EditTestCampaignDetails-psTo');
            this.guideRevision = element.find('.eaTM-EditTestCampaignDetails-guideRevision');
            this.sedRevision = element.find('.eaTM-EditTestCampaignDetails-sedRevision');
            this.otherDependentSW = element.find('.eaTM-EditTestCampaignDetails-otherDependentSW');
            this.nodeTypeVersion = element.find('.eaTM-EditTestCampaignDetails-nodeTypeVersion');
            this.sovStatus = element.find('.eaTM-EditTestCampaignDetails-sovStatus');
            this.comment = element.find('.eaTM-EditTestCampaignDetails-comment');

        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getFilterHolder: function () {
            return this.filterHolder;
        },

        getTestPlanName: function () {
            return this.testPlanName;
        },

        getTestPlanDescription: function () {
            return this.testPlanDescription;
        },

        getEnvironment: function () {
            return this.environment;
        },

        getStartDate: function () {
            return this.startDate;
        },

        getEndDate: function () {
            return this.endDate;
        },

        getAutoUpdateHolder: function () {
            return this.autoUpdateHolder;
        },

        getAutoUpdateCheckbox: function () {
            return this.autoUpdateCheckbox;
        },

        getGroup: function () {
            return this.group;
        },

        getPSFrom: function () {
            return this.psFrom;
        },

        getPSTo: function () {
            return this.psTo;
        },

        getGuideRevision: function () {
            return this.guideRevision;
        },

        getSedRevision: function () {
            return this.sedRevision;
        },

        getOtherDependentSW: function () {
            return this.otherDependentSW;
        },

        getNodeTypeVersion: function () {
            return this.nodeTypeVersion;
        },
        getSovStatus: function () {
            return this.sovStatus;
        },
        getComment: function () {
            return this.comment;
        }

    });

});
