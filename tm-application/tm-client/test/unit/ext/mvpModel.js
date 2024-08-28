/*global describe, it, expect, beforeEach*/
/*jshint expr: true */
define([
    'tm/ext/mvpModel',
    'jscore/ext/utils/base/underscore'
], function (Model, _) {
    'use strict';

    describe('ext/mvpModel', function () {

        it('should be defined', function () {
            expect(Model).not.to.be.undefined;
            expect(_).not.to.be.undefined;
        });

        describe('should search', function () {
            var model;

            beforeEach(function () {
                model = new Model();
                model._model = {
                    attributes: {
                        testField: 'test',
                        testArray: [
                            {arrayField: 'arrayTest'},
                            {arrayField: 'arrayTest'}
                        ],
                        testObject: {
                            nestedField: 'nestedValue',
                            nestedArray: [
                                {
                                    arrayField: 'arrayFieldValue',
                                    arrayObject: {
                                        field: 'fieldValue'
                                    }
                                }
                            ],
                            nestedObject: {
                                objectField1: 'test11',
                                objectField2: 'test12'
                            }
                        }
                    }
                };
            });

            it('should search one field in model by pattern', function () {
                var result = model.search(/^test$/, ['testField']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testField'])).to.equal(true);
            });

            it('should search one field in model by pattern', function () {
                var result = model.search(/^test$/, ['testField']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testField'])).to.equal(true);
            });

            it('not found by one field in model by pattern', function () {
                var result = model.search(/^test$/, ['testField1']);
                expect(result.length).to.equal(0);
            });

            it('not equals in search by field in model by pattern', function () {
                var result = model.search(/^(?!test$)/, ['testField']);
                expect(result.length).to.equal(0);
            });

            it('not found by value in model by pattern', function () {
                var result = model.search(/^tested$/, ['testField1']);
                expect(result.length).to.equal(0);
            });

            it('should search two nested object in model by pattern', function () {
                var result = model.search(/^nestedValue$/, ['testObject.nestedField']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testObject'])).to.equal(true);
            });

            it('should search three deep nested object in model by pattern', function () {
                var result = model.search(/^arrayFieldValue$/, ['testObject.nestedArray.arrayField']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testObject'])).to.equal(true);
            });

            it('should search four deep nested object in model by pattern', function () {
                var result = model.search(/^fieldValue$/, ['testObject.nestedArray.arrayObject.field']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testObject'])).to.equal(true);
            });

            it('should search in deep nested object by "equals"', function () {
                var result = model.search('fieldValue', ['testObject.nestedArray.arrayObject.field']);
                expect(result.length).to.equal(1);
                expect(_.isEqual(result, ['testObject'])).to.equal(true);
            });
        });
    });
});
