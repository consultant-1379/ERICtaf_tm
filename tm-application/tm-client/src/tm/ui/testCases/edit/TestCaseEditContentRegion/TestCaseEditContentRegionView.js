/*global define*/
define([
    'jscore/core',
    'text!./TestCaseEditContentRegion.html',
    'styles!./TestCaseEditContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-TestCaseFormRegion-details');
            this.testStepsBlock = element.find('.eaTM-TestCaseFormRegion-testSteps');
            this.filesHolder = element.find('.eaTM-TestCaseFormRegion-filesHolder');
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
        }

    });

});
