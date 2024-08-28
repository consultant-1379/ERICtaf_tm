/*global define*/
define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: '/tm-server/api/test-campaigns/test-cases',

        getId: function () {
            return this.getAttribute('id');
        },

        setId: function (id) {
            return this.setAttribute('id', id);
        },

        getTestCase: function () {
            return this.getAttribute('testCase');
        },

        setTestCase: function (testCase) {
            return this.setAttribute('testCase', testCase);
        },

        getTestCasePk: function () {
            return this.getTestCase().id;
        },

        setTestCasePk: function (testCasePk) {
            this.getTestCase().id = testCasePk;
        },

        getTestCaseId: function () {
            return this.getTestCase().testCaseId;
        },

        setTestCaseId: function (testCaseId) {
            this.getTestCase().testCaseId = testCaseId;
        },

        getTestCaseVersionPk: function () {
            return this.getTestCase().versionId;
        },

        setTestCaseVersionPk: function (testCaseVersionPk) {
            this.getTestCase().versionId = testCaseVersionPk;
        },

        getTestCaseTitle: function () {
            return this.getTestCase().title;
        },

        setTestCaseTitle: function (testCaseTitle) {
            this.getTestCase().title = testCaseTitle;
        },

        getTestCaseDescription: function () {
            return this.getTestCase().description;
        },

        setTestCaseDescription: function (testCaseDescription) {
            this.getTestCase().description = testCaseDescription;
        },

        getTestCaseSequenceNumber: function () {
            return this.getTestCase().version;
        },

        setTestCaseSequenceNumber: function (testCaseSequenceNumber) {
            this.getTestCase().version = testCaseSequenceNumber;
        },

        getTestCaseComment: function () {
            return this.getTestCase().comment;
        },

        setTestCaseComment: function (testCaseComment) {
            this.getTestCase().comment = testCaseComment;
        },

        getRequirementIds: function () {
            return this.getTestCase().requirementIds;
        },

        setRequirementIds: function (requirementIds) {
            this.getTestCase().requirementIds = requirementIds;
        },

        getUser: function () {
            return this.getAttribute('user');
        },

        setUser: function (user) {
            this.setAttribute('user', user);
        },

        getResult: function () {
            return this.getAttribute('result');
        },

        setResult: function (result) {
            this.setAttribute('result', result);
        },

        getDefectIds: function () {
            return this.getAttribute('defectIds');
        },

        setDefectIds: function (defectIds) {
            this.setAttribute('defectIds', defectIds);
        },

        setTestCaseVersions: function (versions) {
            this.setAttribute('testCaseVersions', versions);
        },

        getTestCaseVersions: function () {
            return this.getAttribute('testCaseVersions');
        },

        getFeature: function () {
            return this.getTestCase().feature;
        },

        setFeature: function (feature) {
            this.getTestCase().feature = feature;
        },

        getTechnicalComponents: function () {
            return this.getTestCase().technicalComponents;
        },

        setTechnicalComponents: function (technicalComponents) {
            this.getTestCase().technicalComponents = technicalComponents;
        }
    });

});
