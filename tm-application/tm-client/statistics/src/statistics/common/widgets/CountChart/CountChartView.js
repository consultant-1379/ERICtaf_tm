define([
    'jscore/core',
    'template!./CountChart.html',
    'styles!./CountChart.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        getTemplate: function() {
            return template(this.options);
        },

        getStyle: function() {
            return style;
        },

        afterRender: function () {
            var element = this.getElement();
            this.countHolder = element.find('.eaStatistics-countWidget-holder');
        },

        getCountHolder: function () {
            return this.countHolder;
        }

    });

});
