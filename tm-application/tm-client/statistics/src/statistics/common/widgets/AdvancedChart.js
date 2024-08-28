/**
 * Created by egergle on 07/02/2017.
 */

define([
    'jscore/core',
    'chartlib/base/d3',
    '../nv.d3'
], function (core, d3, nvd3) {
    'use strict';

    return core.Widget.extend({

        VERTICAL_BAR: 'verticalBar',
        HORIZONTAL_BAR: 'horizontalBar',
        PIE: 'pie',

        init: function (options) {
            this.name = (options.name) ? options.name : 'Not Specified';
            this.type = (options.type) ? options.type : this.VERTICAL_BAR;
            this.eventName = (options.eventName) ? options.eventName : 'unknown';
            this.height = (options.height) ? options.height : '20rem';

            this.eventBus = options.context.eventBus;
            this.chartColor = ['#FABB00','#00A9D4', '#E32119','#F08A00','#89BA17','#B1B3B4','#00625F', '#0066B3',
            '#5C5C5C', '#7B0663', '#FF7600'];

        },

        onViewReady: function () {
            this.getElement().setStyle('height', this.height);
            this.eventBus.subscribe(this.eventName, this.onDataUpdate, this);
            this.eventBus.subscribe('dashboard:maximize', this.onUpdate, this);
            this.eventBus.subscribe('dashboard:minimize', this.onUpdate, this);
        },

        id: 'AdvancedWidget',
        actions: [],

        toJSON: function () {
            return {
                content: this.content,
                backgroundColor: this.backgroundColor
            };
        },

        onDataUpdate: function (data) {

            if (this.type === this.HORIZONTAL_BAR) {
                drawHorizontalBarChart.call(this, this.convertData(data));
            } else if (this.type === this.VERTICAL_BAR) {
                drawBarChart.call(this, this.convertData(data));
            } else if (this.type === this.PIE) {
                drawPieChart.call(this, data);
            } else {
                this.getElement().setText('No chart type set')
            }
        },

        onUpdate: function () {
           this.chart.update();
        },

        convertData: function(data){
            var dataSet = [];
            var obj = {
                key: this.name,
                values: data
            };

            dataSet.push(obj);
            return dataSet;
        }

    });

    function drawBarChart(data) {
        var element = this.getElement();

        nvd3.addGraph(function() {
            this.chart = nvd3.models.discreteBarChart()
                .x(function(d) { return d.label;})
                .y(function(d) { return d.value;})
                .valueFormat(d3.format(",.0f"))
                .margin({
                    right: 50
                })
                .margin({
                    left: 80
                })
                .staggerLabels(true)
                .showValues(true)
                .color(this.chartColor);

            this.chart.yAxis.tickFormat(d3.format(',.0f'));

            d3.select(element._getHTMLElement())
                .append("svg")
                .datum(data)
                .transition().duration(500).call(this.chart);

            nvd3.utils.windowResize(this.chart.update);
            return this.chart;

        }.bind(this));

    }

    function drawHorizontalBarChart(data) {
        var element = this.getElement();

        nvd3.addGraph(function() {
            this.chart = nvd3.models.multiBarHorizontalChart()
                .x(function(d) { return d.label; })
                .y(function(d) { return d.value; })
                .valueFormat(d3.format(',.0f'))
                .showValues(true)
                .showControls(false)
                .color(this.chartColor);

            this.chart.yAxis.tickFormat(d3.format(',.0f'));

            d3.select(element._getHTMLElement())
                .append("svg")
                .datum(data)
                .transition().duration(500).call(this.chart);

            nvd3.utils.windowResize(function() {
                this.chart.update();
            }.bind(this));

            return this.chart;
        }.bind(this));
    }

    function drawPieChart (data) {
        var element = this.getElement();

        nvd3.addGraph(function() {
            this.chart = nvd3.models.pieChart()
                .x(function(d) { return d.label;})
                .y(function(d) { return d.value;})
                .valueFormat(d3.format(",.0f"))
                .margin({
                    right: 60
                })
                .labelThreshold(0.05)
                .labelType("percent")
                .showLegend(true)
                .color(this.chartColor)
                .showLabels(true);

            d3.select(element._getHTMLElement())
                .append("svg")
                .datum(data)
                .transition().duration(500).call(this.chart);

            nvd3.utils.windowResize(function() {
                this.chart.update();
            }.bind(this));

            return this.chart;
        }.bind(this));
    }
});
