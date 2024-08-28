/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormWidget/TestCaseFormWidgetView'
], function (TestCaseFormWidgetView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormWidget/TestCaseFormWidgetView', function () {

        it('TestCaseFormWidgetView should be defined', function () {
            expect(TestCaseFormWidgetView).not.to.be.undefined;
        });

        describe('TestCaseFormWidgetView\'s getters should be defined', function () {
            var testCaseFormWidgetView = new TestCaseFormWidgetView();
            testCaseFormWidgetView.render();
            testCaseFormWidgetView.afterRender();

            it('testCaseFormWidgetView.getElement() should be defined', function () {
                expect(testCaseFormWidgetView.getElement()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getRequirementIdMultiSelect() should be defined', function () {
                expect(testCaseFormWidgetView.getRequirementIdMultiSelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getTestCaseId() should be defined', function () {
                expect(testCaseFormWidgetView.getTestCaseId()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getTitle() should be defined', function () {
                expect(testCaseFormWidgetView.getTitle()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getDescription() should be defined', function () {
                expect(testCaseFormWidgetView.getDescription()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getComponent() should be defined', function () {
                expect(testCaseFormWidgetView.getComponentSelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getPreCondition() should be defined', function () {
                expect(testCaseFormWidgetView.getPreCondition()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getTypeSelect() should be defined', function () {
                expect(testCaseFormWidgetView.getTypeSelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getExecutionTypeSelect() should be defined', function () {
                expect(testCaseFormWidgetView.getExecutionTypeSelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getPrioritySelect() should be defined', function () {
                expect(testCaseFormWidgetView.getPrioritySelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getGroupSelect() should be defined', function () {
                expect(testCaseFormWidgetView.getGroupSelect()).not.to.be.undefined;
            });

            it('testCaseFormWidgetView.getContextSelect() should be defined', function () {
                expect(testCaseFormWidgetView.getContextSelect()).not.to.be.undefined;
            });

        });

    });

});
