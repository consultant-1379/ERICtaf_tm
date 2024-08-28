/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseStepsWidget/TestStepWidget/TestStepWidgetView'
], function (TestStepWidgetView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseStepsWidget/TestStepWidget/TestStepWidgetView', function () {

        it('TestStepWidgetView should be defined', function () {
            expect(TestStepWidgetView).not.to.be.undefined;
        });

        describe('TestStepWidgetView\'s getters should be defined', function () {
            var testStepWidgetView = new TestStepWidgetView();
            testStepWidgetView.render();
            testStepWidgetView.afterRender();

            it('testStepWidgetView.getElement() should be defined', function () {
                expect(testStepWidgetView.getElement()).not.to.be.undefined;
            });

            it('testStepWidgetView.getOrderNr() should be defined', function () {
                expect(testStepWidgetView.getOrderNr()).not.to.be.undefined;
            });

            it('testStepWidgetView.getTitle() should be defined', function () {
                expect(testStepWidgetView.getTitle()).not.to.be.undefined;
            });

            it('testStepWidgetView.getVerifiesList() should be defined', function () {
                expect(testStepWidgetView.getVerifiesList()).not.to.be.undefined;
            });
        });

    });

});
