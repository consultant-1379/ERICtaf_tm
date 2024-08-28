define([
    '../../../../ext/mvpCollection',
    './TestCaseModel'
], function (Collection, TestCaseModel) {
    'use strict';

    return Collection.extend({

        Model: TestCaseModel,

        init: function () {
            this.testCaseId = null;
        },

        url: function () {
            return '/tm-server/api/test-cases/' + this.testCaseId + '/versions';
        },

        getTestCaseId: function () {
            return this.testCaseId;
        },

        setTestCaseId: function (testCaseId) {
            this.testCaseId = testCaseId;
        }

    });

});
