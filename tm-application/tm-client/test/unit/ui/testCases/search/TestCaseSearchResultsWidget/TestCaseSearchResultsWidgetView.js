/**
 * Created by ezhigci on 22/09/2017.
 */
/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/search/TestCaseSearchResultsWidget/TestCaseSearchResultsWidgetView'
], function (TestCaseSearchResultsWidgetView) {
    'use strict';

    describe('tm/ui/testCases/search/TestCaseSearchResultsWidget/TestCaseSearchResultsWidgetView', function () {

        it('TestCaseSearchResultsWidgetView should be defined', function () {
            expect(TestCaseSearchResultsWidgetView).not.to.be.undefined;
        });

        describe('TestCaseSearchResultsWidgetView\'s getters should be defined', function () {
            var testCaseSearchResultsWidgetView = new TestCaseSearchResultsWidgetView();
            testCaseSearchResultsWidgetView.render();
            testCaseSearchResultsWidgetView.afterRender();

            it('testCaseSearchResultsWidgetView.getAddToTestPlanLinkHolder() should be defined', function () {
                expect(testCaseSearchResultsWidgetView.getAddToTestPlanLinkHolder()).not.to.be.undefined;
            });

            it('testCaseSearchResultsWidgetView.getSaveSearchLinkHolder() should be defined', function () {
                expect(testCaseSearchResultsWidgetView.getSaveSearchLinkHolder()).not.to.be.undefined;
            });

            it('testCaseSearchResultsWidgetView.getSaveSearchMenuHolder() should be defined', function () {
                expect(testCaseSearchResultsWidgetView.getSaveSearchMenuHolder()).not.to.be.undefined;
            });

            it('testCaseSearchResultsWidgetView.getExportLinkHolder() should be defined', function () {
                expect(testCaseSearchResultsWidgetView.getExportLinkHolder()).not.to.be.undefined;
            });

        });

    });

});
