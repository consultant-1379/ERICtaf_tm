/*global define, describe, it, expect */
/*jshint expr: true */
define([
    'jscore/core',
    'statistics/dashboardRegion/DashboardRegion'
], function (core, DashboardRegion) {
    'use strict';

    describe('DashboardRegion', function () {
        this.timeout(15000);

        var eventBus, dashboardRegion, element, server;

        beforeEach(function () {
            eventBus = new core.EventBus();
            dashboardRegion = new DashboardRegion({
                context: {eventBus: eventBus}
            });
            element = core.Element.parse('<div class="eaTM-rTest"></div>');

            server = sinon.fakeServer.create();
            server.autoRespond = true;
            server.autoRespondAfter = 0;

           var data = [
                   {label: 'test1', value: '20000'},
                   {label: 'test2', value: '10000'},
                   {label: 'test3', value: '5000'},
                   {label: 'test4', value: '1000'},
                   {label: 'test5', value: '250'}
               ];

            server.respondWith(
                'GET',
                '/tm-server/api/statistics/test-cases',
                [200, {'Content-Type': 'application/json'}, JSON.stringify(data)]
            );

            server.respondWith(
                'GET',
                '/tm-server/api/statistics/users',
                [200, {'Content-Type': 'application/json'}, JSON.stringify(data)]
            );
        });

        afterEach(function () {
            server.restore();
        });

        it('DashboardRegion create four charts for dashboard', function (done) {
            dashboardRegion.attach(element);
            dashboardRegion.onStart();

            expect(dashboardRegion).not.to.be.undefined;

            var chart1 = dashboardRegion.dashboard.getItem(0,0);
            var chart2 = dashboardRegion.dashboard.getItem(0,1);
            var chart3 = dashboardRegion.dashboard.getItem(1,0);
            var chart4 = dashboardRegion.dashboard.getItem(1,1);

            setTimeout(function () { // needs time to render
                expect(chart1.chart).not.to.be.undefined;
                expect(chart2.view.getCountHolder()).not.to.be.undefined;
                expect(chart3.chart).not.to.be.undefined;
                expect(chart4.chart).not.to.be.undefined;

                done();
            }.bind(this), 1000);

        });

    });

});
