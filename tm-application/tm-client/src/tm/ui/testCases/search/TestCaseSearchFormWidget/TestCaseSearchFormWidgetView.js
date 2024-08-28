/*global define*/
define([
    'jscore/core',
    'text!./TestCaseSearchFormWidget.html',
    'styles!./TestCaseSearchFormWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.headerBlock = element.find('.ebLayout-SectionHeading');
            this.contentBlock = element.find('.eaTM-Search-content');
            this.buttonBlock = element.find('.eaTM-Search-buttonBlock');
            this.searchButton = element.find('.eaTM-Search-button');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getHeaderBlock: function () {
            return this.headerBlock;
        },

        getContentBlock: function () {
            return this.contentBlock;
        },

        getButtonBlock: function () {
            return this.buttonBlock;
        },

        getSearchButton: function () {
            return this.searchButton;
        }

    });

});
