/*global sinon, describe, it, expect, beforeEach, afterEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    'tm/common/Constants',
    'tm/common/ContextFilter',
    'tm/ui/testCases/edit/TestCaseEditContentRegion/TestCaseEditContentRegion'
], function (core, $, _, Constants, ContextFilter, TestCaseEditContentRegion) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseEditContentRegion/TestCaseEditContentRegion', function () {

        var eventBus,
            region,
            element,
            server;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            server.autoRespond = true;
            server.autoRespondAfter = 0;

            server.respondWith(
                'GET',
                /\/tm-server\/api\/references\?(?:referenceId=\w+&)/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify([
                    {id: 'type', items: [mkRef('1', 'Functional'), mkRef('2', 'Performance')]},
                    {id: 'executionType', items: [mkRef('1', 'Manual'), mkRef('2', 'Automated')]},
                    {id: 'context', items: [mkRef('1', 'REST'), mkRef('2', 'UI'), mkRef('3', 'CLI'), mkRef('4', 'API')]},
                    {id: 'group', items: [mkRef('10', 'GAT'), mkRef('7', 'KGB'), mkRef('9', 'RNCDB'), mkRef('8', 'VCDB')]}
                ])]
            );

            server.respondWith(
                'GET',
                '/tm-server/api/references?referenceId=project',
                [200, {'Content-Type': 'application/json'}, JSON.stringify([
                    {id: 'project', items: [
                        mkRef('1', 'ASSURE'),
                        mkRef('2', 'CI_FrameworkTeam_PDUOSS'),
                        mkRef('3', 'DURA CI'),
                        mkRef('4', 'SimNet Simulated Networks')
                    ]}
                ])]
            );

            ContextFilter.profileReady = $.Deferred().resolve();
            ContextFilter.profileProject = {id: '1', externalId: 'EQEV', name: 'ASSURE'};
            ContextFilter.profileProduct = {id: 1, externalId: 'ENM', name: 'ENM'};
            ContextFilter.productIdParam = 'ENM';
            ContextFilter.featureIdParam = 'PM';
            eventBus = new core.EventBus();
            region = new TestCaseEditContentRegion({context: {eventBus: eventBus}});
            element = core.Element.parse('<div class="eaTM-rTest"></div>');
        });

        afterEach(function () {
            server.restore();
        });

        var mkRef = function (id, title) {
            return {id: id, title: title};
        };

        it('should populate from query string', function (done) {
            region.attach(element);
            region.init();
            region.onViewReady();

            region.redrawPage({
                screenId: Constants.pages.TEST_CASE_CREATE,
                options: {
                    query: {
                        testCaseId: ['tcid'],
                        title: ['title'],
                        description: ['description'],
                        precondition: ['precondition'],
                        type: [''],
                        executionType: ['automated'],
                        context: ['ui', 'rest'],
                        group: ['kgb', 'gat']
                    }
                }
            });

            _.defer(function () {
                var form = region.detailsWidget;
                expect(form.view.getTestCaseId().getValue()).to.equal('tcid');
                expect(form.view.getDescription().getValue()).to.equal('description');
                expect(form.executionTypeSelect.getValue()).to.have.property('name', 'Automated');

                var groups = form.groupMultiSelect.getSelectedItems();
                expect(groups.length).to.equal(2);
                expect(groups).to.have.deep.property('[0].name', 'GAT');
                expect(groups).to.have.deep.property('[1].name', 'KGB');

                var contexts = form.contextMultiSelect.getSelectedItems();
                expect(groups.length).to.equal(2);
                expect(contexts).to.have.deep.property('[0].name', 'REST');
                expect(contexts).to.have.deep.property('[1].name', 'UI');

                done();
            });
        });

    });

});
