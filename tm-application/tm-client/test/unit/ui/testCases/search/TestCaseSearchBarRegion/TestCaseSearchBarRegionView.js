/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/search/TestCaseSearchBarRegion/TestCaseSearchBarRegionView'
], function (TestCaseSearchBarRegionView) {
    'use strict';

    describe('tm/ui/testCases/search/TestCaseSearchBarRegion/TestCaseSearchBarRegionView', function () {

        it('TestCaseSearchBarRegionView should be defined', function () {
            expect(TestCaseSearchBarRegionView).not.to.be.undefined;
        });

        describe('TestCaseSearchBarRegionView\'s getters should be defined', function () {
            var testCaseSearchBarRegionView = new TestCaseSearchBarRegionView();
            testCaseSearchBarRegionView.render();
            testCaseSearchBarRegionView.afterRender();

            it('testCaseSearchBarRegionView.getQuickSearch() should be defined', function () {
                expect(testCaseSearchBarRegionView.getQuickSearch()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getSearchInput() should be defined', function () {
                expect(testCaseSearchBarRegionView.getSearchInput()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getSearchClearIcon() should be defined', function () {
                expect(testCaseSearchBarRegionView.getSearchClearIcon()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getSearchButton() should be defined', function () {
                expect(testCaseSearchBarRegionView.getSearchButton()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getHideSearchButton() should be defined', function () {
                expect(testCaseSearchBarRegionView.getHideSearchButton()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getMoreButton() should be defined', function () {
                expect(testCaseSearchBarRegionView.getMoreButton()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getContentMenuHolder() should be defined', function () {
                expect(testCaseSearchBarRegionView.getContentMenuHolder()).not.to.be.undefined;
            });

            it('testCaseSearchBarRegionView.getContentSearchMenuHolder() should be defined', function () {
                expect(testCaseSearchBarRegionView.getContentSearchMenuHolder()).not.to.be.undefined;
            });

        });

    });

});
