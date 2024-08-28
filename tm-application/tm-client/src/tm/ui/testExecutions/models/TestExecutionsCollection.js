/*global define*/
define([
    '../../../ext/mvpCollection',
    './TestExecutionModel'
], function (Collection, TestExecutionModel) {
    'use strict';

    return Collection.extend({

        Model: TestExecutionModel,

        init: function () {
            this.testPlanId = '';
            this.testCaseId = '';
            this.perPage = 10;
            this.page = 1;
        },

        url: function () {
            var params = [];

            params.push('perPage=' + this.perPage);
            params.push('page=' + this.page);

            if (!this.testPlanId || !this.testCaseId) {
                throw new Error('TestExecutionsCollection should have testCampaignId and testCaseId!');
            }

            var queryParams = params.join('&');
            return '/tm-server/api/test-campaigns/' + this.testPlanId + '/test-cases/' + this.testCaseId + '/executions?' + queryParams;
        },

        getTestCaseId: function () {
            return this.testCaseId;
        },

        setTestPlanId: function (testPlanId) {
            this.testPlanId = testPlanId;
        },

        setTestCaseId: function (testCaseId) {
            this.testCaseId = testCaseId;
        },
        getPerPage: function () {
            return this.perPage;
        },

        getPage: function () {
            return this.page;
        },

        getTotalCount: function () {
            return this.totalCount;
        },

        setPerPage: function (perPage) {
            this.perPage = perPage;
        },

        setPage: function (page) {
            this.page = page;
        },

        parse: function (data) {
            this.totalCount = data.totalCount;
            return data.items;
        }
    });

});
