/*global define*/
define([
    'jscore/core',
    'text!./SelectBoxFilter.html',
    'styles!./SelectBoxFilter.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getProductSelectBoxHolder: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-selectProductBox');
        },

        getDropSelectBoxHolder: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-selectDropBox');
        },

        getFeatureSelectBoxHolder: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-selectFeatureBox');
        },

        getComponentSelectBoxHolder: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-selectComponentBox');
        },

        getProductBlock: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-productBlock');
        },

        getDropBlock: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-dropBlock');
        },

        getFeatureBlock: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-featureBlock');
        },

        getComponentBlock: function () {
            return this.getElement().find('.eaTM-SelectBoxFilter-componentBlock');
        },

        showDropBlock: function () {
            this.getDropBlock().removeModifier('hidden', 'eaTM-SelectBoxFilter-dropBlock');
        },

        hideDropBlock: function () {
            this.getDropBlock().setModifier('hidden', undefined, 'eaTM-SelectBoxFilter-dropBlock');
        },

        showComponentBlock: function () {
            this.getComponentBlock().removeModifier('hidden', 'eaTM-SelectBoxFilter-componentBlock');
        },

        hideComponentBlock: function () {
            this.getComponentBlock().setModifier('hidden', undefined, 'eaTM-SelectBoxFilter-componentBlock');
        },

        setLabelsOnTop: function () {
            this.getProductSelectBoxHolder().setModifier('top', '', 'ebInput_labeled');
            this.getDropSelectBoxHolder().setModifier('top', '', 'ebInput_labeled');
            this.getFeatureSelectBoxHolder().setModifier('top', '', 'ebInput_labeled');
            this.getComponentSelectBoxHolder().setModifier('top', '', 'ebInput_labeled');
            this.getProductBlock().setModifier('spaced', '', 'eaTM-SelectBoxFilter-productBlock');
            this.getFeatureBlock().setModifier('spaced', '', 'eaTM-SelectBoxFilter-featureBlock');
        }

    });

});
