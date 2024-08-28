/*global define*/
define([
    'jscore/core',
    'text!./RequirementsContentRegion.html',
    'styles!./RequirementsContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.treeHolder = element.find('.eaTM-RequirementsRegion-treeBlock');
            this.detailsBlock = element.find('.eaTM-RequirementsRegion-detailsBlock');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTreeHolder: function () {
            return this.treeHolder;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        }

    });

});
