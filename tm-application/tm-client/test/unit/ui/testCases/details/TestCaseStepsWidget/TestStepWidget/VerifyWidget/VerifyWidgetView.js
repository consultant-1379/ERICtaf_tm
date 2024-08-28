/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseStepsWidget/TestStepWidget/VerifyWidget/VerifyWidgetView'
], function (VerifyWidgetView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseStepsWidget/TestStepWidget/VerifyWidget/VerifyWidgetView', function () {

        it('VerifyWidgetView should be defined', function () {
            expect(VerifyWidgetView).not.to.be.undefined;
        });

        describe('VerifyWidgetView\'s getters should be defined', function () {
            var verifyWidgetView = new VerifyWidgetView();
            verifyWidgetView.render();
            verifyWidgetView.afterRender();

            it('verifyWidgetView.getElement() should be defined', function () {
                expect(verifyWidgetView.getElement()).not.to.be.undefined;
            });

            it('verifyWidgetView.getOrderNr() should be defined', function () {
                expect(verifyWidgetView.getOrderNr()).not.to.be.undefined;
            });

            it('verifyWidgetView.getTitle() should be defined', function () {
                expect(verifyWidgetView.getTitle()).not.to.be.undefined;
            });
        });

    });

});
