define([
    'jscore/core',
    'template!./TestCaseReportsWidget.html',
    'styles!./TestCaseReportsWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        }

    });

});
