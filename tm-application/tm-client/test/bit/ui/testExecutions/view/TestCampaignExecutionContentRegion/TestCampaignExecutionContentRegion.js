/*global define, expect, describe, it, beforeEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/ui/testExecutions/view/TestCampaignExecutionContentRegion/TestCampaignExecutionContentRegion'
], function (core, TestPlanExecutionContentRegion) {
    'use strict';

    describe('tm/ui/testExecutions/view/TestCampaignExecutionContentRegion/TestCampaignExecutionContentRegion', function () {

        describe('TestCampaignExecutionContentRegion', function () {
            var eventBus;
            var testPlanExecutionContentRegion;
            var element = core.Element.parse('<div class=="eatM-rTest"></div>');
            var references = {
                getById: function () {
                }
            };
            beforeEach(function () {
                eventBus = new core.EventBus();
                testPlanExecutionContentRegion = new TestPlanExecutionContentRegion({
                    context: {eventBus: eventBus},
                    references: references
                });

                testPlanExecutionContentRegion.attach(element);
                testPlanExecutionContentRegion.init();
                testPlanExecutionContentRegion.onViewReady();

            });
            it('testCampaignExecutionContentRegion is defined', function () {
                expect(testPlanExecutionContentRegion).not.to.be.undefined;
            });
            it('Hide and unhide columns in Test Case Table', function () {

                var tableData = [{
                    id: 19,
                    testCase: {
                        id: 1,
                        testCaseId: '66a9bbfe-08b7-4267-8c25-8d2c6d3917a8',
                        title: 'Test',
                        comment: 'Test Execution Comment 10.1_1'
                    },
                    user: {
                        userName: 'someUserCouldBeAnyone'
                    },
                    result: {id: '1', title: 'Not started', sortOrder: 4},
                    defects: []
                }, {
                    id: 20,
                    testCase: {
                        id: 2,
                        testCaseId: 'a62366f0-6703-4e38-944c-b1a34432dcdf',
                        title: 'sdfsfsdfsdfs',
                        comment: 'Test Execution Comment 10.2_1'
                    },
                    user: {
                        userName: 'someUserCouldBeAnyone'
                    },
                    result: {id: '4', title: 'Fail', sortOrder: 6},
                    defects: []
                }, {
                    id: 21,
                    testCase: {
                        id: 3,
                        testCaseId: '5b18c890-ccaa-4d93-a282-e1b644b842d7',
                        title: 'Test',
                        comment: 'Test Execution Comment 10.3_1'
                    },
                    user: {
                        userName: 'someUserCouldBeAnyone'
                    },
                    result: {id: '3', title: 'Passed with exception', sortOrder: 2},
                    defects: []
                }, {
                    id: 22,
                    testCase: {
                        id: 4,
                        testCaseId: '7a0793c1-143d-4e15-8063-78e0e78280a4',
                        title: 'My Test Case',
                        comment: 'Test Execution Comment 10.4_1'
                    },
                    user: {
                        userName: 'someUserCouldBeAnyone'
                    },
                    result: {id: '5', title: 'Work in progress', sortOrder: 3},
                    defects: []
                }, {
                    id: 23,
                    testCase: {
                        id: 5,
                        testCaseId: 'ddc198c3-d228-49e0-83b8-3278a23ba246',
                        title: 'TORRV TEST: Add a node using CM CLI',
                        comment: null
                    },
                    user: {
                        userName: 'someUserCouldBeAnyone'
                    },
                    result: null,
                    defects: []
                }];

                testPlanExecutionContentRegion.testCasesTable.setData(tableData);
                expect(testPlanExecutionContentRegion.testCasesTable.getHeaderRow().getCells().length).equal(8);

                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[1].visible = false;
                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[2].visible = false;
                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[3].visible = false;

                testPlanExecutionContentRegion.tableSettings.trigger('change');
                expect(testPlanExecutionContentRegion.testCasesTable.getHeaderRow().getCells().length).equal(5);

                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[1].visible = true;
                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[2].visible = true;
                testPlanExecutionContentRegion.tableSettings.getUpdatedColumns()[3].visible = true;

                testPlanExecutionContentRegion.tableSettings.trigger('change');
                expect(testPlanExecutionContentRegion.testCasesTable.getHeaderRow().getCells().length).equal(8);

            });
        });
    });
});
