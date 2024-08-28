/*global sinon, describe, it, expect, beforeEach, afterEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    'tm/common/Constants',
    'tm/common/ContextFilter',
    'tm/ui/testCampaigns/edit/TestCampaignEditContentRegion/TestCampaignEditContentRegion'
], function (core, $, _, Constants, ContextFilter, TestCampaignEditContentRegion) {
    'use strict';

    describe('tm/ui/testCampaigns/edit/TestCampaignEditContentRegion/TestCampaignEditContentRegion', function () {

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
                /\/tm-server\/api\/products\/features\/components\?featureId=(\d+)/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify(
                    [{
                        id: 255,
                        name: 'PMIC: CTUM',
                        featureName: 'PM',
                        feature: null
                    },
                        {
                            id: 259,
                            name: 'PMIC: Cell Trace Decoder',
                            featureName: 'PM',
                            feature: null
                        },
                        {
                            id: 257,
                            name: 'PMIC: Cell Trace Subscription',
                            featureName: 'PM',
                            feature: null
                        },
                        {
                            id: 38,
                            name: 'Deployment-DEPL_3PP-ERICactiviti',
                            featureName: 'Deployment',
                            feature: {
                                id: 11,
                                name: 'Deployment',
                                product: {
                                    id: 3,
                                    externalId: 'OSS-RC',
                                    name: 'OSS-RC',
                                    dropCapable: true
                                }
                            }
                        }]
                )]);

            server.respondWith(
                'GET',
                /\/tm-server\/api\/products\/(\d+)\/drops/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify(
                    [{
                        id: 6,
                        name: '2.0.OSS',
                        product: {
                            id: 3,
                            externalId: 'OSS-RC',
                            name: 'OSS-RC',
                            dropCapable: true

                        },
                        productName: null,
                        defaultDrop: false
                    },
                        {
                            id: 2,
                            name: '1.1.ENM',
                            product: {
                                id: 2,
                                externalId: 'ENM',
                                name: 'ENM',
                                dropCapable: true
                            },
                            productName: null,
                            defaultDrop: false
                        },
                        {
                            id: 3,
                            name: '2.0.ENM',
                            product: {
                                id: 2,
                                externalId: 'ENM',
                                name: 'ENM',
                                dropCapable: true
                            },
                            productName: null,
                            defaultDrop: false
                        },
                        {
                            id: 4,
                            name: '16.3',
                            product: {
                                id: 2,
                                externalId: 'ENM',
                                name: 'ENM',
                                dropCapable: true
                            },
                            productName: null,
                            defaultDrop: false
                        }
                    ]
                )]);

            server.respondWith(
                'GET',
                /\/tm-server\/api\/products\/(\d+)\/features/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify(
                    [{
                        id: 17,
                        name: 'CM',
                        product: {
                            id: 2,
                            externalId: 'ENM',
                            name: 'ENM',
                            dropCapable: true
                        }
                    },
                        {
                            id: 11,
                            name: 'Deployment',
                            product: {
                                id: 3,
                                externalId: 'OSS-RC',
                                name: 'OSS-RC',
                                dropCapable: true
                            }
                        },

                        {
                            id: 16,
                            name: 'FM',
                            product: {
                                id: 2,
                                externalId: 'ENM',
                                name: 'ENM',
                                dropCapable: true
                            }
                        },
                        {
                            id: 22,
                            name: 'Monitoring & TroubleShooting',
                            product: {
                                id: 2,
                                externalId: 'ENM',
                                name: 'ENM',
                                dropCapable: true
                            }
                        }
                    ]
                )]);

            server.respondWith(
                'GET',
                /\/tm-server\/api\/products/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify(
                    [{
                        id: 1,
                        externalId: 'DE',
                        name: 'DE',
                        dropCapable: true
                    },
                        {
                            id: 2,
                            externalId: 'ENM',
                            name: 'ENM',
                            dropCapable: true
                        },
                        {
                            id: 3,
                            externalId: 'OSS-RC',
                            name: 'OSS-RC',
                            dropCapable: true
                        },
                        {
                            id: 4,
                            externalId: 'Eiffel',
                            name: 'Eiffel',
                            dropCapable: false
                        }
                    ]
                )]);

            server.respondWith(
                'GET',
                /\/tm-server\/api\/test-campaigns\/(\d+)\?view=detailed/,
                [200, {'Content-Type': 'application/json'}, JSON.stringify({
                        id: 19,
                        parentId: null,
                        name: 'OSS-RC Test Plan #4',
                        description: 'OSS-RC Test Plan #4',
                        environment: 'test',
                        product: {
                            id: 3,
                            externalId: 'OSS-RC',
                            name: 'OSS-RC',
                            dropCapable: true
                        },
                        drop: {
                            id: 6,
                            name: '2.0.OSS',
                            product: {
                                id: 3,
                                externalId: 'OSS-RC',
                                name: 'OSS-RC',
                                dropCapable: true

                            },
                            productName: null,
                            defaultDrop: false
                        },
                        features: [
                            {
                                id: 11,
                                name: 'Deployment',
                                product: {
                                    id: 3,
                                    externalId: 'OSS-RC',
                                    name: 'OSS-RC',
                                    dropCapable: true
                                }
                            }
                        ],
                        components: [
                            {
                                id: 38,
                                name: 'Deployment-DEPL_3PP-ERICactiviti',
                                featureName: 'Deployment',
                                feature: {
                                    id: 11,
                                    name: 'Deployment',
                                    product: {
                                        id: 3,
                                        externalId: 'OSS-RC',
                                        name: 'OSS-RC',
                                        dropCapable: true
                                    }
                                }
                            }
                        ],
                        systemVersion: null,
                        startDate: null,
                        endDate: null,
                        hostname: null,
                        locked: false,
                        autoCreate: false,
                        author: null,
                        testCampaignItems: [],
                        groups: [],
                        project: null
                    }
                )]);

            ContextFilter.profileReady = $.Deferred().resolve();
            ContextFilter.profileProject = {id: '1', externalId: 'EQEV', name: 'ASSURE'};
            ContextFilter.profileProduct = {id: 3, externalId: 'OSS-RC', name: 'OSS-RC', dropCapable: true};
            ContextFilter.productIdParam = 'ENM';
            ContextFilter.featureIdParam = 'PMIC';

            eventBus = new core.EventBus();
            region = new TestCampaignEditContentRegion({context: {eventBus: eventBus}});

            element = core.Element.parse('<div class="eaTM-rTest"></div>');
        });

        afterEach(function () {
            server.restore();
        });

        it('should populate product,drop,feature and component options', function (done) {
            region.attach(element);
            region.init();
            region.onViewReady();

            region.redrawPage({
                screenId: Constants.pages.TEST_PLAN_EDIT,
                testCaseId: '10',
                itemId: '10'
            });

            eventBus.publish(Constants.events.PRODUCT_CHANGED, {
                id: 3,
                externalId: 'OSS-RC',
                name: 'OSS-RC',
                dropCapable: true
            });

            _.defer(function () {
                var form = region.detailsWidget;
                expect(form.view.testPlanName.getValue()).to.equal('OSS-RC Test Plan #4');
                expect(form.view.testPlanDescription.getValue()).to.equal('OSS-RC Test Plan #4');
                expect(form.view.environment.getValue()).to.equal('test');

                expect(form.startDate.getValue()).to.be.undefined;
                expect(form.endDate.getValue()).to.be.undefined;
                expect(form.multiSelect.getSelectedItems()[0]).to.be.undefined;

                expect(form.selectBoxFilter.productSelect.getValue().name).to.equal('OSS-RC');
                expect(form.selectBoxFilter.dropSelect.getValue().name).to.equal('2.0.OSS');
                expect(form.selectBoxFilter.featureSelect.getValue()[0].name).to.equal('Deployment');
                expect(form.selectBoxFilter.componentSelect.getValue()[0].name).to.equal(
                    'Deployment-DEPL_3PP-ERICactiviti');
                done();

            });

        });

    });

});
