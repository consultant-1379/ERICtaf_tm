define([
    'jscore/core',
    'text!./TestCaseSearchWidget.html',
    'styles!./TestCaseSearchWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.addButton = element.find('.eaTM-TestCasesSearch-addButton');
            this.searchTable = element.find('.eaTM-TestCasesSearch-searchTable');
            this.searchOptions = element.find('.eaTM-TestCasesSearch-searchOptions');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getAddButton: function () {
            return this.addButton;
        },

        getSearchTable: function () {
            return this.searchTable;
        },

        getSearchOptions: function () {
            return this.searchOptions;
        }

    });

});
