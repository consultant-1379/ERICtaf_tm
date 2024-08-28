/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignDetailsWidget.html',
    'styles!./TestCampaignDetailsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getName: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-name');
        },

        getProduct: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-product');
        },

        getDrop: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-drop');
        },

        getFeature: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-feature');
        },

        getComponents: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-components');
        },

        getDescription: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-description');
        },

        getEnvironment: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-environment');
        },

        getStartDate: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-startDate');
        },

        getEndDate: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-endDate');
        },

        getDropBlock: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-dropBlock');
        },

        getComponentBlock: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-componentBlock');
        },

        showDropBlock: function () {
            this.getDropBlock().removeModifier('hidden', 'eaTM-TestCampaignDetails-dropBlock');
        },

        hideDropBlock: function () {
            this.getDropBlock().setModifier('hidden', undefined, 'eaTM-TestCampaignDetails-dropBlock');
        },

        showComponentBlock: function () {
            this.getComponentBlock().removeModifier('hidden', 'eaTM-TestCampaignDetails-componentBlock');
        },

        hideComponentBlock: function () {
            this.getComponentBlock().setModifier('hidden', undefined, 'eaTM-TestCampaignDetails-componentBlock');
        },

        getGroup: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-group');
        },

        getAuthor: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-author');
        },

        getPSFrom: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-psFrom');
        },

        getPSTo: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-psTo');
        },

        getGuideRevision: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-guideRevision');
        },

        getSedRevision: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-sedRevision');
        },

        getOtherDependentSW: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-otherDependentSW');
        },

        getNodeTypeVersion: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-nodeTypeVersion');
        },
        getSovStatus: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-sovStatus');
        },
        getComment: function () {
            return this.getElement().find('.eaTM-TestCampaignDetails-comment');
        }
    });

});
