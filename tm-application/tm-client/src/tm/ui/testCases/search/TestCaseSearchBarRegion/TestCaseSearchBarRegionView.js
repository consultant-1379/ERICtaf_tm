define([
    'jscore/core',
    'text!./TestCaseSearchBarRegion.html',
    'styles!./TestCaseSearchBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.quickSearch = element.find('.eaTM-DefaultBar-quickSearch');
            this.searchInput = element.find('.eaTM-DefaultBar-searchInput');
            this.searchClearIcon = element.find('.eaTM-DefaultBar-searchClearIcon');
            this.searchButton = element.find('.eaTM-DefaultBar-searchButton');
            this.hideSearchButton = element.find('.eaTM-DefaultBar-hideSearchButton');
            this.moreButton = element.find('.eaTM-DefaultBar-moreButton');
            this.contextSearchMenuHolder = element.find('.eaTM-DefaultBar-contextSearchMenuHolder');
            this.contextMenuHolder = element.find('.eaTM-DefaultBar-contextMenuHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getQuickSearch: function () {
            return this.quickSearch;
        },

        getSearchInput: function () {
            return this.searchInput;
        },

        getSearchClearIcon: function () {
            return this.searchClearIcon;
        },

        getSearchButton: function () {
            return this.searchButton;
        },

        getHideSearchButton: function () {
            return this.hideSearchButton;
        },

        getMoreButton: function () {
            return this.moreButton;
        },

        getContentSearchMenuHolder: function () {
            return this.contextSearchMenuHolder;
        },

        getContentMenuHolder: function () {
            return this.contextMenuHolder;
        }
    });

});
