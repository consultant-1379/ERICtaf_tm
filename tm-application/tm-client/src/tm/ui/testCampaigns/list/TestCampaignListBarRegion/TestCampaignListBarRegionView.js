/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignListBarRegion.html',
    'styles!./TestCampaignListBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        afterRender: function () {
            var element = this.getElement();
            this.groupHolder = element.find('.eaTM-ViewTestCampaignsBar-groupHolder');
        },

        getStyle: function () {
            return styles;
        },

        getSearchButton: function () {
            return this.getElement().find('.ebQuickActionBar-back');
        },

        getFilterHolder: function () {
            return this.getElement().find('.eaTM-ViewTestCampaignsBar-filterHolder');
        },

        getCreateLinkHolder: function () {
            return this.getElement().find('.eaTM-ViewTestCampaignsBar-createLinkHolder');
        },

        getCopyLinkHolder: function () {
            return this.getElement().find('.eaTM-ViewTestCampaignsBar-copyLinkHolder');
        },

        showCopyTestCampaignsButton: function () {
            var copyLinkHolder = this.getElement().find('.eaTM-ViewTestCampaignsBar-copyLinkHolder');
            copyLinkHolder.removeModifier('hidden',  'eaTM-ViewTestCampaignsBar-copyLinkHolder');
        },

        hideCopyTestCampaignsButton: function () {
            var copyLinkHolder = this.getElement().find('.eaTM-ViewTestCampaignsBar-copyLinkHolder');
            copyLinkHolder.setModifier('hidden', undefined, 'eaTM-ViewTestCampaignsBar-copyLinkHolder');
        },

        getGroupHolder: function () {
            return this.groupHolder;
        }
    });

});
