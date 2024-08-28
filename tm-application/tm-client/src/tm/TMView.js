define([
    'jscore/core',
    'text!./_tm.html',
    'styles!./_tm.less',
    'styles!./cssBlockers/_assetsPatch.less',
    'styles!./cssBlockers/_multiSelect.less',
    'styles!./cssBlockers/_fixes.less',
    'styles!./cssBlockers/widgets/table/_table.less'
], function (core, template, styles, assetsPatch, multiSelect, fixesStyles, tableStyles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            this.pageHeading = element.find('.ebLayout-AppHeading');
            this.pageTitle = element.find('.eaTM-AppHeading-Text h1');
            this.navigation = element.find('.ebLayout-Navigation');
            this.notificationsBlock = element.find('.eaTM-Notifications');
            this.contentBlock = element.find('.eaTM-Content');
            this.quickActionBar = element.find('.ebQuickActionBar');
            this.version = element.find('.eaTM-Version');
            this.buildDate = element.find('.eaTM-BuildDate');
            this.subNavigationBlock = element.find('.eaTM-SubNavigation');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return assetsPatch + multiSelect + fixesStyles + tableStyles + styles;
        },

        getPageHeading: function () {
            return this.pageHeading;
        },

        getPageTitle: function () {
            return this.pageTitle;
        },

        getQuickActionBar: function () {
            return this.quickActionBar;
        },

        getNavigation: function () {
            return this.navigation;
        },

        getNotificationsBlock: function () {
            return this.notificationsBlock;
        },

        getContentBlock: function () {
            return this.contentBlock;
        },

        getVersion: function () {
            return this.version;
        },

        getBuildDate: function () {
            return this.buildDate;
        },

        getSubNavigationBlock: function () {
            return this.subNavigationBlock;
        },

        hideHeader: function () {
            this.pageHeading.setAttribute('style', 'display: none;');
            this.quickActionBar.setAttribute('style', 'display: none;');
            this.notificationsBlock.setAttribute('style', 'display: none;');
        },

        showHeader: function () {
            this.pageHeading.setAttribute('style', '');
            this.quickActionBar.setAttribute('style', '');
            this.notificationsBlock.setAttribute('style', '');
        }

    });

});
