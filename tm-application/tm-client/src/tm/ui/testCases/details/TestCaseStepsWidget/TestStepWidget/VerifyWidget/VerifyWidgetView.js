/*global define*/
define([
    'jscore/core',
    'text!./VerifyWidget.html',
    'styles!./VerifyWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.orderNr = element.find('.eaTM-Verify-orderNr');
            this.title = element.find('.eaTM-Verify-title');
            this.titleCompare = element.find('.eaTM-Verify-titleCompare');
            this.actualResultBlock = element.find('.eaTM-Verify-actualResultBlock');
            this.actualResultBox = element.find('.eaTM-Verify-actualResultBox');
            this.fieldset = element.find('.eaTM-Verify-fieldset');
            this.label = element.find('.eaTM-Verify-label');
            this.addLink = element.find('.eaTM-Verify-addLink');
            this.iconBlock = element.find('.eaTM-Verify-iconBlock');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTitle: function () {
            return this.title;
        },

        getOrderNr: function () {
            return this.orderNr;
        },

        getActualResultBlock: function () {
            return this.actualResultBlock;
        },

        getFieldset: function () {
            return this.fieldset;
        },

        getActualResultBox: function () {
            return this.actualResultBox;
        },

        getLabel: function () {
            return this.label;
        },

        getAddLink: function () {
            return this.addLink;
        },

        getIconBlock: function () {
            return this.iconBlock;
        },

        showActualResultBlock: function () {
            this.getActualResultBlock().setModifier('show');
        },

        showActualResultFieldset: function () {
            this.getAddLink().setModifier('hide');
            this.getFieldset().setModifier('show');
        },

        hideActualResultFieldset: function () {
            this.getAddLink().removeModifier('hide');
            this.getFieldset().removeModifier('show');
        },

        getTitleCompare: function () {
            return this.titleCompare;
        }

    });

});
