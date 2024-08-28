/*global define*/
define([
    'jscore/core',
    'text!./TestCaseImportContentRegion.html',
    'styles!./TestCaseImportContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.input = element.find('.eaTM-ViewTestCaseImport-input');
            this.importButton = element.find('.eaTM-ViewTestCaseImport-importButton');
            this.tableholder = element.find('.eaTM-ViewTestCaseImport-tableHolder');
            this.errorPanel = element.find('.eaTM-ViewTestCaseImport-errorPanel');
            this.loaderHolder = element.find('.eaTM-ViewTestCaseImport-loaderHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getInput: function () {
            return this.input;
        },

        getTableHolder: function () {
            return this.tableholder;
        },

        getErrorPanel: function () {
            return this.errorPanel;
        },

        getImportButton: function () {
            return this.importButton;
        },

        showLoadingBar: function () {
            this.loaderHolder.setStyle('display', 'inline-block');
        },

        hideLoadingBar: function () {
            this.loaderHolder.setStyle('display', 'none');
        }

    });

});
