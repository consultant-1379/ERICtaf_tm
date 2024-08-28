/*global define*/
define([
    'jscore/core',
    'text!./TestCaseFormStepsWidget.html',
    'styles!./TestCaseFormStepsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.testStepsBlock = element.find('.eaTM-TestStepsForm-block');
            this.addLink = element.find('.eaTM-TestStepsForm-addLink');
            this.expandCollapseButton = element.find('.eaTM-TestStepsForm-ExpandCollapse-button');
            this.expandCollapseIcon = element.find('.eaTM-TestStepsForm-ExpandCollapse-icon');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTestStepsBlock: function () {
            return this.testStepsBlock;
        },

        getAddLink: function () {
            return this.addLink;
        },

        getExpandCollapseButton: function () {
            return this.expandCollapseButton;
        },

        getExpandCollapseIcon: function () {
            return this.expandCollapseIcon;
        }

    });

});
