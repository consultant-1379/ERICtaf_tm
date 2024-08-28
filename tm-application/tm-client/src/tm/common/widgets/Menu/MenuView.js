/*global define*/
define([
    'jscore/core',
    'text!./Menu.html',
    'styles!./_menu.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        getButton: function () {
            return this.getElement().find('.eaTM-Menu-button');
        }

    });

});
