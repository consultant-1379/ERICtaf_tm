/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseEditContentRegion/TestCaseEditContentRegionView'
], function (TestCaseEditContentRegionView) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseEditContentRegion/TestCaseEditContentRegionView', function () {

        it('TestCaseEditContentRegionView should be defined', function () {
            expect(TestCaseEditContentRegionView).not.to.be.undefined;
        });

        describe('TestCaseEditContentRegionView\'s getters should be defined', function () {
            var testCaseEditContentRegionView = new TestCaseEditContentRegionView();
            testCaseEditContentRegionView.render();
            testCaseEditContentRegionView.afterRender();

            it('testCaseEditContentRegionView.getElement() should be defined', function () {
                expect(testCaseEditContentRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseEditContentRegionView.getDetailsBlock() should be defined', function () {
                expect(testCaseEditContentRegionView.getDetailsBlock()).not.to.be.undefined;
            });

            it('testCaseEditContentRegionView.getTestStepsBlock() should be defined', function () {
                expect(testCaseEditContentRegionView.getTestStepsBlock()).not.to.be.undefined;
            });

        });

    });

});
