/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseStepsWidget/TestCaseStepsWidgetView'
], function (TestCaseStepsWidgetView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseStepsWidget/TestCaseStepsWidgetView', function () {

        it('TestCaseStepsWidgetView should be defined', function () {
            expect(TestCaseStepsWidgetView).not.to.be.undefined;
        });

        describe('TestCaseStepsWidgetView\'s getters should be defined', function () {
            var testCaseStepsWidgetView = new TestCaseStepsWidgetView();
            testCaseStepsWidgetView.render();
            testCaseStepsWidgetView.afterRender();

            it('testCaseStepsWidgetView.getElement() should be defined', function () {
                expect(testCaseStepsWidgetView.getElement()).not.to.be.undefined;
            });

            it('testCaseStepsWidgetView.getTestStepsBlock() should be defined', function () {
                expect(testCaseStepsWidgetView.getTestStepsBlock()).not.to.be.undefined;
            });

        });
    });

});
