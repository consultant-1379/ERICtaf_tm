/*global define*/
define([
    'jscore/core',
    'text!./ContentBoxItem.html',
    'styles!./ContentBoxItem.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.icon = element.find('.eaTM-ContentBoxItem-icon');
            this.text = element.find('.eaTM-ContentBoxItem-text');
            this.infotip = element.find('.eaTM-ContentBoxItem-infotip');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getIcon: function () {
            return this.icon;
        },

        getText: function () {
            return this.text;
        },

        getInfotip: function () {
            return this.infotip;
        }
    });

});
