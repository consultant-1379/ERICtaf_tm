define([
    'jscore/core',
    './StatisticsView',
    'i18n!statistics/dictionary.json',
    'layouts/TopSection',
    './dashboardRegion/DashboardRegion'
], function (core, View, Dictionary, TopSection, DashboardRegion) {
    'use strict';

    return core.App.extend({

        view: function () {
            return new View({i18n: Dictionary});
        },

        onStart: function () {
            this.topSection = new TopSection({
                context: this.getContext(),
                title: this.options.properties.title
            });

            this.topSection.setContent(new DashboardRegion({context: this.getContext()}));
            this.topSection.attachTo(this.getElement());
        }
    });

});
