/*global define*/
define([
    'jscore/core',
    'template!./TestExecutionWidget.html',
    'styles!./TestExecutionWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template(this.options.execution);
        },

        getStyle: function () {
            return styles;
        },

        getFilesBlock: function () {
            return this.getElement().find('.eaTM-TestExecution-filesBlock');
        }

    });

});
