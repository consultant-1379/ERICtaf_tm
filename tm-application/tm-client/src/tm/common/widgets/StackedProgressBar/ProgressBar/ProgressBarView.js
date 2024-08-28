/*global define*/
define([
    'jscore/core',
    'template!./_progressBar.html',
    'styles!./_progressBar.less'
], function (core, template, style) {
    'use strict';

    var ProgressBarView = core.View.extend({

        iconModifier: null,

        getTemplate: function () {
            return template(this.options);
        },

        getStyle: function () {
            return style;
        },

        getFill: function () {
            return this.getElement();
        },

        setValue: function (value) {
            if (value < 0) {
                value = 0;
            }
            this.getElement().setStyle('width', value + '%');
        }

    });

    return ProgressBarView;

});
