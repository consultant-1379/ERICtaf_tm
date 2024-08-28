/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignGroupBarRegion.html',
    'styles!./TestCampaignGroupBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        afterRender: function () {
            var element = this.getElement();
            this.createlinkHolder = element.find('.eaTM-ViewTestCampaignsGroupBar-createLinkHolder');
            this.testCampaignListHolder = element.find('.eaTM-ViewTestCampaignsGroupBar-createLinkHolder');
            this.searchButton = element.find('.ebQuickActionBar-back');
            this.filterHolder = element.find('.eaTM-ViewTestCampaignsGroupBar-filterHolder');
            this.addGroupHolder = element.find('.eaTM-ViewTestCampaignsGroupBar-addGroupHolder');
            this.productSelect = element.find('.eaTM-ViewTestCampaignsGroupBar-productSelect');
        },

        getStyle: function () {
            return styles;
        },

        getSearchButton: function () {
            return this.searchButton;
        },

        getFilterHolder: function () {
            return this.filterHolder;
        },

        getCreateLinkHolder: function () {
            return this.createlinkHolder;
        },

        getTestCampaignListHolder: function () {
            return this.testCampaignListHolder;
        },

        getAddGroupHolder: function () {
            return this.addGroupHolder;
        },

        getProductSelect: function () {
            return this.productSelect;
        }
    });

});
