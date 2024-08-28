/*global define*/
define([
    'jscore/core',
    'text!./DefectFormWidget.html',
    'styles!./DefectFormWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.element = this.getElement();
            this.requiredTextHolder = this.element.find('.eaTM-CreateDefect-requiredHolder');
            this.reporterBlock = this.element.find('.eaTM-CreateDefect-reporter');
            this.attachmentInput = this.element.find('.eaTM-CreateDefect-file');
            this.projectLabel = this.element.find('.eaTM-CreateDefect-project');
            this.summaryInput = this.element.find('.eaTM-CreateDefect-summary');
            this.comboHolder = this.element.find('.eaTM-CreateDefect-comboHolder');
            this.multiHolder = this.element.find('.eaTM-CreateDefect-multiHolder');
            this.createButton = this.element.find('.eaTM-CreateDefect-createButton');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        setReporter: function (reporter) {
            this.reporterBlock.setText(reporter);
        },

        getBlockByReferenceId: function (id) {
            return this.element.find('.eaTM-CreateDefect-' + id);
        },

        getTextFieldValue: function (id) {
            return this.getBlockByReferenceId(id).getValue();
        },

        setTextFieldValue: function (id, value) {
            this.getBlockByReferenceId(id).setValue(value);
        },

        getAttachmentInput: function () {
            return this.attachmentInput;
        },

        getProjectHolder: function () {
            return this.projectLabel;
        },

        getSummaryInput: function () {
            return this.summaryInput;
        },

        getComboHolder: function () {
            return this.comboHolder;
        },

        getMultiHolder: function () {
            return this.multiHolder;
        },

        getRequiredTextHolder: function () {
            return this.requiredTextHolder;
        },

        getCreateButton: function () {
            return this.createButton;
        }
    });

});
