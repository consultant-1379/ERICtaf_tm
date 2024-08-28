/*global define*/
define([
    'jscore/core',
    'text!./CreateTestExecutionContentRegion.html',
    'styles!./CreateTestExecutionContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-CreateTestExecutionContentRegion-details');
            this.testCaseDetails = element.find('.eaTM-CreateTestExecutionContentRegion-testCaseDetails');
            this.testCaseSteps = element.find('.eaTM-CreateTestExecutionContentRegion-testCaseSteps');
            this.testResult = element.find('.eaTM-CreateTestExecutionContentRegion-executionResult');
            this.defectButtonBlock = element.find('.eaTM-CreateTestExecutionContentRegion-executionDefect');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        },

        getTestCaseDetails: function () {
            return this.testCaseDetails;
        },

        getTestCaseSteps: function () {
            return this.testCaseSteps;
        },

        getTestResult: function () {
            return this.testResult;
        },

        getDefectButtonBlock: function () {
            return this.defectButtonBlock;
        },

        showDefectButton: function () {
            this.defectButtonBlock.removeModifier('hidden');
        },

        hideDefectButton: function () {
            this.defectButtonBlock.setModifier('hidden');
        }

    });

});
