/*global define, describe, it, expect */
/*jshint expr: true */
define([
    'tm/ui/testCampaigns/models/TestCampaignsCollection'
], function (TestPlansCollection) {
    'use strict';

    describe('tm/ui/testCampaigns/models/TestCampaignsCollection', function () {

        it('TestCampaignsCollection should be defined', function () {
            expect(TestPlansCollection).not.to.be.undefined;
        });

        it('initial values should be correct', function () {
            var collection = new TestPlansCollection();
            expect(collection.getPerPage()).to.be.equal(20);
            expect(collection.getPage()).to.be.equal(1);
            expect(collection.getTotalCount()).to.be.equal(0);
        });

        it('url should be created correctly', function () {
            var collection = new TestPlansCollection();
            var searchCriteria = {
                product: {
                    id: 1
                },
                drop: {
                    id: 1
                },
                feature: [{
                    id: 1
                }]
            };
            collection.setSearchCriteria(searchCriteria);
            var actualUrl = collection.url();
            expect(actualUrl).to.be.equal('/tm-server/api/test-campaigns?product=1&feature=1&drop=1&perPage=20&page=1');
        });

    });

});
