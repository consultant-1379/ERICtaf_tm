define([
    '../../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: '/tm-server/api/test-cases',

        getId: function () {
            return this.getAttribute('id');
        },

        getSequenceNumber: function () {
            return this.getAttribute('version');
        },

        getTestCaseId: function () {
            return this.getAttribute('testCaseId');
        },

        getType: function () {
            return this.getAttribute('type') ? this.getAttribute('type').title : '';
        },

        getTypeObj: function () {
            return this.getAttribute('type');
        },

        getComponentTitles: function () {
            var componentTitles = [];
            if (this.getAttribute('technicalComponents')) {
                this.getAttribute('technicalComponents').forEach(function (componentObj, index) {
                    componentTitles.push((index > 0 ? ' ' : '') + componentObj.title);
                });
            }
            return componentTitles;
        },

        getComponent: function () {
            return this.getAttribute('component');
        },

        getTechnicalComponents: function () {
            return this.getAttribute('technicalComponents');
        },

        getExecutionType: function () {
            return this.getAttribute('executionType') ? this.getAttribute('executionType').title : '';
        },

        getExecutionTypeObj: function () {
            return this.getAttribute('executionType');
        },

        getAutomationCandidateTitle: function () {
            return this.getAttribute('automationCandidate') ? this.getAttribute('automationCandidate').title : '';
        },

        getAutomationCandidate: function () {
            return this.getAttribute('automationCandidate');
        },

        getTestCaseStatus: function () {
            return this.getAttribute('testCaseStatus') ? this.getAttribute('testCaseStatus').title : '';
        },

        getTestCaseReviewer: function () {
            return this.getAttribute('reviewUser') ? this.getAttribute('reviewUser').userName : this.getAttribute('reviewGroup') ? this.getAttribute('reviewGroup').name : '';
        },

        getTestCaseStatusObj: function () {
            return this.getAttribute('testCaseStatus');
        },

        getPriority: function () {
            return this.getAttribute('priority') ? this.getAttribute('priority').title : '';
        },

        getPriorityObj: function () {
            return this.getAttribute('priority');
        },

        getGroup: function () {
            return this.getAttribute('groups');
        },

        getGroups: function () {
            var groupsTitles = [];
            if (this.getAttribute('groups')) {
                this.getAttribute('groups').forEach(function (groupObj, index) {
                    groupsTitles.push((index > 0 ? ' ' : '') + groupObj.title);
                });
            }
            return groupsTitles;
        },

        getContext: function () {
            return this.getAttribute('contexts');
        },

        getContexts: function () {
            var contextsTitles = [];
            if (this.getAttribute('contexts')) {
                this.getAttribute('contexts').forEach(function (contextObj, index) {
                    contextsTitles.push((index > 0 ? ' ' : '') + contextObj.title);
                });
            }
            return contextsTitles;
        },

        getTitle: function () {
            return this.getAttribute('title');
        },

        getSteps: function () {
            return this.getAttribute('testSteps');
        },

        getRequirements: function () {
            return this.getAttribute('requirements');
        },

        getRequirementIds: function () {
            return this.getAttribute('requirementIds');
        },

        getRequirementId: function () {
            var attr = this.getAttribute('requirementIds');
            return attr ? attr[0] : null;
        },

        getModifications: function () {
            return this.getAttribute('modifications');
        },

        setId: function (id) {
            this.setAttribute('id', id);
        },

        setSequenceNumber: function (sequenceNumber) {
            this.setAttribute('version', sequenceNumber);
        },

        getComment: function () {
            return this.getAttribute('comment');
        },

        setComment: function (comment) {
            this.setAttribute('comment', comment);
        },

        setTestCaseId: function (testCaseId) {
            this.setAttribute('testCaseId', testCaseId);
        },

        setType: function (type) {
            this.setAttribute('type', type);
        },

        setTechnicalComponents: function (technicalComponents) {
            this.setAttribute('technicalComponents', technicalComponents);
        },

        setExecutionType: function (executionType) {
            this.setAttribute('executionType', executionType);
        },

        setAutomationCandidate: function (automationCandidate) {
            this.setAttribute('automationCandidate', automationCandidate);
        },

        setTestCaseStatus: function (testCaseStatus) {
            this.setAttribute('testCaseStatus', testCaseStatus);
        },

        setPriority: function (priority) {
            this.setAttribute('priority', priority);
        },

        setGroup: function (groups) {
            this.setAttribute('groups', groups);
        },

        setContext: function (contexts) {
            this.setAttribute('contexts', contexts);
        },

        setTitle: function (title) {
            this.setAttribute('title', title);
        },

        setSteps: function (testSteps) {
            this.setAttribute('testSteps', testSteps);
        },

        setRequirementIds: function (requirementIds) {
            this.setAttribute('requirementIds', requirementIds);
        },

        setRequirementId: function (requirementId) {
            this.setAttribute('requirementIds', [requirementId]);
        },

        setRequirement: function (requirement) {
            this.setAttribute('requirements', [requirement]);
        },

        getDescription: function () {
            return this.getAttribute('description');
        },

        setDescription: function (description) {
            this.setAttribute('description', description);
        },

        getAssociatedTestPlans: function () {
            return this.getAttribute('associatedTestCampaigns');
        },

        getFeature: function () {
            return this.getAttribute('feature');
        },

        getFeatureTitle: function () {
            return this.getAttribute('feature').name;
        },

        getProduct: function () {
            return this.getAttribute('feature').product;
        },

        setFeature: function (feature) {
            this.setAttribute('feature', feature);
        },

        isIntrusive: function () {
            return this.getAttribute('intrusive');
        },

        setIntrusive: function (intrusive) {
            this.setAttribute('intrusive', intrusive);
        },

        getIntrusiveComment: function () {
            return this.getAttribute('intrusiveComment');
        },

        setIntrusiveComment: function (comment) {
            this.setAttribute('intrusiveComment', comment);
        },

        getSuiteName: function () {
            var suite = this.getSuiteObj();
            if (suite) {
                return suite.title;
            } else {
                return null;
            }
        },

        getSuiteObj: function () {
            return this.getAttribute('testSuite');
        },

        setSuite: function (value) {
            this.setAttribute('testSuite', value);
        },

        getTeamName: function () {
            var team = this.getTeamObj();
                if (team) {
                    return team.title;
                } else {
                    return null;
                }
        },

        getTeamObj: function () {
            return this.getAttribute('testTeam');
        },

        setTeam: function (value) {
            this.setAttribute('testTeam', value);
        },

        getReviewGroup: function () {
            return this.getAttribute('reviewGroup');
        },

        setTestCaseVersions: function (versions) {
            this.setAttribute('testCaseVersions', versions);
        },

        getTestCaseVersions: function () {
            return this.getAttribute('testCaseVersions');
        },

        getPreCondition: function () {
            return this.getAttribute('precondition');
        },

        setPreCondition: function (preCondition) {
            this.setAttribute('precondition', preCondition);
        },

        getReviewUser: function () {
            return this.getAttribute('reviewUser');
        }

    });

});
