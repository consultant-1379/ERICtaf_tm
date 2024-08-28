/*global define, describe, it, expect, beforeEach*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormStepsWidget/TestCaseFormStepsWidget'
], function (TestCaseFormStepsWidget) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormStepsWidget/TestCaseFormStepsWidget', function () {

        it('TestCaseFormStepsWidget should be defined', function () {
            expect(TestCaseFormStepsWidget).not.to.be.undefined;
        });

        describe('testCaseFormStepsWidget.onVerifyAction("itemDrop")', function () {
            var testCaseFormStepsWidget, testSteps;

            beforeEach(function () {
                testSteps = [
                    {
                        id: 1,
                        name: 'Test Step 1',
                        itemIndex: 0,
                        verifies: [],
                        getIndex: function () {
                            return this.itemIndex;
                        }
                    },
                    {
                        id: 2,
                        name: 'Test Step 2',
                        itemIndex: 1,
                        verifies: [],
                        getIndex: function () {
                            return this.itemIndex;
                        }
                    },
                    {
                        id: 3,
                        name: 'Test Step 3',
                        itemIndex: 2,
                        verifies: [],
                        getIndex: function () {
                            return this.itemIndex;
                        }
                    },
                    {
                        id: 4,
                        name: 'Test Step 4',
                        itemIndex: 3,
                        verifies: [],
                        getIndex: function () {
                            return this.itemIndex;
                        }
                    }
                ];

                testCaseFormStepsWidget = new TestCaseFormStepsWidget({
                    steps: testSteps
                });
            });

            it('should do nothing when drop is done on same object', function () {
                var itemDragIndex = 1;
                testCaseFormStepsWidget.onTestStepAction('itemDrop', testSteps[1], itemDragIndex);

                var actualTestStepWidgets = testCaseFormStepsWidget._testStepWidgets;

                expectVerify(testSteps[0], actualTestStepWidgets[0].testStepObj);
                expectVerify(testSteps[1], actualTestStepWidgets[1].testStepObj);
                expectVerify(testSteps[2], actualTestStepWidgets[2].testStepObj);
                expectVerify(testSteps[3], actualTestStepWidgets[3].testStepObj);
            });

            it('should move first element to fourth\'s position', function () {
                var itemDragIndex = 0;
                testCaseFormStepsWidget.onTestStepAction('itemDrop', testSteps[3], itemDragIndex);

                var actualTestStepWidgets = testCaseFormStepsWidget._testStepWidgets;

                expectVerify(testSteps[1], actualTestStepWidgets[0].testStepObj);
                expectVerify(testSteps[2], actualTestStepWidgets[1].testStepObj);
                expectVerify(testSteps[3], actualTestStepWidgets[2].testStepObj);
                expectVerify(testSteps[0], actualTestStepWidgets[3].testStepObj);
            });

            it('should move third element to first\'s position', function () {
                var itemDragIndex = 2;
                testCaseFormStepsWidget.onTestStepAction('itemDrop', testSteps[0], itemDragIndex);

                var actualTestStepWidgets = testCaseFormStepsWidget._testStepWidgets;

                expectVerify(testSteps[2], actualTestStepWidgets[0].testStepObj);
                expectVerify(testSteps[0], actualTestStepWidgets[1].testStepObj);
                expectVerify(testSteps[1], actualTestStepWidgets[2].testStepObj);
                expectVerify(testSteps[3], actualTestStepWidgets[3].testStepObj);
            });

        });

    });

    function expectVerify (expectedTestStepObj, actualTestStepObj) {
        expect(expectedTestStepObj.id).to.be.equal(actualTestStepObj.id);
    }

});
