/*global describe, it, expect, sinon, beforeEach*/
/*jshint expr: true */
define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'tm/ui/testCases/search/TestCaseSearchContentRegion/TestCaseSearchContentRegion',
    'tm/ui/testCases/search/TestCaseSearchResultsWidget/TestCaseSearchResultsWidget',
    'tm/common/Constants',
    'tm/common/ContextFilter'
], function (core, _, TestCaseSearchContentRegion, TestCaseSearchResultsWidget, Constants, ContextFilter) {
    'use strict';

    describe('tm/ui/testCases/search/TestCaseSearchResultsWidget/TestCaseSearchResultsWidget', function () {
        this.timeout(5000);
        var eventBus,
            testCaseSearchContentRegion,
            element,
            server,
            interval
            ;

        server = sinon.fakeServer.create();
        server.autoRespond = true;
        server.autoRespondAfter = 0;

        server.respondWith(
            'GET',
            /\/tm-server\/api\/test-cases\?(?:\w+=\w+&\w+=\w+&q=productName%3DENM)/,
            [200, {'Content-Type': 'application/json'}, JSON.stringify({
                    items: [{
                        id: 1,
                        testCaseId: 'CIP-4209_Func_12',
                        requirementIds: ['CIP-4209'],
                        title: 'ENM',
                        description: 'Updated test case',
                        type: {id: '6', title: 'Functional'},
                        groups: [{id: '7', title: 'KGB'}]

                    }, {
                        id: 2,
                        testCaseId: 'CIP-4209_Func_13',
                        requirementIds: ['CIP-4210'],
                        title: 'Title2',
                        description: 'Updated test case two',
                        type: {id: '6', title: 'Functional'},
                        groups: [{id: '7', title: 'KGB'}]

                    }],
                    meta: {
                        query: {
                            criteria: [{field: 'productName', operator: '=', value: 'ENM', type: '&'}],
                            sortBy: {descending: true, field: 'id'}
                        }
                    },
                    totalCount: 2
                }
            )]);

        server.respondWith(
            'GET',
            /\/tm-server\/api\/test-cases\?(?:\w+=\w+&\w+=\w+&q=any~Test2)/,
            [200, {'Content-Type': 'application/json'}, JSON.stringify({
                    items: [{
                        id: 1,
                        testCaseId: 'CIP-4209_Func_12',
                        requirementIds: ['CIP-4209'],
                        title: 'Test2',
                        description: 'Updated test case',
                        type: {id: '6', title: 'Functional'},
                        groups: [{id: '7', title: 'KGB'}]

                    }, {
                        id: 2,
                        testCaseId: 'CIP-4209_Func_13',
                        requirementIds: ['CIP-4210'],
                        title: 'Title2',
                        description: 'Updated test case two',
                        type: {id: '6', title: 'Functional'},
                        groups: [{id: '7', title: 'KGB'}]

                    }],
                    meta: {
                        query: {
                            criteria: [{field: 'productName', operator: '=', value: 'ENM', type: '&'}],
                            sortBy: {descending: true, field: 'id'}
                        }
                    },
                    totalCount: 2
                }
            )
            ]);

        server.respondWith(
            'GET',
            /\/tm-server\/api\/test-cases\?(?:\w+=\w+&\w+=\w+&q=productName%3DOSS)/,
            [200, {'Content-Type': 'application/json'}, JSON.stringify({
                    items: [],
                    meta: {
                        query: {
                            criteria: [{field: 'productName', operator: '=', value: 'OSS', type: '&'}],
                            sortBy: {descending: true, field: 'id'}
                        }
                    },
                    totalCount: 0
                }
            )]);

        server.respondWith(
            'GET',
            /\/tm-server\/api\/test-cases\?(?:\w+=\w+&\w+=\w+&q=any~abcde)/,
            [200, {'Content-Type': 'application/json'}, JSON.stringify({
                    items: [],
                    meta: {
                        query: {
                            criteria: [{field: 'productName', operator: '=', value: 'ENM', type: '&'}],
                            sortBy: {descending: true, field: 'id'}
                        }
                    },
                    totalCount: 0
                }
            )]);

        beforeEach(function () {
            eventBus = new core.EventBus();
            testCaseSearchContentRegion = new TestCaseSearchContentRegion({context: {eventBus: eventBus}});
            element = core.Element.parse('<div class="eaTM-rTest"></div>');
            testCaseSearchContentRegion.attach(element);
        });

        it('region should be defined', function () {
            expect(testCaseSearchContentRegion).not.to.be.undefined;
        });

        it('TestCaseSearchResultsWidget\'s does not show generate report link when test case returned but product and search query are empty', function () {
            testCaseSearchContentRegion.updateListViewTable();
            expect(testCaseSearchContentRegion.resultsBlock.generateReportActionLink.getElement().getAttribute('class')).to.include('_hide');
        });

        it('TestCaseSearchResultsWidget\'s does show generate report link when test case returned and search query entered', function () {
            ContextFilter.searchQuery = 'any~Test2';
            testCaseSearchContentRegion.updateListViewTable(ContextFilter.searchQuery);
            interval = setInterval(isHidden.bind(this), 100);
        });

        it('TestCaseSearchResultsWidget\'s does show generate report link when test case returned and product selected', function () {
            ContextFilter.searchQuery = 'productName=ENM';
            testCaseSearchContentRegion.updateListViewTable(ContextFilter.searchQuery);
            interval = setInterval(isHidden.bind(this), 100);
        });

        it('TestCaseSearchResultsWidget\'s does not show generate report link when product selected and test cases is zero', function () {
            ContextFilter.searchQuery = 'productName=OSS';
            testCaseSearchContentRegion.updateListViewTable(ContextFilter.searchQuery);
            expect(testCaseSearchContentRegion.resultsBlock.generateReportActionLink.getElement().getAttribute('class')).to.include('_hide');
        });

        it('TestCaseSearchResultsWidget\'s does not show generate report link when search done and test cases returned is zero', function () {
            ContextFilter.searchQuery = 'any~abcde';
            testCaseSearchContentRegion.updateListViewTable(ContextFilter.searchQuery);
            expect(testCaseSearchContentRegion.resultsBlock.generateReportActionLink.getElement().getAttribute('class')).to.include('_hide');
        });

        function isHidden (done) {
            if (testCaseSearchContentRegion.resultsBlock.testCasesCollection.toJSON().length !== 0) {
                expect(testCaseSearchContentRegion.resultsBlock.generateReportActionLink.getElement().getAttribute('class')).to.not.include('_hide');
                done();
                clearInterval(interval);
            }
        }

    });
});
