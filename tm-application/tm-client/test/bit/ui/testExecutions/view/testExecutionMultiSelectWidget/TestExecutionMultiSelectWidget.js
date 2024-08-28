/*global define, expect, describe, it, beforeEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/ui/testExecutions/view/TestExecutionMultiSelectWidget/TestExecutionMultiSelectWidget',
    'tm/ui/testCampaigns/models/TestCampaignsCollection'
], function (core, TestExecutionMultiSelectWidget, TestPlansCollection) {
    'use strict';

    describe('tm/ui/testExecutions/view/TestExecutionMultiSelectWidget/TestExecutionMultiSelectWidget', function () {

        describe('testExecutionMultiSelectWidget', function () {
            var eventBus;
            var references = {
                getById: function () {
                }
            };
            beforeEach(function () {
                eventBus = new core.EventBus();

            });
            it('Populate TestExecutionMultiSelectWidget table and clear the selected data', function () {

                var collection = new TestPlansCollection();
                var testModel1 = {
                    testCase: {
                        id: 1,
                        testCaseId: 'Test-234',
                        title: 'This is a test',
                        sequenceNumber: 1
                    },
                    result: {title: 'Fail', id: '1'}
                };

                var testModel2 = {
                    testCase: {
                        id: 2,
                        testCaseId: 'Test-2347',
                        title: 'This is a test',
                        sequenceNumber: 1
                    },
                    result: {title: 'Pass with Exceptions', id: '1'}
                };

                collection.addModel(testModel1);
                collection.addModel(testModel2);

                var testExecutionMultiSelectWidget = new TestExecutionMultiSelectWidget({
                    testCaseCollection: collection,
                    region: {},
                    references: references,
                    eventBus: eventBus,
                    testPlanId: 'TestPlanId1'
                });

                testExecutionMultiSelectWidget.testCaseTable.checkRows(function () {
                    return true;
                });

                expect(testExecutionMultiSelectWidget.testCaseTable.getSelectedRows().length).equal(2);
                testExecutionMultiSelectWidget.view.getClearButton().trigger('click');
                expect(testExecutionMultiSelectWidget.testCaseTable.getSelectedRows().length).equal(0);

            });
        });
    });
});
