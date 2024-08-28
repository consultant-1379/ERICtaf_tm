define([
    'jscore/core',
    'text!./StatisticsOption.html',
    'styles!./StatisticsOption.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.statisticsLink = element.find('.eaTmsUserButton-statisticsOption-anchor');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getStatisticsLink: function () {
            return this.statisticsLink;
        }

    });

});
