/*global describe, it, expect, sinon*/
/*jshint expr: true */
define([
    'jscore/core',
    'jscore/ext/mvp',
    'tm/common/table/TableHelper',
    'widgets/Pagination'
], function (core, mvp, TableHelper, Pagination) {
    'use strict';

    describe('tm/common/TableHelper', function () {

        it('TableHelper should be defined', function () {
            expect(TableHelper).not.to.be.undefined;
        });

        describe('TableHelper.updatePage() method', function () {

            it('should do nothing if same pages are displayed', function () {
                var collection = {
                    getTotalCount: function () { return 5; },
                    getPerPage: function () { return 2; },
                    setPage: function () {},
                    getPage: function () { return 1; },
                    fetch: function () {}
                };

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
                    eventBus: {},
                    parent: {},
                    isPaginated: true,
                    table: table
                });

                tableHelper.updatePage();
                expect(tableHelper.pagination).to.be.equal(null);
            });

            it('should destroy old pagination, new pagination should not be created', function () {
                var collection = {
                        getTotalCount: function () { return 5; },
                        getPerPage: function () { return 5; },
                        setPage: function () {},
                        getPage: function () { return 1; },
                        fetch: function () {}
                    },
                    paginationObj = {
                        destroy: function () {}
                    };

                var tableHelper = new TableHelper({
                    collection: collection,
                    eventBus: {},
                    parent: {},
                    isPaginated: true
                });
                tableHelper.pagination = paginationObj;
                tableHelper.count = 3;

                sinon.spy(paginationObj, 'destroy');

                tableHelper.updatePage();
                expect(tableHelper.pagination).to.be.equal(null);

                paginationObj.destroy.restore();
            });

            it('should create a new pagination', function () {
                var collection = {
                        getTotalCount: function () { return 5; },
                        getPerPage: function () { return 2; },
                        setPage: function () {},
                        getPage: function () { return 1; },
                        fetch: function () {}
                    },
                    parent = new core.Element('div');

                var tableHelper = new TableHelper({
                    collection: collection,
                    eventBus: {},
                    parent: parent,
                    isPaginated: true
                });
                tableHelper.count = 3;

                sinon.stub(Pagination.prototype, 'attachTo', function () {});

                tableHelper.updatePage();
                expect(tableHelper.pagination).not.to.be.equal(null);
                expect(Pagination.prototype.attachTo.calledOnce).to.be.true;
                expect(Pagination.prototype.attachTo.calledWith(parent)).to.be.true;

                Pagination.prototype.attachTo.restore();
            });

            it('should create a new pagination and make page change', function (done) {
                var collection = {
                        getTotalCount: function () { return 5; },
                        getPerPage: function () { return 2; },
                        setPage: function () {},
                        getPage: function () { return 1; },
                        fetch: function () {}
                    },
                    parent = new core.Element('div');

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
                    eventBus: {},
                    parent: parent,
                    isPaginated: true,
                    table: table
                });
                tableHelper.count = 3;

                sinon.spy(collection, 'setPage');
                sinon.spy(collection, 'fetch');
                sinon.stub(Pagination.prototype, 'attachTo', function () {});

                tableHelper.updatePage();
                expect(tableHelper.pagination).not.to.be.equal(null);

                Pagination.prototype.attachTo.restore();

                var pageNumber = 2;
                tableHelper.pagination.setPage(1);
                tableHelper.pagination.setPage(pageNumber);
                setTimeout(function () {
                    expect(collection.setPage.calledOnce).to.be.true;
                    expect(collection.setPage.calledWith(pageNumber)).to.be.true;
                    expect(collection.fetch.calledOnce).to.be.true;

                    done();
                }, 10);
            });

        });

    });

});
