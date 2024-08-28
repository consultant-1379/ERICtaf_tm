/*global define, describe, it, expect, beforeEach*/
/*jshint expr: true */
define([
    'tm/ui/testCases/edit/TestCaseFormStepsWidget/TestStepEditWidget/TestStepEditWidget'
], function (TestStepEditWidget) {
    'use strict';

    describe('tm/ui/testCases/edit/TestCaseFormStepsWidget/TestStepEditWidget/TestStepEditWidget', function () {

        it('TestStepEditWidget should be defined', function () {
            expect(TestStepEditWidget).not.to.be.undefined;
        });

        describe('testStepEditWidget.onVerifyAction("itemDrop")', function () {
            var testStepEditWidget, testStepObj;

            beforeEach(function () {
                testStepObj = {
                    id: 1,
                    name: 'Test Step 1',
                    verifies: [
                        {id: 1, name: 'Verify 1.1', itemIndex: 0},
                        {id: 2, name: 'Verify 1.2', itemIndex: 1},
                        {id: 3, name: 'Verify 1.3', itemIndex: 2},
                        {id: 4, name: 'Verify 1.4', itemIndex: 3}
                    ]
                };
                testStepEditWidget = new TestStepEditWidget({
                    testStepObj: testStepObj,
                    itemIndex: 1,
                    itemsCount: 4
                });
            });

            it('should do nothing when drop is done on same object', function () {
                testStepEditWidget.onVerifyAction('itemDrop', testStepObj.verifies[1], 1);

                var actualVerifyWidgets = testStepEditWidget._verifyWidgets,
                    expectedVerifyObj = testStepObj.verifies;

                expectVerify(expectedVerifyObj[0], actualVerifyWidgets[0].verifyObj);
                expectVerify(expectedVerifyObj[1], actualVerifyWidgets[1].verifyObj);
                expectVerify(expectedVerifyObj[2], actualVerifyWidgets[2].verifyObj);
                expectVerify(expectedVerifyObj[3], actualVerifyWidgets[3].verifyObj);
            });

            it('should move first element to third\'s position', function () {
                var itemDragIndex = 0;
                testStepEditWidget.onVerifyAction('itemDrop', testStepObj.verifies[2], itemDragIndex);

                var actualVerifyWidgets = testStepEditWidget._verifyWidgets,
                    expectedVerifyObj = testStepObj.verifies;

                expectVerify(expectedVerifyObj[1], actualVerifyWidgets[0].verifyObj);
                expectVerify(expectedVerifyObj[2], actualVerifyWidgets[1].verifyObj);
                expectVerify(expectedVerifyObj[0], actualVerifyWidgets[2].verifyObj);
                expectVerify(expectedVerifyObj[3], actualVerifyWidgets[3].verifyObj);
            });

            it('should move third element to first\'s position', function () {
                var itemDragIndex = 2;
                testStepEditWidget.onVerifyAction('itemDrop', testStepObj.verifies[0], itemDragIndex);

                var actualVerifyWidgets = testStepEditWidget._verifyWidgets,
                    expectedVerifyObj = testStepObj.verifies;

                expectVerify(expectedVerifyObj[2], actualVerifyWidgets[0].verifyObj);
                expectVerify(expectedVerifyObj[0], actualVerifyWidgets[1].verifyObj);
                expectVerify(expectedVerifyObj[1], actualVerifyWidgets[2].verifyObj);
                expectVerify(expectedVerifyObj[3], actualVerifyWidgets[3].verifyObj);
            });

        });

    });

    function expectVerify (expectedVerifyObj, actualVerifyObj) {
        expect(expectedVerifyObj.id).to.be.equal(actualVerifyObj.id);
    }

});
