/*global define*/
define([
    '../../../ext/mvpCollection',
    '../../testCases/models/testCases/TestCaseModel'
], function (Collection, TestCaseModel) {
    'use strict';

    return Collection.extend({

        Model: TestCaseModel,

        init: function () {
            this.requirementId = '';
        },

        url: function () {
            if (this.requirementId === '') {
                throw new Error('Requirement Id should be defined for TestCasesByRequirementCollection!');
            }
            return '/tm-server/api/requirements/' + this.requirementId;
        },

        getRequirementId: function () {
            return this.requirementId;
        },

        setRequirementId: function (requirementId) {
            this.requirementId = requirementId;
        },

        parse: function (data) {
            return data.testCases;
        }

    });

});
