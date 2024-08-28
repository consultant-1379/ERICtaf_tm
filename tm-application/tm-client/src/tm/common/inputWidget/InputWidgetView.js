/*global define*/
define([
    'jscore/core',
    'text!./InputWidget.html',
    'styles!./InputWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.element = this.getElement();
            this.input = this.element.find('.eaTM-Input-field');
        },

        getTemplate: function () {
            return template;
        },

        getInput: function () {
            return this.input;
        },

        getStyle: function () {
            return styles;
        }

    });

});
