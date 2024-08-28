define([
    'jscore/core',
    'text!./TestCampaignExecutionBarRegion.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getBackButton: function () {
            return this.getElement().find('.ebQuickActionBar-back');
        },

        getBackIcon: function () {
            return this.getElement().find('.ebQuickActionBar-backIcon');
        }

    });

});
