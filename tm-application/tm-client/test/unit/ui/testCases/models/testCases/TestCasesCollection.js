/*global describe, it, expect*/
/*jshint expr: true */
define([
    'tm/ui/testCases/models/testCases/TestCasesCollection'
], function (TestCasesCollection) {
    'use strict';

    describe('tm/ui/testCases/models/testCases/TestCasesCollection', function () {

        it('TestCasesCollection should be defined', function () {
            expect(TestCasesCollection).not.to.be.undefined;
        });

        it('initial values should be correct', function () {
            var collection = new TestCasesCollection();
            expect(collection.getPerPage()).to.be.equal(20);
            expect(collection.getPage()).to.be.equal(1);
            expect(collection.getTotalCount()).to.be.equal(0);
            expect(collection.getSearchQuery()).to.be.equal('');
        });

        describe('url should be created correctly', function () {

            it('searchQuery is empty', function () {
                var collection = new TestCasesCollection();
                var actualUrl = collection.url();
                expect(actualUrl).to.be.equal('/tm-server/api/test-cases?perPage=20&page=1');
            });

            it('searchQuery is populated and page should be refreshed', function () {
                var collection = new TestCasesCollection();
                collection.setPage(2);
                collection.setPerPage(10);
                collection.setSearchQueryAndResetPage('abc=111');

                var actualUrl = collection.url();
                expect(actualUrl).to.be.equal('/tm-server/api/test-cases?perPage=10&page=1&q=abc%3D111');
            });

        });

    });

});
