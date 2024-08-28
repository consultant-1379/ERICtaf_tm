/*global define*/
define([
    'jscore/core',
    'text!./Infotip.html',
    'styles!./Infotip.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.text = element.find('.eaTM-Infotip-text');
            this.arrow = element.find('.eaTM-Infotip-arrow');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getText: function () {
            return this.text;
        },

        getArrow: function () {
            return this.arrow;
        }
    });

});
