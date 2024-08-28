/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormStepsWidget/TestStepEditWidget/VerifyEditWidget/VerifyEditWidgetView'
], function (VerifyEditWidgetView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormStepsWidget/VerifyListEditWidget/VerifyEditWidget/VerifyEditWidgetView', function () {

        it('VerifyEditWidgetView should be defined', function () {
            expect(VerifyEditWidgetView).not.to.be.undefined;
        });

        describe('VerifyEditWidgetView\'s getters should be defined', function () {
            var verifyEditWidgetView = new VerifyEditWidgetView();
            verifyEditWidgetView.render();
            verifyEditWidgetView.afterRender();

            it('verifyEditWidgetView.getElement() should be defined', function () {
                expect(verifyEditWidgetView.getElement()).not.to.be.undefined;
            });

            it('verifyEditWidgetView.getOrderNr() should be defined', function () {
                expect(verifyEditWidgetView.getOrderNr()).not.to.be.undefined;
            });

            it('verifyEditWidgetView.getTitle() should be defined', function () {
                expect(verifyEditWidgetView.getTitle()).not.to.be.undefined;
            });

            it('verifyEditWidgetView.getIconsBlock() should be defined', function () {
                expect(verifyEditWidgetView.getIconsBlock()).not.to.be.undefined;
            });

        });

    });

});
