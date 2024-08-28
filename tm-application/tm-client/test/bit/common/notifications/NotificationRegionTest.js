/*global describe, it, expect, beforeEach, afterEach, sinon */
/*jshint expr: true */
define([
    'jscore/core',
    'tm/common/notifications/NotificationRegion/NotificationRegion'
], function (core, NotificationRegion) {
    'use strict';

    describe('tm/common/notifications/NotificationRegion', function () {

        var server;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            server.autoRespond = true;
            server.autoRespondAfter = 0;
        });

        afterEach(function () {
            server.restore();
        });

        it('should ignore closed notifications', function () {
            window.localStorage.setItem(NotificationRegion.LOCAL_STORAGE_KEY, {
                6: true
            });
            server.respondWith(
                'GET',
                /\/rest\/api\/notifications?timestamp=\d+/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify([{
                    id: 5,
                    type: {id: '3', title: 'Warning'},
                    text: 'toasty toast'
                }, {
                    id: 6,
                    type: {id: '1', title: 'Info'},
                    text: 'testy test'
                }])]
            );

            var eventBus = new core.EventBus();
            var element = core.Element.parse('<div class="eaTM-rTest"></div>');
            var region = new NotificationRegion({context: {eventBus: eventBus}});
            region.attach(element);
            region.init();
            region.onViewReady();

            var holder = region.view.getHolder();
            expect(holder.children()).to.have.length(0);

            region.notify('toasty test', NotificationRegion.NOTIFICATION_TYPES.info);

            expect(holder.children()).to.have.length(1); // ID = 6 should be ignored
            expect(holder.find('.ebNotification-label').getText()).to.equal('toasty test');
        });
    });
});
