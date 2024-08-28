/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseCreateBarRegion/TestCaseCreateBarRegionView'
], function (TestCaseCreateBarRegionView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseCreateBarRegion/TestCaseCreateBarRegionView', function () {

        it('TestCaseCreateBarRegionView should be defined', function () {
            expect(TestCaseCreateBarRegionView).not.to.be.undefined;
        });

        describe('TestCaseCreateBarRegionView\'s getters should be defined', function () {
            var testCaseCreateBarRegionView = new TestCaseCreateBarRegionView();
            testCaseCreateBarRegionView.render();
            testCaseCreateBarRegionView.afterRender();

            it('testCaseCreateBarRegionView.getElement() should be defined', function () {
                expect(testCaseCreateBarRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseCreateBarRegionView.getBackButton() should be defined', function () {
                expect(testCaseCreateBarRegionView.getBackButton()).not.to.be.undefined;
            });

            it('testCaseCreateBarRegionView.getBackIcon() should be defined', function () {
                expect(testCaseCreateBarRegionView.getBackIcon()).not.to.be.undefined;
            });

            it('testCaseCreateBarRegionView.getCreateLinkHolder() should be defined', function () {
                expect(testCaseCreateBarRegionView.getCreateLinkHolder()).not.to.be.undefined;
            });

            it('testCaseCreateBarRegionView.getCancelLinkHolder() should be defined', function () {
                expect(testCaseCreateBarRegionView.getCancelLinkHolder()).not.to.be.undefined;
            });

        });

    });

});
