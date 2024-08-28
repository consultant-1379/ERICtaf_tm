define([
    '../../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/test-campaigns/' + this.getTestPlanId() + '/test-cases';
        },

        getTestPlanId: function () {
            return this.getAttribute('testPlanId');
        },

        setTestPlanId: function (testPlanId) {
            this.setAttribute('testPlanId', testPlanId);
        },

        setTestCases: function (testCases) {
            this.setAttribute('testCases', testCases);
        },

        getTestCases: function () {
            return this.getAttribute('testCases');
        }

    });

});
