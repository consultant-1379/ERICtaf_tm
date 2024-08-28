/*global define*/
define([
    'jscore/core',
    'text!./AdminContentRegion.html',
    'styles!./AdminContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-ViewAdminRegion-details');

        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        }

    });

});
