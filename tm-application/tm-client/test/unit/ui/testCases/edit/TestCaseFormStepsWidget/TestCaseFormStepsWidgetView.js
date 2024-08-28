/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormStepsWidget/TestCaseFormStepsWidgetView'
], function (TestCaseFormStepsWidgetView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormStepsWidget/TestCaseFormStepsWidgetView', function () {

        it('TestCaseFormStepsWidgetView should be defined', function () {
            expect(TestCaseFormStepsWidgetView).not.to.be.undefined;
        });

        describe('TestCaseFormStepsWidgetView\'s getters should be defined', function () {
            var testCaseFormStepsWidgetView = new TestCaseFormStepsWidgetView();
            testCaseFormStepsWidgetView.render();
            testCaseFormStepsWidgetView.afterRender();

            it('testCaseFormStepsWidgetView.getElement() should be defined', function () {
                expect(testCaseFormStepsWidgetView.getElement()).not.to.be.undefined;
            });

            it('testCaseFormStepsWidgetView.getTestStepsBlock() should be defined', function () {
                expect(testCaseFormStepsWidgetView.getTestStepsBlock()).not.to.be.undefined;
            });

            it('testCaseFormStepsWidgetView.getExpandCollapseIcon() should be defined', function () {
                expect(testCaseFormStepsWidgetView.getExpandCollapseIcon()).not.to.be.undefined;
            });

            it('testCaseFormStepsWidgetView.getExpandCollapseButton() should be defined', function () {
                expect(testCaseFormStepsWidgetView.getExpandCollapseButton()).not.to.be.undefined;
            });

            it('testCaseFormStepsWidgetView.getAddLink() should be defined', function () {
                expect(testCaseFormStepsWidgetView.getAddLink()).not.to.be.undefined;
            });
        });

    });

});
