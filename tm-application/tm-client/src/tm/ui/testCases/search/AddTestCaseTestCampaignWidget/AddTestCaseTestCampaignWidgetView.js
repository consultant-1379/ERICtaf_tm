define([
    'jscore/core',
    'text!./AddTestCaseTestCampaignWidget.html',
    'styles!./AddTestCaseTestCampaignWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.dropSelectHolder = element.find('.eaTM-AddTestCaseTestCampaignWidget-dropSelectHolder');
            this.featureSelectHolder = element.find('.eaTM-AddTestCaseTestCampaignWidget-featureSelectHolder');
            this.testCampaignSelectHolder = element.find('.eaTM-AddTestCaseTestCampaignWidget-testCampaignSelectHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getDropSelectHolder: function () {
            return this.dropSelectHolder;
        },

        getFeatureSelectHolder: function () {
            return this.featureSelectHolder;
        },

        getTestCampaignSelectHolder: function () {
            return this.testCampaignSelectHolder;
        }

    });

});
