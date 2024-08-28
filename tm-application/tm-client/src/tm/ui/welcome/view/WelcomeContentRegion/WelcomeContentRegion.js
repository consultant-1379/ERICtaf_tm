define([
    'jscore/core',
    './WelcomeContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    './contentBox/ContentBox',
    '../../../../common/HelpDetails'
], function (core, View, Constants, Animation, Navigation, ContentBox, HelpDetails) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */
        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;

            this.firstContentList = [
                {
                    name: 'Test Case',
                    tooltip: 'Click for more information on Test Cases',
                    dialogContent: {
                        title: 'What is a Test Case?',
                        content: HelpDetails.data.TEST_CASE
                    }
                },
                {
                    name: 'Test Campaign',
                    tooltip: 'Click for more information on Test Campaigns',
                    dialogContent: {
                        title: 'What is a Test Campaign?',
                        content: HelpDetails.data.TEST_PLAN
                    }
                },
                {
                    name: 'Test Campaign Group',
                    tooltip: 'Click for more information on Test Campaign Group',
                    dialogContent: {
                        title: 'What is a Test Campaign Group?',
                        content: HelpDetails.data.TEST_CAMPAIGN_GROUPS
                    }
                },
                {
                    name: 'Test Specification (GAT)',
                    tooltip: 'Click for more information on Test Specification (GAT)',
                    dialogContent: {
                        title: 'What is a Test Specification?',
                        content: HelpDetails.data.TEST_SPECIFICATION
                    }},
                {
                    name: 'Requirement',
                    tooltip: 'Click for more information on Requirements',
                    dialogContent: {
                        title: 'What is a Requirement?',
                        content: HelpDetails.data.REQUIREMENT
                    }}
            ];

            this.secondContentList = [
                {
                    name: 'Select a Test Case',
                    link: '#' + Navigation.getTestCasesListUrl(),
                    tooltip: 'Click to select a Test Case'
                },
                {
                    name: 'Create a Test Case',
                    link: '#' + Navigation.getTestCaseCreateUrl(),
                    tooltip: 'Click to create a Test Case'
                },
                {
                    name: 'Create a Test Campaign',
                    link: '#' + Navigation.getTestPlanCreateUrl(),
                    tooltip: 'Click to create a Test Campaign'
                },
                {
                    name: 'Select a Test Campaign',
                    link: '#' + Navigation.getTestPlansListUrl(),
                    tooltip: 'Click to execute a Test Campaign'
                },
                {
                    name: 'Select a Test Campaign Group',
                    link: '#' + Navigation.getTestCampaignGroupsUrl(),
                    tooltip: 'Click to view Test Campaign Groups'
                }
            ];

            this.thirdContentList = [
                {
                    name: 'Latest Functionality added',
                    link: Navigation.getChangeLogUrl(),
                    tooltip: 'Click to see the latest updates to TMS'
                }
            ];
        },

        onViewReady: function () {
            this.view.afterRender();

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_WELCOME, this.refreshCurrentPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_WELCOME);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_WELCOME, this.refreshCurrentPage.bind(this));

            this.view.getTitle().setText('Welcome to TAF TMS');
            this.view.getText().setText('What would you like to do?');
            createContentBoxes.call(this);
        },

        refreshCurrentPage: function () {
        }

    });

    function createContentBoxes () {
        this.firstContentBox = new ContentBox({
            title: 'What is...',
            content: this.firstContentList
        });
        this.firstContentBox.attachTo(this.view.getContent());

        this.secondContentBox = new ContentBox({
            title: 'Quick Links...',
            content: this.secondContentList
        });
        this.secondContentBox.attachTo(this.view.getContent());

        this.thirdContentBox = new ContentBox({
            title: 'What\'s new...',
            content: this.thirdContentList
        });
        this.thirdContentBox.attachTo(this.view.getContent());
    }
});
