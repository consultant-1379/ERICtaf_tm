/*global describe, it, expect*/
/*jshint expr: true */
define([
    'tm/common/ObjectHelper',
    'jscore/ext/utils/base/underscore'
], function (ObjectHelper, _) {
    'use strict';

    describe('tm/common/ObjectHelper', function () {

        it('should be defined', function () {
            expect(ObjectHelper).not.to.be.undefined;
            expect(_).not.to.be.undefined;
        });

        describe('should find value', function () {
            var model = {
                id: 2,
                name: 'Test Plan #2',
                testPlanItems: [
                    {
                        id: 7,
                        testCase: {
                            id: 4,
                            testCaseId: '7a0793c1-143d-4e15-8063-78e0e78280a4',
                            title: 'My Test Case',
                            versionId: 4,
                            comment: null,
                            sequenceNumber: 1,
                            requirementIds: []
                        },
                        release: null,
                        result: null,
                        defectIds: [],
                        testPlan: null,
                        user: 'taf'
                    }
                ]
            };

            var findAllModel = [
                {
                    testCase: {
                        id: 1,
                        description: 'Test 1'

                    }

                },
                {
                    testCase: {
                        id: 2,
                        description: 'Test 2'

                    }
                },
                {
                    testCase: {
                        id: 3,
                        description: 'Test 3'
                    }
                }

            ];

            it('find value of "name" property', function () {
                var value = ObjectHelper.findValue(model, 'name');
                expect(value).to.equal('Test Plan #2');
            });

            it('find value in two nested properties', function () {
                var value = ObjectHelper.findValue(model.testPlanItems, 'testCase.testCaseId');
                expect(value).to.equal('7a0793c1-143d-4e15-8063-78e0e78280a4');
            });

            it('find null value in two nested properties', function () {
                var value = ObjectHelper.findValue(model.testPlanItems, 'testCase.comment');
                expect(value).to.equal(null);
            });

            it('find the same value in non-existing property', function () {
                expect(function () {
                    ObjectHelper.findValue(model.testPlanItems, 'testCase.versionIdd');
                }).to.throw(Error);
            });

            it('find in array', function () {
                var value = ObjectHelper.findValue(model.testPlanItems, 'defectIds');
                expect(_.isEqual(value, [])).to.equal(true);
            });

            it('return null if object is null', function () {
                var value = ObjectHelper.findValue(model.testPlanItems, 'release.title');
                expect(value).to.equal(null);
            });

            it('find all values', function () {
                var value = ObjectHelper.findAll(findAllModel, 'testCase.id');
                expect(value.length).to.equal(3);
                expect(_.isEqual(value, [1, 2, 3])).to.equal(true);
            });

            it('find all values that dont exist', function () {
                expect(function () {
                    ObjectHelper.findAll(findAllModel, 'testCase.id.wrong');
                }).to.throw(Error);
            });

            it('find all in null collection', function () {
                var value = ObjectHelper.findAll(null, 'testCase.id');
                expect(value).to.equal(null);
            });
        });
    });
});
