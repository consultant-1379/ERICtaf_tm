define([
    'jscore/core',
    'text!./TableProgressBar.html',
    'styles!./TableProgressBar.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.holder = this.getElement().find('.eaTM-ViewTestCasesArray-holder');
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
