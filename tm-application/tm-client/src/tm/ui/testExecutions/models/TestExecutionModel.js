define([
    '../../../ext/mvpModel',
    '../../../common/DateHelper'
], function (Model, DateHelper) {
    'use strict';

    return Model.extend({

        init: function () {
            this.testPlanId = '';
            this.testCaseId = '';
        },

        url: function () {
            if (!this.testPlanId || !this.testCaseId) {
                throw new Error('TestExecutionModel should have testCampaignId and testCaseId!');
            }
            return '/tm-server/api/test-campaigns/' + this.testPlanId + '/test-cases/' + this.testCaseId + '/executions/';
        },

        getId: function () {
            return this.getAttribute('id');
        },

        getProduct: function () {
            return this.getAttribute('product');
        },

        setTestPlanId: function (testPlanId) {
            this.testPlanId = testPlanId;
            this.setAttribute('testPlan', testPlanId);
        },

        getTestCaseId: function () {
            return this.testCaseId;
        },

        setTestCaseId: function (testCaseId) {
            this.testCaseId = testCaseId;
            this.setAttribute('testCase', testCaseId);
        },

        getExecutionResultTitle: function () {
            var resultObj = this.getExecutionResultObj();
            if (resultObj !== null) {
                return resultObj.title;
            }
            return '';
        },

        getExecutionResultId: function () {
            var resultObj = this.getExecutionResultObj();
            if (resultObj) {
                return resultObj.id;
            }
            return '';
        },

        setTestStepExecutions: function (testStepExecutions) {
            this.setAttribute('testStepExecutions', testStepExecutions);
        },

        setVerifyStepExecutions: function (verifyStepExecutions) {
            this.setAttribute('verifyStepExecutions', verifyStepExecutions);
        },

        getExecutionResultObj: function () {
            return this.getAttribute('result');
        },

        setExecutionResult: function (result) {
            this.setAttribute('result', result);
        },

        getAuthor: function () {
            return this.getAttribute('author');
        },

        getComment: function () {
            return this.getAttribute('comment');
        },

        setComment: function (comment) {
            this.setAttribute('comment', comment);
        },

        getDate: function () {
            var dateStr = this.getAttribute('createdAt');
            return DateHelper.formatStringToDatetime(dateStr);
        },

        getDefectIds: function () {
            return this.getAttribute('defectIds');
        },

        setDefectIds: function (defectIds) {
            this.setAttribute('defectIds', defectIds);
        },

        getRequirementIds: function () {
            return this.getAttribute('requirementIds');
        },

        setRequirementIds: function (requirementIds) {
            this.setAttribute('requirementIds', requirementIds);
        },

        getExecutionTime: function () {
            return this.getAttribute('executionTime');
        },

        setExecutionTime: function (time) {
            return this.setAttribute('executionTime', time);
        },

        getIso: function () {
            return this.getAttribute('iso');
        },

        setIso: function (iso) {
            this.setAttribute('iso', iso);
        },

        getIsoVersion: function () {
            var iso = this.getIso();
            if (iso) {
                return iso.version;
            }
            return null;
        },

        getKpiMeasurement: function () {
            return this.getAttribute('kpiMeasurement');
        },

        setKpiMeasurement: function (kpi) {
            this.setAttribute('kpiMeasurement', kpi);
        }

    });

});
