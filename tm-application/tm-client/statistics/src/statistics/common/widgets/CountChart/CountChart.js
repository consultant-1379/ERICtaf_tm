/**
 * Created by egergle on 07/02/2017.
 */

define([
    'jscore/core',
    './CountChartView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({

        view: function () {
            return new View();
        },

        init: function (options) {
            this.eventName = (options.eventName) ? options.eventName : 'unknown';
            this.height = (options.height) ? options.height : '20rem';

            this.eventBus = options.context.eventBus;
        },

        onViewReady: function () {
            this.getElement().setStyle('height', this.height);
            this.eventBus.subscribe(this.eventName, this.onDataUpdate, this);
        },

        id: 'CountChartWidget',
        actions: [],

        toJSON: function () {
            return {
                content: this.content,
                backgroundColor: this.backgroundColor
            };
        },

        onDataUpdate: function (data) {
            var total = 0;
            data.forEach(function (item) {
                total += parseInt(item.value);
            });

            total = total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getCountHolder().setText(total)
        }
    });
});

