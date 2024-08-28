define([
    'jscore/core',
    'jscore/ext/net',
    './TmsUserButtonView',
    'widgets/Tooltip',
    './options/inboxoption/InboxOption',
    './options/adminOption/AdminOption',
    './options/reviewoption/ReviewOption',
    './options/statisticsoption/StatisticsOption',
    './options/logoutoption/LogoutOption'
], function (core, net, View, Tooltip, InboxOption, AdminOption, ReviewOption, StatisticsOption, LogoutOption) {
    'use strict';

    return core.App.extend({
        /*jshint validthis:true*/

        View: View,

        onStart: function () {
            this.displayUserName();
            this.isDisplayed = false;
            this.handlerEnabled = true;

            this.inboxOption = new InboxOption({
                inboxUrl: this.options.properties.inboxUrl
            });

            this.reviewOption = new ReviewOption({
                reviewUrl: this.options.properties.reviewUrl
            });

            this.statisticsOption = new StatisticsOption({
                statisticsUrl: this.options.properties.statisticsUrl
            });

            this.logoutOption = new LogoutOption({
                logoutUrl: this.options.properties.logoutUrl,
                restLogoutUrl: this.options.properties.restLogoutUrl
            });

            this.addListButtons();
            //Click event to display the User Dropdown when element is clicked
            this.view.getElement().addEventHandler('click', function () {
                this.handlerEnabled = false;
                this.tooltipWidget.disable();
                if (!this.isDisplayed) {
                    this.view.getListItems().setModifier('visible');
                    this.isDisplayed = true;
                } else {
                    this.view.getListItems().removeModifier('visible');
                    this.isDisplayed = false;
                }
                this.tooltipWidget.enable();
            }.bind(this));

            //Click event hides the User dropdown when it is displayed
            core.Window.addEventHandler("click", function () {
                if (this.handlerEnabled) {
                    if (this.isDisplayed) {
                        this.view.getListItems().removeModifier('visible');
                        this.isDisplayed = false;
                    }
                }
                this.handlerEnabled = true;
            }.bind(this));

            this.view.getElement().addEventHandler('hover', this.displayTooltip(), this);
        },

        addListButtons: function () {
            this.inboxOption.attachTo(this.view.getInboxOption());
            this.reviewOption.attachTo(this.view.getReviewOption());
            this.statisticsOption.attachTo(this.view.getStatisticsOption());
            this.logoutOption.attachTo(this.view.getLogoutOption());
        },

        displayUserName: function () {
            if (this.options.properties.restUserUrl) {
                net.ajax({
                    url: this.options.properties.restUserUrl,
                    type: 'GET',
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        setUserDetails.call(this, data);
                        adminOptions.call(this, data);
                    }.bind(this)
                });
            }
        },

        displayTooltip: function () {
            this.tooltipWidget = new Tooltip({
                parent: this.view.getUserButton(),
                timeout: 5000
            });

            this.tooltipWidget.attachTo(this.view.getElement());
        }
    });

    function setUserDetails(data) {
        if (data.userName) {
            if (data.userName.length < 16) {
                this.view.getUserName().setText(data.userName);
            } else {
                this.view.getUserName().setText(data.userName.substring(0, 15) + '...');
            }
            this.tooltipWidget.setContentText(data.userName);
        }
    }

    function adminOptions(data) {
        if (data.administrator) {
            this.adminOption = new AdminOption({
                adminUrl: this.options.properties.adminUrl
            });
            this.adminOption.attachTo(this.view.getAdminOption());
        }
    }
});
