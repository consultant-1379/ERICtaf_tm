/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/ui/testExecutions/create/TestExecutionResultWidget/TestExecutionResultWidget',
    'tm/ui/testExecutions/models/TestExecutionsCollection',
    'tm/ui/testExecutions/models/TestExecutionModel',
    'tm/common/widgets/GenericFileUpload/FileUpload'
], function (core, TestExecutionResultWidget, TestExecutionCollection, TestExecutionModel, FileUpload) {
    'use strict';

    describe('tm/ui/testExecutions/create/TestExecutionResultWidget/TestExecutionResultWidget', function () {

        it('TestExecutionResultWidget should be defined', function () {
            expect(TestExecutionResultWidget).not.to.be.undefined;
        });

        describe('TestExecutionResultWidget', function () {
            var testModel1 = new TestExecutionModel();

            var comment = 'This is test1';
            var defects = ['TOR-123', 'DURACI-321'];
            var result = {title: 'Fail', id: '1'};

            testModel1.setTestCaseId('TestId');
            testModel1.setExecutionResult(result);
            testModel1.setAttribute('comment', comment);
            testModel1.setDefectIds(defects);

            var defaultResult = 'Select result...';

            var references = {
                getById: function () {
                }
            };

            it('populate widget with model data', function () {
                var testExecutionResultWidget = new TestExecutionResultWidget({
                    model: new TestExecutionModel(),
                    references: references,
                    isoCollection: new TestExecutionCollection(),
                    fileWidget: new FileUpload()
                });
                testExecutionResultWidget.populateFields(testModel1);

                var defectIds = testExecutionResultWidget.defectIdMultiSelect.getSelectedItems();

                expect(testExecutionResultWidget.resultSelect.getValue().title).equal(result.title);
                expect(testExecutionResultWidget.view.getComment().getValue()).equal(comment);
                expect(defectIds[0].value).equal(defects[0]);
                expect(defectIds[1].value).equal(defects[1]);
            });

            it('clear data from widget', function () {
                var testExecutionResultWidget = new TestExecutionResultWidget({
                    model: new TestExecutionModel(),
                    references: references,
                    isoCollection: new TestExecutionCollection(),
                    fileWidget: new FileUpload()
                });
                testExecutionResultWidget.populateFields(testModel1);

                var defectIds = testExecutionResultWidget.defectIdMultiSelect.getSelectedItems();

                expect(testExecutionResultWidget.resultSelect.getValue().title).equal(result.title);
                expect(testExecutionResultWidget.view.getComment().getValue()).equal(comment);
                expect(defectIds[0].value).equal(defects[0]);
                expect(defectIds[1].value).equal(defects[1]);

                testExecutionResultWidget.clear();
                defectIds = testExecutionResultWidget.defectIdMultiSelect.getSelectedItems();

                expect(testExecutionResultWidget.resultSelect.getValue().title).equal(defaultResult);
                expect(testExecutionResultWidget.view.getComment().getValue()).equal('');
                expect(defectIds.length).equal(0);
            });
        });
    });
});
