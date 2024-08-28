/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/details/TestCaseDetailsBarRegion/TestCaseDetailsBarRegionView'
], function (TestCaseDetailsBarRegionView) {
    'use strict';

    describe('tm/ui/testCases/details/TestCaseDetailsBarRegion/TestCaseDetailsBarRegionView', function () {

        it('TestCaseDetailsBarRegionView should be defined', function () {
            expect(TestCaseDetailsBarRegionView).not.to.be.undefined;
        });

        describe('TestCaseDetailsBarRegionView\'s getters should be defined', function () {
            var testCaseDetailsBarRegionView = new TestCaseDetailsBarRegionView();
            testCaseDetailsBarRegionView.render();
            testCaseDetailsBarRegionView.afterRender();

            it('testCaseDetailsBarRegionView.getElement() should be defined', function () {
                expect(testCaseDetailsBarRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseDetailsBarRegionView.getBackButton() should be defined', function () {
                expect(testCaseDetailsBarRegionView.getBackButton()).not.to.be.undefined;
            });

            it('testCaseDetailsBarRegionView.getBackIcon() should be defined', function () {
                expect(testCaseDetailsBarRegionView.getBackIcon()).not.to.be.undefined;
            });

            it('testCaseDetailsBarRegionView.getCreateTestCaseLinkHolder() should be defined', function () {
                expect(testCaseDetailsBarRegionView.getCreateTestCaseLinkHolder()).not.to.be.undefined;
            });

            it('testCaseDetailsBarRegionView.getEditLinkHolder() should be defined', function () {
                expect(testCaseDetailsBarRegionView.getEditLinkHolder()).not.to.be.undefined;
            });

        });

    });

});
