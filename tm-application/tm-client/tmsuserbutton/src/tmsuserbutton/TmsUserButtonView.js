define([
    'jscore/core',
    'text!./TmsUserButton.html',
    'styles!./TmsUserButton.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.userButton = element.find('.eaTmsUserButton-button');
            this.listItems = element.find('.eaTmsUserButton-list');
            this.userName = element.find('.eaTmsUserButton-span');
            this.inboxOption = element.find('.eaTmsUserButton-inbox');
            this.reviewOption = element.find('.eaTmsUserButton-review');
            this.adminOption = element.find('.eaTmsUserButton-admin');
            this.statisticsOption = element.find('.eaTmsUserButton-statistics');
            this.logoutOption = element.find('.eaTmsUserButton-logout');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getUserButton: function () {
            return this.userButton;
        },

        getListItems: function () {
            return this.listItems;
        },

        getUserName: function () {
            return this.userName;
        },

        getInboxOption: function () {
            return this.inboxOption;
        },

        getLogoutOption: function () {
            return this.logoutOption;
        },

        getAdminOption: function () {
            return this.adminOption;
        },

        getReviewOption: function () {
            return this.reviewOption;
        },

        getStatisticsOption: function () {
            return this.statisticsOption;
        }

    });

});
