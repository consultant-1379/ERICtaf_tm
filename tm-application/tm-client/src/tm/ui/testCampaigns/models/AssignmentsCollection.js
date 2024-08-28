/*global define*/
define([
    '../../../ext/mvpCollection',
    './AssignmentModel'
], function (Collection, AssignmentModel) {
    'use strict';

    return Collection.extend({

        Model: AssignmentModel,

        init: function () {
            this.searchQuery = '';
            this.testPlanId = null;
            this.baseUrl = '/tm-server/api/test-campaigns/';
        },

        url: function () {
            var queryParams = '';
            if (this.searchQuery !== '') {
                queryParams = '?q=' + this.escape(this.searchQuery);
            }
            return this.baseUrl + this.testPlanId + '/test-cases' + queryParams;
        },

        setSearchQuery: function (searchQuery) {
            this.searchQuery = searchQuery;
        },

        getTestPlanId: function () {
            return this.testPlanId;
        },

        setTestPlanId: function (testPlanId) {
            this.testPlanId = testPlanId;
        }

    });

});
