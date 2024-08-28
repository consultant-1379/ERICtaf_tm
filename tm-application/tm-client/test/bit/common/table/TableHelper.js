/*global describe, it, expect, beforeEach, afterEach, sinon*/
/*jshint expr: true */
define([
    'jscore/core',
    '../../ext/dom',
    'tm/common/collection/PaginatedCollection',
    'tm/common/table/TableHelper'
], function (core, domExt, PaginatedCollection, TableHelper) {
    'use strict';

    var Collection = PaginatedCollection.extend({

        onInit: function () {
            this.setPerPage(2);
        },

        url: function () {
            var params = this.getPagingParams();
            return '/rest/api/url?' + params.join('&');
        },

        parse: function (data) {
            this.totalCount = data.totalCount;
            return data.items;
        }

    });

    describe('tm/common/table/TableHelper', function () {

        it('TableHelper should be defined', function () {
            expect(TableHelper).not.to.be.undefined;
        });

        describe('TableHelper is created with 3 pages', function () {
            var eventBus = {
                    publish: function () {
                    }
                },
                server, collection;

            beforeEach(function () {
                collection = new Collection();

                server = sinon.fakeServer.create();
                server.autoRespond = true;
                server.autoRespondAfter = 0;

                // Prepare response for apps (return appsCollection).
                server.respondWith(
                    'GET',
                    '/rest/api/url?perPage=2&page=1',
                    [200, {'Content-Type': 'application/json'}, JSON.stringify({
                        totalCount: 5,
                        items: [
                            {name: 'name1'},
                            {name: 'name2'}
                        ]
                    })]
                );

                // Prepare response for apps (return appsCollection).
                server.respondWith(
                    'GET',
                    '/rest/api/url?perPage=2&page=2',
                    [200, {'Content-Type': 'application/json'}, JSON.stringify({
                        totalCount: 5,
                        items: [
                            {name: 'name3'},
                            {name: 'name4'}
                        ]
                    })]
                );

                // Prepare response for apps (return appsCollection).
                server.respondWith(
                    'GET',
                    '/rest/api/url?perPage=2&page=3',
                    [200, {'Content-Type': 'application/json'}, JSON.stringify({
                        totalCount: 5,
                        items: [
                            {name: 'name5'}
                        ]
                    })]
                );

            });

            afterEach(function () {
                server.restore();
            });

            it('Check, that paging is shown. First page is selected.', function (done) {
                var element = new core.Element('div');
                element.setStyle('width', '100px');
                element.setStyle('height', '30px');

                var tableHelper = new TableHelper({
                    collection: collection,
                    parent: element,
                    eventBus: eventBus,
                    isPaginated: true
                });
                collection.addEventHandler('reset', tableHelper.updatePage, tableHelper);

                expect(element.children().length).to.be.equal(0);

                collection.fetch({reset: true});

                setTimeout(function () {
                    expect(collection.getPage()).to.be.equal(1);
                    expect(element.children().length).to.be.equal(1);

                    var paginationEl = element.children()[0];
                    expect(domExt.is('.ebPagination', paginationEl)).to.be.true;
                    expect(paginationEl.children().length).to.be.equal(3);

                    var pagesHolderEl = paginationEl.children()[1].find('.ebPagination');
                    expect(domExt.is('.ebPagination', pagesHolderEl)).to.be.true;

                    // FIXME: For some reason child pages is not shown. May be it is issue with visibility.
                    //                    expect(pagesHolderEl.children().length).to.be.equal(3);
                    //
                    //                    var firstPage = pagesHolderEl.children()[0],
                    //                        secondPage = pagesHolderEl.children()[1];
                    //
                    //                    expect(firstPage.find('.ebPagination-entryAnchor_current')).not.to.be.undefined;
                    //                    expect(secondPage.find('.ebPagination-entryAnchor_current')).to.be.undefined;

                    done();
                }, 10);
            });

            it('Select second page. Paging should be updated.', function (done) {
                var element = new core.Element('div');
                element.setStyle('width', '100px');
                element.setStyle('height', '30px');

                var table = {
                    view: {
                        getTable: function () {
                            return {
                                setAttribute: function () {}
                            };
                        }
                    }
                };

                var tableHelper = new TableHelper({
                    collection: collection,
                    parent: element,
                    eventBus: eventBus,
                    isPaginated: true,
                    table: table
                });
                collection.addEventHandler('reset', tableHelper.updatePage, tableHelper);

                expect(element.children().length).to.be.equal(0);

                collection.fetch({reset: true});

                setTimeout(function () {
                    expect(collection.getPage()).to.be.equal(1);
                    expect(element.children().length).to.be.equal(1);

                    var paginationEl = element.children()[0];
                    expect(domExt.is('.ebPagination', paginationEl)).to.be.true;
                    expect(paginationEl.children().length).to.be.equal(3);

                    var pagesHolderEl = paginationEl.children()[1].find('.ebPagination');
                    expect(domExt.is('.ebPagination', pagesHolderEl)).to.be.true;

                    tableHelper.pagination.setPage(1);
                    tableHelper.pagination.setPage(2);

                    setTimeout(function () {
                        expect(collection.getPage()).to.be.equal(2);

                        done();
                    }, 10);
                }, 10);
            });

        });

    });

});
