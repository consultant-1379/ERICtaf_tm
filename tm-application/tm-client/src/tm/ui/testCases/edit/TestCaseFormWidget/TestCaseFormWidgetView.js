/*global define*/
define([
    'jscore/core',
    'text!./TestCaseFormWidget.html',
    'styles!./TestCaseFormWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            // jira fields
            this.requirementIdMultiSelect = element.find('.eaTM-TestCaseForm-requirementIdMultiSelect');

            // testCase fields
            this.title = element.find('.eaTM-TestCaseForm-title');
            this.testCaseId = element.find('.eaTM-TestCaseForm-testCaseId');
            this.description = element.find('.eaTM-TestCaseForm-description');
            this.preCondition = element.find('.eaTM-TestCaseForm-preCondition');
            this.intrusiveComment = element.find('.eaTM-TestCaseForm-intrusiveComment');

            // select fields
            this.componentSelect = element.find('.eaTM-TestCaseForm-componentSelect');
            this.productSelect = element.find('.eaTM-TestCaseForm-productSelect');
            this.featureSelect = element.find('.eaTM-TestCaseForm-featureSelect');
            this.typeSelect = element.find('.eaTM-TestCaseForm-typeSelect');
            this.executionTypeSelect = element.find('.eaTM-TestCaseForm-executionTypeSelect');
            this.automationCandidateBlock = element.find('.eaTM-TestCaseForm-automationCandidateBlock');
            this.acSwitcher = element.find('.eaTM-TestCaseForm-automationCandidate-switcher');
            this.prioritySelect = element.find('.eaTM-TestCaseForm-prioritySelect');
            this.groupSelect = element.find('.eaTM-TestCaseForm-groupSelect');
            this.contextSelect = element.find('.eaTM-TestCaseForm-contextSelect');
            this.testCaseStatus = element.find('.eaTM-TestCaseForm-testCaseStatus');
            this.intrusiveSwitcher = element.find('.eaTM-TestCaseForm-intrusive-switcher');
            this.suite = element.find('.eaTM-TestCaseForm-testCaseSuiteSelect');
            this.team = element.find('.eaTM-TestCaseForm-testCaseTeamSelect');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        // jira fields
        getRequirementIdMultiSelect: function () {
            return this.requirementIdMultiSelect;
        },

        // testCase fields
        getTestCaseId: function () {
            return this.testCaseId;
        },
        getTitle: function () {
            return this.title;
        },
        getDescription: function () {
            return this.description;
        },
        getPreCondition: function () {
            return this.preCondition;
        },
        getComponentSelect: function () {
            return this.componentSelect;
        },
        getProductSelect: function () {
            return this.productSelect;
        },
        getFeatureSelect: function () {
            return this.featureSelect;
        },
        getTypeSelect: function () {
            return this.typeSelect;
        },
        getExecutionTypeSelect: function () {
            return this.executionTypeSelect;
        },
        getAutomationCandidateBlock: function () {
            return this.automationCandidateBlock;
        },
        getPrioritySelect: function () {
            return this.prioritySelect;
        },
        getGroupSelect: function () {
            return this.groupSelect;
        },
        getContextSelect: function () {
            return this.contextSelect;
        },
        getTestCaseStatus: function () {
            return this.testCaseStatus;
        },
        getAcSwitcher: function () {
            return this.acSwitcher;
        },
        getIntrusiveSwitcher: function () {
            return this.intrusiveSwitcher;
        },
        getIntrusiveComment: function () {
            return this.intrusiveComment;
        },
        getSuiteSelect: function () {
            return this.suite;
        },
        getTeamSelect: function () {
            return this.team;
        },
        hideAutomationCandidateBlock: function () {
            this.automationCandidateBlock.setModifier('hidden');
        },
        showAutomationCandidateBlock: function () {
            this.automationCandidateBlock.removeModifier('hidden');
        }

    });

});
