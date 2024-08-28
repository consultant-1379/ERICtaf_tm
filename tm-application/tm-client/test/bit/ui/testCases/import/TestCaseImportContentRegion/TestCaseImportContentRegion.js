/*global describe, it, expect, beforeEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/ui/testCases/import/TestCaseImportContentRegion/TestCaseImportContentRegion'
], function (core, TestCaseImportContentRegion) {
    'use strict';

    describe('tm/ui/testCases/import/TestCaseImportContentRegion/TestCaseImportContentRegion', function () {

        var eventBus,
            region,
            element;

        beforeEach(function () {
            eventBus = new core.EventBus();
            region = new TestCaseImportContentRegion({context: {eventBus: eventBus}});
            element = core.Element.parse('<div class="eaTM-rTest"></div>');
        });

        it('region should be defined', function () {
            region.attach(element);
            region.init();
            region.onViewReady();

            expect(region).not.to.be.undefined;

        });

    });

});
