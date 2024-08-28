/*global define*/
define([
    'jscore/core',
    'text!./CriterionWidget.html',
    'styles!./CriterionWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.field = element.find('.eaTM-Criterion-field');
            this.condition = element.find('.eaTM-Criterion-condition');
            this.valueHolder = element.find('.eaTM-Criterion-value');
            this.valueInput = element.find('.eaTM-Criterion-valueInput');
            this.actions = element.find('.eaTM-Criterion-actions');
            this.typeHolder = element.find('.eaTM-Criterion-typeHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getField: function () {
            return this.field;
        },

        getCondition: function () {
            return this.condition;
        },

        getValueHolder: function () {
            return this.valueHolder;
        },

        getValueInput: function () {
            return this.valueInput;
        },

        getActions: function () {
            return this.actions;
        },

        getTypeHolder: function () {
            return this.typeHolder;
        },

        hideTypeHolder: function () {
            this.typeHolder.setStyle('display', 'none');
        }

    });

});
