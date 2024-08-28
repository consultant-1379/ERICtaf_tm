/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/import/TestCaseImportContentRegion/TestCaseImportContentRegionView'
], function (TestCaseImportContentRegionView) {
    'use strict';

    describe('tm/ui/testCases/import/TestCaseImportContentRegion/TestCaseImportContentRegion', function () {

        it('TestCaseImportContentRegion should be defined', function () {
            expect(TestCaseImportContentRegionView).not.to.be.undefined;
        });

        describe('TestCaseImportContentRegion\'s getters should be defined', function () {
            var testCaseImportContentRegionView = new TestCaseImportContentRegionView();
            testCaseImportContentRegionView.render();
            testCaseImportContentRegionView.afterRender();

            it('testCaseEditContentRegionView.getElement() should be defined', function () {
                expect(testCaseImportContentRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseEditContentRegionView.getDetailsBlock() should be defined', function () {
                expect(testCaseImportContentRegionView.getErrorPanel()).not.to.be.undefined;
            });

            it('testCaseEditContentRegionView.getTestStepsBlock() should be defined', function () {
                expect(testCaseImportContentRegionView.getTableHolder()).not.to.be.undefined;
            });

        });

    });

});
