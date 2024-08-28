/*global define*/
define([
    'jscore/core',
    'text!./DetailsBlockWidget.html',
    'styles!./DetailsBlockWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.requirementBlock = element.find('.eaTM-DetailsBlock-requirementBlock');
            this.requirementIdLinkBlock = element.find('.eaTM-DetailsBlock-requirementIdLink');
            this.details = element.find('.eaTM-DetailsBlock-details');
            this.detailsInfo = element.find('.eaTM-DetailsBlock-detailsInfo');
            this.testCaseTable = element.find('.eaTM-DetailsBlock-tableBlock');
            this.pagingBlock = element.find('.eaTM-DetailsBlock-pagingBlock');
            this.progressBlock = element.find('.eaTM-DetailsBlock-progressBlock');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getRequirementBlock: function () {
            return this.requirementBlock;
        },

        getRequirementIdLinkBlock: function () {
            return this.requirementIdLinkBlock;
        },

        getRequirementTitle: function () {
            return this.requirementTitle;
        },

        getDetails: function () {
            return this.details;
        },

        getDetailsInfo: function () {
            return this.detailsInfo;
        },

        getTestCasesTable: function () {
            return this.testCaseTable;
        },

        getPagingBlock: function () {
            return this.pagingBlock;
        },

        getProgressBlock: function () {
            return this.progressBlock;
        }

    });

});
