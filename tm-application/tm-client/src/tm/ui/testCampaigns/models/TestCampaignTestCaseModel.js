define([
    '../../testCases/models/testCases/TestCaseModel'
], function (TestCaseModel) {
    'use strict';

    return TestCaseModel.extend({

        init: function () {
            this.testPlanId = null;
            this.baseUrl = '/tm-server/api/test-campaigns/';
        },

        url: function () {
            if (this.testPlanId == null) {
                throw new Error('TestCampaignTestCaseModel should have testCampaignId set!');
            }
            return this.baseUrl + this.testPlanId + '/test-cases';
        },

        getTestPlanId: function () {
            return this.testPlanId;
        },

        setTestPlanId: function (testPlanId) {
            this.testPlanId = testPlanId;
        },

        getTitle: function () {
            return this.getAttribute('title');
        }

    });

});
