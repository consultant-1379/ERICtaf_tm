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
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-name');
        },

        getDescription: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-description');
        },

        getProduct: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-product');
        },

        getDrop: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-drop');
        },

        getFeature: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-feature');
        },

        getEnvironment: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-environment');
        },

        getDropBlock: function () {
            return this.getElement().find('.eaTM-TestCampaignDetailsBody-dropBlock');
        },

        showDropBlock: function () {
            this.getDropBlock().removeModifier('hidden', 'eaTM-TestCampaignDetailsBody-dropBlock');
        },

        hideDropBlock: function () {
            this.getDropBlock().setModifier('hidden', undefined, 'eaTM-TestCampaignDetailsBody-dropBlock');
        }
    });

});
