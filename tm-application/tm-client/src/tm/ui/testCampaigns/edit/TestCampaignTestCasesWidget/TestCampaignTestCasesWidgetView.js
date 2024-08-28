/*global define*/
define([
    'jscore/core',
    'text!./TestCampaignTestCasesWidget.html',
    'styles!./TestCampaignTestCasesWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTestCaseInput: function () {
            return this.getElement().find('.eaTM-TestCampaignTestCases-testCaseInput');
        },

        getLinkButton: function () {
            return this.getElement().find('.eaTM-TestCampaignTestCases-linkButton');
        },

        getSearchButton: function () {
            return this.getElement().find('.eaTM-TestCampaignTestCases-addMultipleButton');
        },

        getTestCaseList: function () {
            return this.getElement().find('.eaTM-TestCampaignTestCases-testCaseList');
        },

        getHeadingCommandsBlock: function () {
            return this.getElement().find('.ebLayout-HeadingCommands-block');
        },

        getTotalCount: function () {
            return this.getElement().find('.eaTM-TestCampaignTestCases-totalCount');
        }

    });

});
