/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/details/TestCampaignDetailsBarRegion/TestCampaignDetailsBarRegionView'
], function (TestCampaignDetailsBarRegionView) {
    'use strict';

    describe('tm/ui/testCampaigns/details/TestCampaignDetailsBarRegion/TestCampaignDetailsBarRegionView', function () {

        it('TestCaseDetailsBarRegionView should be defined', function () {
            expect(TestCampaignDetailsBarRegionView).not.to.be.undefined;
        });

        describe('TestCampaignDetailsBarRegionView\'s getters should be defined', function () {
            var testCampaignDetailsBarRegionView = new TestCampaignDetailsBarRegionView();
            testCampaignDetailsBarRegionView.render();

            it('TestCampaignDetailsBarRegionView.getElement() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getElement()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getBackButton() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getBackButton()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getBackIcon() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getBackIcon()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getRemoveLinkHolder() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getRemoveLinkHolder()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getEditLinkHolder() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getEditLinkHolder()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getTestCampaignExecutionLinkHolder() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getTestPlanExecutionLinkHolder()).not.to.be.undefined;
            });

            it('TestCampaignDetailsBarRegionView.getStatusChangeBlock() should be defined', function () {
                expect(testCampaignDetailsBarRegionView.getStatusChangeBlock()).not.to.be.undefined;
            });

        });

    });

});
