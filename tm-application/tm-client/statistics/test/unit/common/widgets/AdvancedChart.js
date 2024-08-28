/*global define, describe, it, expect */
/*jshint expr: true */
define([
    'jscore/core',
    'statistics/common/widgets/AdvancedChart'
], function (core, AdvancedChart) {
    'use strict';

    describe('AdvancedChart', function () {
    var eventBus = new core.EventBus();
    var options = {context: {eventBus: eventBus}};

    var data = [
        {label: 'test1', value: '20000'},
        {label: 'test2', value: '10000'},
        {label: 'test3', value: '5000'},
        {label: 'test4', value: '1000'},
        {label: 'test5', value: '250'}
    ];

        it('AdvancedChart should be defined', function () {
            expect(AdvancedChart).not.to.be.undefined;
        });

        it('AdvancedChart should be initialised', function () {
            expect(new AdvancedChart(options)).not.to.be.undefined;
        });

         it('AdvancedChart will create vertical bar chart', function (done) {
            options.eventName = 'test';
            options.type = 'verticalBar';
            var advancedChart = new AdvancedChart(options);
            advancedChart.onDataUpdate(data);

            setTimeout(function () { // needs time to render
                expect(advancedChart.chart.discretebar).not.to.be.undefined;
                 done();
            }.bind(this), 500);

         });

         it('AdvancedChart will create horizontal bar chart', function (done) {
             options.eventName = 'test';
             options.type = 'horizontalBar';
             var advancedChart = new AdvancedChart(options);
             advancedChart.onDataUpdate(data);

             setTimeout(function () { // needs time to render
                 expect(advancedChart.chart.multibar).not.to.be.undefined;
                 done();
             }.bind(this), 500);
          });

          it('AdvancedChart will create pie chart', function (done) {
               options.eventName = 'test';
               options.type = 'pie';
               var advancedChart = new AdvancedChart(options);
               advancedChart.onDataUpdate(data);

               setTimeout(function () { // needs time to render
                   expect(advancedChart.chart.pie).not.to.be.undefined;
                    done();
               }.bind(this), 500);
            });
    });

});
