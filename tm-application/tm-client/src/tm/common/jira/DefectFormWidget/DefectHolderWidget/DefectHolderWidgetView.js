/*global define*/
define([
    'jscore/core',
    'text!./DefectHolderWidget.html',
    'styles!./DefectHolderWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.element = this.getElement();
            this.input = this.element.find('.eaTM-DefectHolder-input');
            this.span = this.element.find('span');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getInput: function () {
            return this.input;
        },

        getLabelText: function () {
            return this.span;
        }

    });

});
