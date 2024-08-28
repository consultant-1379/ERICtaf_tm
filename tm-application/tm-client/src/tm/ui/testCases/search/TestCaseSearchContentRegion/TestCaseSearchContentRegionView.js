/*global define*/
define([
    'jscore/core',
    'text!./TestCaseSearchContentRegion.html',
    'styles!./TestCaseSearchContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.slidingPanels = element.find('.eaTM-ContentRegion-slidingPanels');
            this.searchPanel = element.find('.eaTM-ContentRegion-searchPanel');
            this.searchContent = element.find('.eaTM-ContentRegion-searchContent');
            this.resultsPanel = element.find('.eaTM-ContentRegion-resultsPanel');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        showSearch: function () {
            this.slidingPanels.setModifier('show', 'search');
        },

        hideSearch: function () {
            this.slidingPanels.removeModifier('show');
        },

        getSlidingPanels: function () {
            return this.slidingPanels;
        },

        getSearchPanel: function () {
            return this.searchPanel;
        },

        getSearchContent: function () {
            return this.searchContent;
        },

        getResultsPanel: function () {
            return this.resultsPanel;
        }

    });

});
