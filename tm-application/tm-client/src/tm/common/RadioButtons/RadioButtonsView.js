define([
    'jscore/core',
    'text!./RadioButtons.html',
    'styles!./RadioButtons.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.options = this.getElement().find('.eaTM-RadioButtons-options');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        getRadioOptionsDiv: function () {
            return this.options;
        }
    });
});
