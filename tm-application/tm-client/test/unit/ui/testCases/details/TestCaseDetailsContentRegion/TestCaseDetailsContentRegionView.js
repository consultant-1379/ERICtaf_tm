/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseDetailsContentRegion/TestCaseDetailsContentRegionView'
], function (TestCaseDetailsContentRegionView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseDetailsContentRegion/TestCaseDetailsContentRegionView', function () {

        it('TestCaseDetailsContentRegionView should be defined', function () {
            expect(TestCaseDetailsContentRegionView).not.to.be.undefined;
        });

        describe('TestCaseDetailsContentRegionView\'s getters should be defined', function () {
            var testCaseDetailsContentRegionView = new TestCaseDetailsContentRegionView();
            testCaseDetailsContentRegionView.render();
            testCaseDetailsContentRegionView.afterRender();

            it('testCaseDetailsContentRegionView.getElement() should be defined', function () {
                expect(testCaseDetailsContentRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getDetailsBlock() should be defined', function () {
                expect(testCaseDetailsContentRegionView.getDetailsBlock()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getTestStepsBlock() should be defined', function () {
                expect(testCaseDetailsContentRegionView.getTestStepsBlock()).not.to.be.undefined;
            });

        });

    });

});
