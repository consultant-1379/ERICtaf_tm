/*global define*/
define([
    'jscore/core',
    'text!./ResultWidget.html',
    'styles!./ResultWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.passIcon = element.find('.eaTM-Result-pass');
            this.failIcon = element.find('.eaTM-Result-fail');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getPassIcon: function () {
            return this.passIcon;
        },

        getFailIcon: function () {
            return this.failIcon;
        },

        selectPassIcon: function () {
            this.passIcon.setProperty('selected', true);
            this.passIcon.setModifier('selected', '', 'eaTM-Result-pass');
        },

        deselectPassIcon: function () {
            this.passIcon.setProperty('selected', false);
            this.passIcon.removeModifier('selected', '', 'eaTM-Result-pass');
        },

        selectFailIcon: function () {
            this.failIcon.setProperty('selected', true);
            this.failIcon.setModifier('selected', '', 'eaTM-Result-fail');
        },

        deselectFailIcon: function () {
            this.failIcon.setProperty('selected', false);
            this.failIcon.removeModifier('selected', '', 'eaTM-Result-fail');
        }
    });

});
