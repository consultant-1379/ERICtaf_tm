/*global define*/
define([
    'jscore/core',
    'text!./FileUpload.html',
    'styles!./FileUpload.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.inputFile = element.find('.eaTM-FileUpload-input');
            this.tableHolder = element.find('.eaTM-FileUpload-tableHolder');
            this.inputFileHolder = element.find('.eaTM-FileUpload-holder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getInputFile: function () {
            return this.inputFile;
        },

        getTableHolder: function () {
            return this.tableHolder;
        },

        hideInputFileHolder: function () {
            return this.inputFileHolder.setModifier('hidden');
        },

        getFileHolder: function () {
            return this.inputFileHolder;
        }
    });

});
