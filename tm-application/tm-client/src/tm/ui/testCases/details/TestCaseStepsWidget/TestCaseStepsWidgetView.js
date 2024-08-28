/*global define*/
define([
    'jscore/core',
    'text!./TestCaseStepsWidget.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.testStepsBlock = element.find('.eaTM-ViewTestSteps-block');
        },

        getTemplate: function () {
            return template;
        },

        getTestStepsBlock: function () {
            return this.testStepsBlock;
        }

    });

});
