/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignEditContentRegion.html',
    'styles!./TestCampaignEditContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.detailsBlock = element.find('.eaTM-TestCampaignEditContentRegion-details');
            this.testCasesBlock = element.find('.eaTM-TestCampaignEditContentRegion-testCases');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getDetailsBlock: function () {
            return this.detailsBlock;
        },

        getTestCasesBlock: function () {
            return this.testCasesBlock;
        }

    });

});
