define([
    'jscore/core',
    'text!./RequirementPillsArrayWidget.html',
    'styles!./RequirementPillsArrayWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.holder = this.getElement().find('.eaTM-ViewTestCasesArray-value');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getHolder: function () {
            return this.holder;
        }

    });

});
