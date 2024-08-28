define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseSearchBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink',
    '../../../../ext/stringUtils',
    '../../../../common/widgets/ContextSearchMenu/ContextSearchMenu',
    '../../../../common/widgets/Menu/Menu'
], function (ActionBarRegion,  View, Constants, Navigation, ContextFilter, ActionIcon, ActionLink,
             stringUtils, ContextSearchMenu, Menu) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.isSearchShown = false;
        },

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;

            this.pages = this.options.pages;

            this.menu = new Menu({
                items: [
                    {
                        name: 'Create Test Case',
                        icon: 'plus',
                        action: function () {
                            Navigation.navigateTo(Navigation.getTestCaseCreateUrl());
                        }
                    },
                    {
                        name: 'Test Campaigns',
                        icon: 'addToFolder',
                        action: function () {
                            Navigation.navigateTo(Navigation.getTestPlansListUrl());
                        }
                    },
                    {
                        name: 'Test Campaign Groups',
                        icon: 'topology',
                        action: function () {
                            Navigation.navigateTo(Navigation.getTestCampaignGroupsUrl());
                        }
                    },
                    {
                        name: 'Requirements',
                        icon: 'topology',
                        action: function () {
                            Navigation.navigateTo(Navigation.getRequirementsTreeUrl());
                        }
                    },
                    {
                        name: 'Import Test Case',
                        icon: 'import',
                        action: function () {
                            Navigation.navigateTo(Navigation.getImportUrl());
                        }
                    }
                ]
            });

            this.menu.attachTo(this.view.getContentMenuHolder());

            this.view.getSearchButton().addEventHandler('click', this.onSearchButtonClick, this);
            this.view.getHideSearchButton().addEventHandler('click', this.onHideSearchButtonClick, this);
            this.view.getMoreButton().addEventHandler('click', this.onMoreButtonClick, this);
            this.view.getSearchInput().addEventHandler('keydown', this.onSearchInputKeyDown, this);
            this.view.getSearchInput().addEventHandler('input', updateSearchClearIcon, this);

            this.searchClearIcon = new ActionIcon({
                iconKey: 'close',
                interactive: true,
                hide: true
            });
            this.searchClearIcon.attachTo(this.view.getSearchClearIcon());
            updateSearchClearIcon.call(this);
            this.view.getSearchClearIcon().addEventHandler('click', this.onClearSearch, this);

            this.ContextSearchMenu = new ContextSearchMenu({
                eventBus: this.eventBus
            });
            this.ContextSearchMenu.attachTo(this.view.getContentSearchMenuHolder());

            this.eventBus.subscribe(Constants.events.SHOW_BAR_FOR_ADVANCED_SEARCH, this.onMoreButtonClick, this);
            this.eventBus.subscribe(Constants.events.HIDE_BAR_FOR_ADVANCED_SEARCH, this.onHideSearchButtonClick, this);
            this.eventBus.subscribe(Constants.events.CLEAR_SEARCH_INPUT, this.onClearSearchInput, this);
            this.eventBus.subscribe(Constants.events.REPLACE_SEARCH_INPUT, this.onReplaceSearchInput, this);
        },

        show: function () {
            ActionBarRegion.prototype.show.call(this);
        },

        onReplaceSearchInput: function (searchValue) {
            var searchInput = this.view.getSearchInput();
            searchInput.setValue(searchValue);
            updateSearchClearIcon.call(this);
        },

        onClearSearchInput: function () {
            var searchInput = this.view.getSearchInput();
            searchInput.setValue('');
            updateSearchClearIcon.call(this);
        },

        onClearSearch: function () {
            this.onClearSearchInput();
            this.eventBus.publish(Constants.events.SEARCH_BY_VALUE, '');
        },

        onSearchInputKeyDown: function (e) {
            var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
            if (keyCode === 13) { // Enter
                this.view.getSearchButton().trigger('click');
                e.preventDefault();
            }
        },

        onAuthenticationRequired: function () {
            this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
        },

        onSearchButtonClick: function (e) {
            e.preventDefault();

            var searchInput = this.view.getSearchInput();

            var searchValue = stringUtils.trim(searchInput.getValue());
            searchInput.setValue(searchValue);

            this.eventBus.publish(Constants.events.SEARCH_BY_VALUE, searchValue);
        },

        onHideSearchButtonClick: function () {
            if (!this.isSearchShown) {
                return;
            }

            hideShowSearchElements.call(this, false);

            this.eventBus.publish(Constants.events.HIDE_SEARCH);
            this.isSearchShown = false;
        },

        onMoreButtonClick: function () {
            if (this.isSearchShown) {
                return;
            }

            hideShowSearchElements.call(this, true);

            this.eventBus.publish(Constants.events.SHOW_SEARCH);
            this.isSearchShown = true;
        }
    });

    /*************** PRIVATE FUNCTIONS ******************/

    function hideShowSearchElements (hideQuickSearch) {
        var quickSearchStyle = hideQuickSearch ? 'none' : 'inline-block',
            hideSearchStyle = !hideQuickSearch ? 'none' : 'inline-block';

        this.view.getQuickSearch().setStyle('display', quickSearchStyle);
        this.view.getMoreButton().setStyle('display', quickSearchStyle);

        this.view.getHideSearchButton().setStyle('display', hideSearchStyle);
    }

    function updateSearchClearIcon () {
        var searchInput = this.view.getSearchInput();
        var isEmpty = searchInput.getValue().length === 0;
        this.searchClearIcon.setHidden(isEmpty);
    }

});
