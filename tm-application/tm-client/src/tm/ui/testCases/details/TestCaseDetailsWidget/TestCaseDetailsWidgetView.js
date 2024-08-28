/*global define*/
define([
    'jscore/core',
    'text!./TestCaseDetailsWidget.html',
    'styles!./TestCaseDetailsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            // header fields
            this.lastAuthor = element.find('.eaTM-ViewTestCase-infoName');
            this.versionTitle = element.find('.eaTM-ViewTestCase-infoTitle');

            // jira fields
            this.requirementsBlock = element.find('.eaTM-ViewTestCase-requirements');

            // unknown fields
            this.createdAt = element.find('.eaTM-ViewTestCase-createdAt');
            this.updatedAt = element.find('.eaTM-ViewTestCase-updatedAt');

            // testCase fields
            this.testCaseId = element.find('.eaTM-ViewTestCase-testCaseId');
            this.title = element.find('.eaTM-ViewTestCase-title');
            this.description = element.find('.eaTM-ViewTestCase-description');
            this.type = element.find('.eaTM-ViewTestCase-type');
            this.executionType = element.find('.eaTM-ViewTestCase-executionType');
            this.automationCandidateBlock = element.find('.eaTM-ViewTestCase-automationCandidateBlock');
            this.automationCandidate = element.find('.eaTM-ViewTestCase-automationCandidate');
            this.testCaseStatus = element.find('.eaTM-ViewTestCase-testCaseStatus');
            this.testCaseReviewer = element.find('.eaTM-ViewTestCase-testCaseReviewer');
            this.component = element.find('.eaTM-ViewTestCase-component');
            this.feature = element.find('.eaTM-ViewTestCase-feature');
            this.priority = element.find('.eaTM-ViewTestCase-priority');
            this.group = element.find('.eaTM-ViewTestCase-group');
            this.context = element.find('.eaTM-ViewTestCase-context');
            this.preCondition = element.find('.eaTM-ViewTestCase-preCondition');
            this.revisionsBlock = element.find('.eaTM-ViewTestCase-revisionsBlock');
            this.intrusive = element.find('.eaTM-ViewTestCase-intrusive');
            this.intrusiveComment = element.find('.eaTM-ViewTestCase-intrusiveComment');
            this.suite = element.find('.eaTM-ViewTestCase-suite');
            this.team = element.find('.eaTM-ViewTestCase-team');

            this.compareTestCaseStatus = element.find('.eaTM-ViewTestCase-testCaseStatusCompare');
            this.compareTitle = element.find('.eaTM-ViewTestCase-titleCompare');
            this.compareDescription = element.find('.eaTM-ViewTestCase-descriptionCompare');
            this.compareType = element.find('.eaTM-ViewTestCase-typeCompare');
            this.compareExecutionType = element.find('.eaTM-ViewTestCase-executionTypeCompare');
            this.compareAutomationCandidate = element.find('.eaTM-ViewTestCase-automationCandidateCompare');
            this.compareComponent = element.find('.eaTM-ViewTestCase-componentCompare');
            this.compareFeature = element.find('.eaTM-ViewTestCase-featureCompare');
            this.comparePriority = element.find('.eaTM-ViewTestCase-priorityCompare');
            this.compareGroup = element.find('.eaTM-ViewTestCase-groupCompare');
            this.compareContext = element.find('.eaTM-ViewTestCase-contextCompare');
            this.comparePreCondition = element.find('.eaTM-ViewTestCase-preConditionCompare');
            this.compareIntrusive = element.find('.eaTM-ViewTestCase-intrusiveCompare');
            this.compareIntrusiveComment = element.find('.eaTM-ViewTestCase-intrusiveCommentCompare');
            this.compareSuite = element.find('.eaTM-ViewTestCase-suiteCompare');
            this.compareTeam = element.find('.eaTM-ViewTestCase-teamCompare');
            this.compareRequirements = element.find('.eaTM-ViewTestCase-requirementsCompare');

        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        // jira fields
        getRequirementsBlock: function () {
            return this.requirementsBlock;
        },

        // updateInfo fields
        getCreatedAt: function () {
            return this.createdAt;
        },
        getUpdatedAt: function () {
            return this.updatedAt;
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
        getType: function () {
            return this.type;
        },
        getExecutionType: function () {
            return this.executionType;
        },
        getAutomationCandidate: function () {
            return this.automationCandidate;
        },
        getAutomationCandidateBlock: function () {
            return this.automationCandidateBlock;
        },
        getTestCaseStatus: function () {
            return this.testCaseStatus;
        },
        getTestCaseReviewer: function () {
            return this.testCaseReviewer;
        },
        getComponent: function () {
            return this.component;
        },
        getPriority: function () {
            return this.priority;
        },
        getGroup: function () {
            return this.group;
        },
        getContext: function () {
            return this.context;
        },
        getPreCondition: function () {
            return this.preCondition;
        },
        getRevisionsBlock: function () {
            return this.revisionsBlock;
        },
        getIntrusive: function () {
            return this.intrusive;
        },
        getIntrusiveComment: function () {
            return this.intrusiveComment;
        },

        getSuite: function () {
            return this.suite;
        },

        getTeam: function () {
            return this.team;
        },

        getLastAuthor: function () {
            return this.lastAuthor;
        },

        getVersionTitle: function () {
            return this.versionTitle;
        },

        getFeature: function () {
            return this.feature;
        },

        hideAutomationCandidateBlock: function () {
            this.automationCandidateBlock.setModifier('hidden');
        },

        showAutomationCandidateBlock: function () {
            this.automationCandidateBlock.removeModifier('hidden');
        },

        getTestCaseStatusCompare: function () {
            return this.compareTestCaseStatus;
        },

        getTitleCompare: function () {
            return this.compareTitle;
        },

        getDescriptionCompare: function () {
            return this.compareDescription;
        },

        getTypeCompare: function () {
            return this.compareType;
        },

        getExecutionTypeCompare: function () {
            return this.compareExecutionType;
        },

        getAutomationCandidateCompare: function () {
            return this.compareAutomationCandidate;
        },

        getComponentCompare: function () {
            return this.compareComponent;
        },

        getPriorityCompare: function () {
            return this.comparePriority;
        },

        getGroupCompare: function () {
            return this.compareGroup;
        },

        getContextCompare: function () {
            return this.compareContext;
        },

        getPreConditionCompare: function () {
            return this.comparePreCondition;
        },

        getIntrusiveCompare: function () {
            return this.compareIntrusive;
        },

        getIntrusiveCommentCompare: function () {
            return this.compareIntrusiveComment;
        },

        getFeatureCompare: function () {
            return this.compareFeature;
        },

        getSuiteCompare: function () {
            return this.compareSuite;
        },

        getTeamCompare: function () {
            return this.compareTeam;
        },

        getRequirementsCompare: function () {
            return this.compareRequirements;
        }

    });

});
