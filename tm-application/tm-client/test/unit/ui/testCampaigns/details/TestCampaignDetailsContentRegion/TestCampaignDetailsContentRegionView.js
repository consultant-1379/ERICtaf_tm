/**
 * Created by ezhigci on 23/10/2017.
 */
/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/details/TestCampaignDetailsContentRegion/TestCampaignDetailsContentRegionView'
], function (TestCampaignDetailsContentRegionView) {
    'use strict';

    describe('tm/ui/testCampaigns/details/TestCampaignDetailsContentRegion/TestCampaignDetailsContentRegionView', function () {

        it('TestCampaignDetailsContentRegionView should be defined', function () {
            expect(TestCampaignDetailsContentRegionView).not.to.be.undefined;
        });

        describe('TestCampaignDetailsContentRegionView\'s getters should be defined', function () {
            var testCampaignDetailsContentRegionView = new TestCampaignDetailsContentRegionView();
            testCampaignDetailsContentRegionView.render();

            it('testCaseDetailsContentRegionView.getElement() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getElement()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getTemplate() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getTemplate()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getStyle() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getStyle()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getMessagesBlock() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getMessagesBlock()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getDetailsBlock() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getDetailsBlock()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getTestCaseList() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getTestCaseList()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getStackedProgressBarHolder() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getStackedProgressBarHolder()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getTotalCount() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getTotalCount()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getSlidingPanels() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getSlidingPanels()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getSettingsContent() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getSettingsContent()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getSettings() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getSettingsButton()).not.to.be.undefined;
            });

            it('testCaseDetailsContentRegionView.getHideSettingsButton() should be defined', function () {
                expect(testCampaignDetailsContentRegionView.getHideSettingsButton()).not.to.be.undefined;
            });
        });

    });

});
