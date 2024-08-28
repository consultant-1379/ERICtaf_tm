/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseEditBarRegion/TestCaseEditBarRegionView'
], function (TestCaseEditBarRegionView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseEditBarRegion/TestCaseEditBarRegionView', function () {

        it('TestCaseEditBarRegionView should be defined', function () {
            expect(TestCaseEditBarRegionView).not.to.be.undefined;
        });

        describe('TestCaseEditBarRegionView\'s getters should be defined', function () {
            var testCaseEditBarRegionView = new TestCaseEditBarRegionView();
            testCaseEditBarRegionView.render();
            testCaseEditBarRegionView.afterRender();

            it('testCaseEditBarRegionView.getElement() should be defined', function () {
                expect(testCaseEditBarRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseEditBarRegionView.getBackButton() should be defined', function () {
                expect(testCaseEditBarRegionView.getBackButton()).not.to.be.undefined;
            });

            it('testCaseEditBarRegionView.getBackIcon() should be defined', function () {
                expect(testCaseEditBarRegionView.getBackIcon()).not.to.be.undefined;
            });

            it('testCaseEditBarRegionView.getSaveLinkHolder() should be defined', function () {
                expect(testCaseEditBarRegionView.getSaveLinkHolder()).not.to.be.undefined;
            });

            it('testCaseEditBarRegionView.getCancelLinkHolder() should be defined', function () {
                expect(testCaseEditBarRegionView.getCancelLinkHolder()).not.to.be.undefined;
            });

        });

    });

});
