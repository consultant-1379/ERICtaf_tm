define([
    'jscore/core',
    './DashboardRegionView',
    'i18n!statistics/dictionary.json',
    'layouts/Dashboard',
    '../common/widgets/CountChart/CountChart',
    '../common/widgets/AdvancedChart',
    '../models/TestCaseCollection',
    '../models/UserCollection',
    '../common/Constants'
], function (core, View, dictionary, Dashboard, CountChart, AdvancedChart, TestCaseCollection,
             UserCollection, Constants) {

    return core.Region.extend({

        view: function () {
            return new View({i18n: dictionary});
        },

        onStart: function () {
            this.eventBus = this.getContext().eventBus;

            this.dashboard = new Dashboard({
                context: this.getContext(),
                items: [
                    [{
                        header: 'Test Management System - Test Cases per Product',
                        type: "AdvancedWidget",
                        config: {
                            type: 'verticalBar',
                            name: 'No. of Test Cases',
                            eventName: Constants.events.NO_OF_TEST_CASES,
                            height: '40rem'
                        },
                        settings: false,
                        maximizable: true,
                        maximize: false
                    },
                        {
                            header: 'Test Management System - Total Number of Test Cases',
                            type: "CountChartWidget",
                            config: {
                                name: 'No. of Test Cases',
                                eventName: Constants.events.NO_OF_TEST_CASES,
                                height: '40rem'
                            },
                            settings: false,
                            maximizable: true,
                            maximize: false
                        }],

                    [{
                        header: 'Test Management System - Number of User\'s Logged on per Month',
                        type: "AdvancedWidget",
                        config: {
                            type: 'verticalBar',
                            name: 'No. of users logged on per month',
                            eventName: Constants.events.NO_OF_PEOPLE,
                            height: '40rem'
                        },
                        maximizable: true,
                        settings: false
                    },
                        {
                            header: 'Test Management System - Percentage of Test Cases',
                            type: "AdvancedWidget",
                            config: {
                                type: 'pie',
                                name: 'No. of Test Cases',
                                eventName: Constants.events.NO_OF_TEST_CASES,
                                height: '40rem'
                            },
                            settings: false,
                            maximizable: true,
                            maximize: false
                        }]

                ],
                layout: 'two-columns',
                references: [AdvancedChart, CountChart]
            });

            this.dashboard.attachTo(this.getElement());

            getTestCaseData.call(this);
            getUserData.call(this);
        }
    });

    function getTestCaseData () {
        var testCaseCollection = new TestCaseCollection();
            testCaseCollection.fetch({
                success : function (items) {
                    var data = items.toJSON();
                    this.eventBus.publish(Constants.events.NO_OF_TEST_CASES, data);
                }.bind(this)
            });
    }

    function getUserData () {
        var userCollection = new UserCollection();
            userCollection.fetch({
                success : function (items) {
                    var data = items.toJSON();
                    this.eventBus.publish(Constants.events.NO_OF_PEOPLE, data);
                }.bind(this)
            });
        }

});
