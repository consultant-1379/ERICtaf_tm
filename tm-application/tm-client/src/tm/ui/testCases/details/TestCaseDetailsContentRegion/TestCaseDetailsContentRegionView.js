/*global define*/
define([
    'jscore/core',
    'text!./TestCaseDetailsContentRegion.html',
    'styles!./TestCaseDetailsContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-ViewTestCaseRegion-details');
            this.testStepsBlock = element.find('.eaTM-ViewTestCaseRegion-testSteps');
            this.filesHolder = element.find('.eaTM-ViewTestCaseRegion-filesHolder');
            this.compareCheckBox = element.find('.eaTM-ViewTestCaseRegion-compareCheckBox');
            this.difference = element.find('.eaTM-ViewTestCaseRegion-difference');
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

        getTestStepsBlock: function () {
            return this.testStepsBlock;
        },

        getFilesHolder: function () {
            return this.filesHolder;
        },

        getCompareCheckBox: function () {
            return this.compareCheckBox;
        },

        getDifferenceDiv: function () {
            return this.difference;
        }

    });

});
