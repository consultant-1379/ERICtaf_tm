define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        init: function () {
            this.testPlanId = '';
            this.testExecutions = [];

        },

        url: function () {

            if (!this.testPlanId) {
                throw new Error('TestCaseExecutionsCollection should have testPlanId!');
            }

            return '/tm-server/api/test-campaigns/' + this.testPlanId + '/test-cases/executions';
        },

        getTestCaseId: function () {
            return this.testCaseId;
        },

        setTestPlanId: function (testPlanId) {
            this.testPlanId = testPlanId;
        },

        add: function (testExecution) {
            this.testExecutions.push(testExecution);
        },

        clear: function () {
            this.testExecutions = [];
        },

        getTestExecution: function () {
            return this.testExecutions;
        }

    });

});
