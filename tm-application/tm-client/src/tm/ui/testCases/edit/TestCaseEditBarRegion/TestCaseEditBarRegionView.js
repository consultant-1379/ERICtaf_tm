/*global define*/
define([
    'jscore/core',
    'text!./TestCaseEditBarRegion.html',
    'styles!./TestCaseEditBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.saveLinkHolder = element.find('.eaTM-EditTestCaseBar-saveLinkHolder');
            this.cancelLinkHolder = element.find('.eaTM-EditTestCaseBar-cancelLinkHolder');
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

        getSaveLinkHolder: function () {
            return this.saveLinkHolder;
        },

        getCancelLinkHolder: function () {
            return this.cancelLinkHolder;
        }
    });

});
