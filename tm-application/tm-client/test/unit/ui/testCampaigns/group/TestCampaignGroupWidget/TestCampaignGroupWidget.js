/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/group/TestCampaignGroupWidget/TestCampaignGroupWidgetView'
], function (TestCampaignGroupWidgetView) {
    'use strict';

    describe('tm/ui/testCampaigns/group/TestCampaignGroupContentRegion/TestCampaignGroupContentRegionView', function () {

        it('TestCampaignGroupContentRegionView should be defined', function () {
            expect(TestCampaignGroupWidgetView).not.to.be.undefined;
        });

        describe('TestCampaignGroupContentRegionView\'s getters should be defined', function () {
            var testCampaignGroupWidgetView = new TestCampaignGroupWidgetView();
            testCampaignGroupWidgetView.render();
            testCampaignGroupWidgetView.afterRender();

            it('testCampaignGroupWidgetView.getElement() should be defined', function () {
                expect(testCampaignGroupWidgetView.getElement()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getContent() should be defined', function () {
                expect(testCampaignGroupWidgetView.getContent()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getTotalCount() should be defined', function () {
                expect(testCampaignGroupWidgetView.getTotalCount()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getResultHolder() should be defined', function () {
                expect(testCampaignGroupWidgetView.getResultHolder()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getTreeHolder() should be defined', function () {
                expect(testCampaignGroupWidgetView.getTreeHolder()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getTableHolder() should be defined', function () {
                expect(testCampaignGroupWidgetView.getTableHolder()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getTotalTestCases() should be defined', function () {
                expect(testCampaignGroupWidgetView.getTotalTestCases()).not.to.be.undefined;
            });

            it('testCampaignGroupWidgetView.getDetailedProgressBar() should be defined', function () {
                expect(testCampaignGroupWidgetView.getDetailedProgressBar()).not.to.be.undefined;
            });

        });

    });

});
