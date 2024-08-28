/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormStepsWidget/TestStepEditWidget/TestStepEditWidgetView'
], function (TestStepEditWidgetView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormStepsWidget/TestStepEditWidget/TestStepEditWidgetView', function () {

        it('TestStepEditWidgetView should be defined', function () {
            expect(TestStepEditWidgetView).not.to.be.undefined;
        });

        describe('TestStepEditWidgetView\'s getters should be defined', function () {
            var testStepEditWidgetView = new TestStepEditWidgetView();
            testStepEditWidgetView.render();
            testStepEditWidgetView.afterRender();

            it('testStepEditWidgetView.getElement() should be defined', function () {
                expect(testStepEditWidgetView.getElement()).not.to.be.undefined;
            });

            it('testStepEditWidgetView.getOrderNr() should be defined', function () {
                expect(testStepEditWidgetView.getOrderNr()).not.to.be.undefined;
            });

            it('testStepEditWidgetView.getTitle() should be defined', function () {
                expect(testStepEditWidgetView.getTitle()).not.to.be.undefined;
            });

            it('testStepEditWidgetView.getIconsBlock() should be defined', function () {
                expect(testStepEditWidgetView.getIconsBlock()).not.to.be.undefined;
            });

            it('testStepEditWidgetView.getVerifiesList() should be defined', function () {
                expect(testStepEditWidgetView.getVerifiesList()).not.to.be.undefined;
            });

            it('testStepEditWidgetView.getAddLink() should be defined', function () {
                expect(testStepEditWidgetView.getAddLink()).not.to.be.undefined;
            });

        });

    });

});
