/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignListWidget.html',
    'styles!./TestCampaignListWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getContent: function () {
            return this.getElement().find('.eaTM-TestCampaigns-content');
        },

        getTotalCount: function () {
            return this.getElement().find('.eaTM-TestCampaigns-totalCount');
        },

        getStackedProgressBarHolder: function () {
            return this.getElement().find('.eaTM-TestCampaigns-stackedProgressBarHolder');
        }

    });

});
