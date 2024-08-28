/*global sinon, describe, it, expect, beforeEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'tm/ui/testExecutions/create/CreateTestExecutionContentRegion/CreateTestExecutionContentRegion',
    'tm/ui/testExecutions/models/TestExecutionsCollection',
    'tm/ui/testExecutions/models/TestExecutionModel'
], function (core, _, CreateTestExecutionContentRegion, TestExecutionCollection, TestExecutionModel) {
    'use strict';

    describe('tm/ui/testExecutions/create/CreateTestExecutionContentRegion/CreateTestExecutionContentRegion', function () {

        describe('createTestExecutionContentRegion', function () {
            var eventBus, createTestExecutionContentRegion, element;
            var references = {
                getById: function () {
                }
            };

            beforeEach(function () {
                eventBus = new core.EventBus();
                createTestExecutionContentRegion = new CreateTestExecutionContentRegion({
                    context: {eventBus: eventBus},
                    references: references
                });
                element = core.Element.parse('<div class="eaTM-rTest"></div>');
            });

            it('Populate TestExecutionWidget with empty collection', function (done) {
                var resultModel;
                createTestExecutionContentRegion.attach(element);
                createTestExecutionContentRegion.onViewReady();

                sinon.stub(createTestExecutionContentRegion.testResult, 'populateFields', function (value) {
                    resultModel = value;
                });

                createTestExecutionContentRegion.populateExecutionModel();
                createTestExecutionContentRegion.populateTestExecutionWidget();

                _.defer(function () {
                    expect(resultModel).not.to.be.defined;

                    createTestExecutionContentRegion.testResult.populateFields.restore();

                    done();
                });
            });

            it('Populate TestExecutionWidget with non-empty collection', function (done) {
                var collection = new TestExecutionCollection(),
                    testModel1 = new TestExecutionModel(),
                    testModel2 = new TestExecutionModel(),
                    comment = 'This is test ',
                    defects = ['TOR-123', 'DURACI-321'],
                    result = {title: 'Fail', id: '1'},
                    result2 = {title: 'Pass with Exceptions', id: '1'},
                    testId = 'TestId_',
                    resultModel = {};

                createTestExecutionContentRegion.attach(element);
                createTestExecutionContentRegion.onViewReady();

                sinon.stub(createTestExecutionContentRegion.testResult, 'populateFields', function (value) {
                    resultModel = value;
                });

                testModel1.setTestCaseId(testId + 1);
                testModel1.id = 1;
                testModel1.setExecutionResult(result);
                testModel1.setAttribute('comment', comment + 1);
                testModel1.setDefectIds(defects);

                testModel2.setTestCaseId(testId + 2);
                testModel2.id = 2;
                testModel2.setExecutionResult(result2);
                testModel2.setAttribute('comment', comment + 2);
                testModel2.setDefectIds(defects);

                collection.addModel(testModel1);
                collection.addModel(testModel2);

                createTestExecutionContentRegion.testExecutionCollection = collection;
                createTestExecutionContentRegion.populateExecutionModel();
                createTestExecutionContentRegion.populateTestExecutionWidget();

                _.defer(function () {
                    expect(resultModel.getTestCaseId()).equal(testId + 1);

                    createTestExecutionContentRegion.testResult.populateFields.restore();

                    done();
                });
            });
        });
    });
});
