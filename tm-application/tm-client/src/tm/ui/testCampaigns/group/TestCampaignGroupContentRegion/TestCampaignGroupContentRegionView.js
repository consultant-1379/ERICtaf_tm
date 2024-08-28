/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignGroupContentRegion.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getTestCampaigns: function () {
            return this.getElement().find('.eaTM-ViewTestCampaignsRegion-testCampaigns');
        }

    });

});
