/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/details/TestCampaignDetailsWidget/TestCampaignDetailsWidgetView'
], function (TestCampaignDetailsWidgetView) {
    'use strict';

    describe('tm/ui/testCampaigns/details/TestCampaignDetailsWidget/TestCampaignDetailsWidgetView', function () {

        it('TestCampaignDetailsWidgetView should be defined', function () {
            expect(TestCampaignDetailsWidgetView).not.to.be.undefined;
        });

        describe('TestCampaignDetailsWidgetView\'s getters should be defined', function () {
            var testCampaignDetailsWidgetView = new TestCampaignDetailsWidgetView();
            testCampaignDetailsWidgetView.render();

            it('testCampaignDetailsWidgetView.getName() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getName()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getProduct() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getProduct()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getDrop() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getDrop()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getFeature() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getFeature()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getComponents() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getComponents()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getAuthor() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getAuthor()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getGroup() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getGroup()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getDescription() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getDescription()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getEnvironment() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getEnvironment()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getStartDate() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getStartDate()).not.to.be.undefined;
            });

            it('testCampaignDetailsWidgetView.getEndDate() should be defined', function () {
                expect(testCampaignDetailsWidgetView.getEndDate()).not.to.be.undefined;
            });

        });

    });

});
