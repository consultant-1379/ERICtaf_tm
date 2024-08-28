/*global define*/
define([
    'jscore/core',
    'text!./TestCaseSearchResultsWidget.html',
    'styles!./TestCaseSearchResultsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.resultsCount = element.find('.eaTM-Results-count');
            this.exportLink = element.find('.eaTM-Results-exportLink');
            this.contentBlock = element.find('.eaTM-Results-content');
            this.addToTestPlanLinkHolder = element.find('.eaTM-Results-addToTestPlanLinkHolder');
            this.saveSearchLinkHolder = element.find('.eaTM-Results-saveSearchLinkHolder');
            this.saveSearchMenuHolder = element.find('.eaTM-Results-saveSearchMenuHolder');
            this.exportLinkHolder = element.find('.eaTM-Results-exportLinkHolder');
            this.totalCount = element.find('.eaTM-Results-totalCount');
            this.exportLink = element.find('.eaTM-Results-exportLink');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getExportLink: function () {
            return this.exportLink;
        },

        getResultsCount: function () {
            return this.resultsCount;
        },

        getExportLinkHolder: function () {
            return this.exportLinkHolder;
        },

        getContentBlock: function () {
            return this.contentBlock;
        },

        getAddToTestPlanLinkHolder: function () {
            return this.addToTestPlanLinkHolder;
        },

        getSaveSearchLinkHolder: function () {
            return this.saveSearchLinkHolder;
        },

        getSaveSearchMenuHolder: function () {
            return this.saveSearchMenuHolder;
        },

        getTotalCount: function () {
            return this.totalCount;
        }

    });

});
