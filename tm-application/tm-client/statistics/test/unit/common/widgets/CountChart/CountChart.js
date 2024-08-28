/*global define, describe, it, expect */
/*jshint expr: true */
define([
    'statistics/common/widgets/CountChart/CountChart',
    'statistics/common/widgets/CountChart/CountChartView'
], function (CountChart, CountChartView) {
    'use strict';

    describe('CountChart', function () {
        var countChartView = new CountChartView();
        countChartView.render();
        countChartView.afterRender();

        it('CountChart should be defined', function () {
            expect(CountChart).not.to.be.undefined;
        });

        it('CountChartView.getCountHolder should be defined', function () {
            expect(countChartView.getCountHolder()).not.to.be.undefined;
        });

    });

});
