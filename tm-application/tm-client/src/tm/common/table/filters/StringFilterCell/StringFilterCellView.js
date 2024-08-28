define([
    'jscore/core',
    'text!./StringFilterCell.html',
    'styles!./StringFilterCell.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.inputBox = element.find('.eaTM-StringFilter-input');
            this.optionsElement = element.find('.eaTM-StringFilter-options');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getInputBox: function () {
            return this.inputBox;
        },

        getOptionsBlock: function () {
            return this.optionsElement;
        }

    });

});
