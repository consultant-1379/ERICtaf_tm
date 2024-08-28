define([
    'jscore/core',
    './StatisticsOptionView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this.statisticsUrl = options.statisticsUrl;
        },

        onViewReady: function () {
            this.view.getStatisticsLink().setText('Statistics');

            if (this.statisticsUrl) {
                this.view.getStatisticsLink().setAttribute('href', this.statisticsUrl);
            }
        }
    });
});
