/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignListContentRegion.html',
    'styles!./TestCampaignListContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTestPlans: function () {
            return this.getElement().find('.eaTM-ViewTestCampaignsRegion-testCampaigns');
        }

    });

});
