/*global define*/
define([
    'jscore/core',
    'text!./TestCaseCreateBarRegion.html',
    'styles!./TestCaseCreateBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.createLinkHolder = element.find('.eaTM-CreateTestCaseBar-createLinkHolder');
            this.cancelLinkHolder = element.find('.eaTM-CreateTestCaseBar-cancelLinkHolder');
            this.projectSelectBox = element.find('.eaTM-CreateTestCaseBarProject-selectBox');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getBackButton: function () {
            return this.backButton;
        },

        getBackIcon: function () {
            return this.backIcon;
        },

        getCreateLinkHolder: function () {
            return this.createLinkHolder;
        },

        getCancelLinkHolder: function () {
            return this.cancelLinkHolder;
        }
    });

});
