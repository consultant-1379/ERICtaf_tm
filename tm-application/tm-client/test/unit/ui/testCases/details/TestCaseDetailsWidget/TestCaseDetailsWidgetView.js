/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseDetailsWidget/TestCaseDetailsWidgetView'
], function (TestCaseDetailsWidgetView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseDetailsWidget/TestCaseDetailsWidgetView', function () {

        it('TestCaseDetailsWidgetView should be defined', function () {
            expect(TestCaseDetailsWidgetView).not.to.be.undefined;
        });

        describe('TestCaseDetailsWidgetView\'s getters should be defined', function () {
            var testCaseDetailsWidgetView = new TestCaseDetailsWidgetView();
            testCaseDetailsWidgetView.render();
            testCaseDetailsWidgetView.afterRender();

            it('testCaseDetailsWidgetView.getElement() should be defined', function () {
                expect(testCaseDetailsWidgetView.getElement()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getLastAuthor() should be defined', function () {
                expect(testCaseDetailsWidgetView.getLastAuthor()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getRequirementsBlock() should be defined', function () {
                expect(testCaseDetailsWidgetView.getRequirementsBlock()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getCreatedAt() should be defined', function () {
                expect(testCaseDetailsWidgetView.getCreatedAt()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getUpdatedAt() should be defined', function () {
                expect(testCaseDetailsWidgetView.getUpdatedAt()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getTestCaseId() should be defined', function () {
                expect(testCaseDetailsWidgetView.getTestCaseId()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getTitle() should be defined', function () {
                expect(testCaseDetailsWidgetView.getTitle()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getDescription() should be defined', function () {
                expect(testCaseDetailsWidgetView.getDescription()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getType() should be defined', function () {
                expect(testCaseDetailsWidgetView.getType()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getExecutionType() should be defined', function () {
                expect(testCaseDetailsWidgetView.getExecutionType()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getComponent() should be defined', function () {
                expect(testCaseDetailsWidgetView.getComponent()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getPriority() should be defined', function () {
                expect(testCaseDetailsWidgetView.getPriority()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getGroup() should be defined', function () {
                expect(testCaseDetailsWidgetView.getGroup()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getContext() should be defined', function () {
                expect(testCaseDetailsWidgetView.getContext()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getPreCondition() should be defined', function () {
                expect(testCaseDetailsWidgetView.getPreCondition()).not.to.be.undefined;
            });

            it('testCaseDetailsWidgetView.getStatus() should be defined', function () {
                expect(testCaseDetailsWidgetView.getTestCaseStatus()).not.to.be.undefined;
            });

        });

    });

});
