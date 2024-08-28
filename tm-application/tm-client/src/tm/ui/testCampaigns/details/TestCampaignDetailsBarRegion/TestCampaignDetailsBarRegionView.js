define([
    'jscore/core',
    'text!./TestCampaignDetailsBarRegion.html',
    'styles!./TestCampaignDetailsBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getBackButton: function () {
            return this.getElement().find('.ebQuickActionBar-back');
        },

        getBackIcon: function () {
            return this.getElement().find('.ebQuickActionBar-backIcon');
        },

        getRemoveLinkHolder: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBar-removeLinkHolder');
        },

        getEditLinkHolder: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBar-editLinkHolder');
        },

        getTestPlanExecutionLinkHolder: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBar-executionLinkHolder');
        },

        getStatusChangeBlock: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBar-statusChangeBlock');
        },

        getCopyLinkHolder: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBar-copyLinkHolder');
        }

    });

});
