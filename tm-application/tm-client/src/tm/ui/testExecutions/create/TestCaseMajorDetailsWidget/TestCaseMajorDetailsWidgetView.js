/*global define*/
define([
    'jscore/core',
    'text!./TestCaseMajorDetailsWidget.html',
    'styles!./TestCaseMajorDetailsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            // jira fields
            this.requirementsBlock = element.find('.eaTM-TestCaseMajorDetails-requirements');

            // testCase fields
            this.testCaseId = element.find('.eaTM-TestCaseMajorDetails-testCaseId');
            this.title = element.find('.eaTM-TestCaseMajorDetails-title');
            this.description = element.find('.eaTM-TestCaseMajorDetails-description');
            this.preCondition = element.find('.eaTM-TestCaseMajorDetails-preCondition');
            this.type = element.find('.eaTM-TestCaseMajorDetails-type');
            this.group = element.find('.eaTM-TestCaseMajorDetails-group');
            this.context = element.find('.eaTM-TestCaseMajorDetails-context');
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
        getType: function () {
            return this.type;
        },
        getPriority: function () {
            return this.priority;
        },
        getGroup: function () {
            return this.group;
        },
        getContext: function () {
            return this.context;
        }

    });

});
