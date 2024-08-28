/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/group/TestCampaignGroupContentRegion/TestCampaignGroupContentRegionView'
], function (TestCampaignGroupContentRegionView) {
    'use strict';

    describe('tm/ui/testCampaigns/group/TestCampaignGroupContentRegion/TestCampaignGroupContentRegionView', function () {

        it('TestCampaignGroupContentRegionView should be defined', function () {
            expect(TestCampaignGroupContentRegionView).not.to.be.undefined;
        });

        describe('TestCampaignGroupContentRegionView\'s getters should be defined', function () {
            var testCampaignGroupContentRegionView = new TestCampaignGroupContentRegionView();
            testCampaignGroupContentRegionView.render();

            it('testCampaignGroupContentRegionView.getElement() should be defined', function () {
                expect(testCampaignGroupContentRegionView.getElement()).not.to.be.undefined;
            });

            it('testCampaignGroupContentRegionView.getTestCampaigns() should be defined', function () {
                expect(testCampaignGroupContentRegionView.getTestCampaigns()).not.to.be.undefined;
            });

        });

    });

});
