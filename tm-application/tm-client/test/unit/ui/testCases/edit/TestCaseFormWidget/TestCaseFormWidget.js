/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/ui/testCases/edit/TestCaseFormWidget/TestCaseFormWidget',
    'tm/ui/testCases/models/testCases/TestCaseModel',
    'tm/common/models/ReferencesCollection',
    'tm/common/Constants'
], function (core, TestCaseFormWidget, TestCaseModel, ReferencesCollection, Constants) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseFormWidget/TestCaseFormWidget', function () {

        it('TestCaseStepsWidget should be defined', function () {
            expect(TestCaseFormWidget).not.to.be.undefined;
        });

        it('TestCaseModel should be defined', function () {
            expect(TestCaseModel).not.to.be.undefined;
        });

        describe('TestCaseFormWidget\'s testCaseStatusSelect has default value set', function () {
            var options = getTestCaseMockedData();
            var testCaseFormWidget = new TestCaseFormWidget(options);
            var element = new core.Element('div');
            testCaseFormWidget.attachTo(element);
            testCaseFormWidget.updateFields(Constants.pages.TEST_CASE_CREATE, 2);

            it('testCaseStatusSelect By default is set to Preliminary', function () {
                expect(testCaseFormWidget.view.getTestCaseStatus().getText()).equal('Preliminary');
            });
        });

       describe('TestCaseFormWidget\'s testCaseStatusSelect is set to value provided in model', function () {
            var options = getTestCaseMockedData();
            options.model.setTestCaseId('TestId');
            options.model.setTestCaseStatus({name: 'Approved', title: 'Approved', id: '1'});

            var testCaseFormWidget = new TestCaseFormWidget(options);
            var element = new core.Element('div');
            testCaseFormWidget.attachTo(element);
            testCaseFormWidget.updateFields(Constants.pages.TEST_CASE_CREATE, 2);

            it('testCaseStatusSelect is set to provided in model', function () {
                expect(testCaseFormWidget.view.getTestCaseStatus().getText()).equal('Approved');
            });
        });
    });

    function mkRef (id, title) {
        return {id: id, title: title};
    }

    function getTestCaseMockedData () {
        var referencesCollection = new ReferencesCollection([
            {id: 'type', items: [mkRef('1', 'Functional'), mkRef('2', 'Performance')]},
            {id: 'executionType', items: [mkRef('1', 'Manual'), mkRef('2', 'Automated')]},
            {id: 'testCaseStatus', items: [mkRef('1', 'Approved'), mkRef('2', 'Preliminary'), mkRef('3', 'Review')]},
            {id: 'context', items: [mkRef('1', 'REST'), mkRef('2', 'UI'), mkRef('3', 'CLI'), mkRef('4', 'API')]},
            {id: 'group', items: [mkRef('10', 'GAT'), mkRef('7', 'KGB'), mkRef('9', 'RNCDB'), mkRef('8', 'VCDB')]}
        ]);
        referencesCollection.trigger('reset', referencesCollection);

        return {
            model: new TestCaseModel(),
            references: referencesCollection,
            events: {}
        };
    }

});
