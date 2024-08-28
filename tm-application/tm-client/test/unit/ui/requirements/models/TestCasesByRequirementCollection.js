/*global define, describe, it, expect */
/*jshint expr: true */
define([
    'tm/ui/requirements/models/TestCasesByRequirementCollection'
], function (TestCasesByRequirementCollection) {
    'use strict';

    describe('tm/ui/requirements/models/TestCasesByRequirementCollection', function () {

        it('TestCasesByRequirementCollection should be defined', function () {
            expect(TestCasesByRequirementCollection).not.to.be.undefined;
        });

        it('initial values should be correct', function () {
            var collection = new TestCasesByRequirementCollection();
            expect(collection.getRequirementId()).to.be.equal('');
        });

        describe('url should be created correctly', function () {

            it('should throw Error if requirementId is empty', function () {
                var collection = new TestCasesByRequirementCollection();
                expect(function () {
                    collection.url();
                }).to.throw(Error);
            });

            it('requirementId is populated, and page is not refreshed', function () {
                var collection = new TestCasesByRequirementCollection(),
                    requirementId = 'CIP-123';
                collection.setRequirementId(requirementId);

                var actualUrl = collection.url();
                expect(actualUrl).to.be.equal('/tm-server/api/requirements/' + requirementId);
            });

        });

    });

});
