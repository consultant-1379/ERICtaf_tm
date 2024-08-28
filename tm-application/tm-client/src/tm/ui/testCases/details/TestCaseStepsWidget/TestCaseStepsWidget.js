define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './TestCaseStepsWidgetView',
    './TestStepWidget/TestStepWidget',
    '../../../../common/ComparisonHelper'
], function (core, _, View, TestStepWidget, ComparisonHelper) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.showResult = options.showResult;
            this._testStepWidgets = [];
        },

        onViewReady: function () {
            this.view.afterRender();
            this.steps = this.options.steps || [];
        },

        redrawSteps: function (steps) {
            clearSteps.call(this);
            createSteps.call(this, steps, this.showResult);
        },

        getExecutionResults: function () {
            var testStepExecutions = [],
                verifyStepExecutions = [];

            this._testStepWidgets.forEach(function (testStepWidget) {
                var testStepResult = testStepWidget.getTestStepResult();
                if (testStepResult && testStepResult != null) {
                    testStepExecutions.push({
                        testStep: testStepWidget.getTestStepId(),
                        result: testStepResult
                    });
                }

                var verifyStepsActualResults = testStepWidget.getVerifyStepsActualResults();
                if (verifyStepsActualResults != null) {
                    verifyStepsActualResults.forEach(function (verifyStepActualResult) {
                        verifyStepExecutions.push({
                            testStep: testStepWidget.getTestStepId(),
                            verifyStep: verifyStepActualResult.verifyStepId,
                            actualResult: verifyStepActualResult.actualResult
                        });
                    });
                }

            });

            return {
                testStepExecutions: testStepExecutions,
                verifyStepExecutions: verifyStepExecutions
            };
        },

        setExecutionResults: function (testStepExecutions, verifyStepExecutions) {
            this._testStepWidgets.forEach(function (testStepWidget) {
                var testStepId = testStepWidget.getTestStepId();

                var testStepResultObj = _.findWhere(testStepExecutions, {testStep: testStepId});
                if (testStepResultObj) {
                    testStepWidget.applyTestStepResult(testStepResultObj.result);
                }

                var verifyStepsActualResults = _.where(verifyStepExecutions, {testStep: testStepId});
                if (verifyStepsActualResults.length > 0) {
                    testStepWidget.applyVerifyStepsActualResults(verifyStepsActualResults);
                }
            }.bind(this));
        },

        compareTestSteps: function (model) {
            var testSteps = model.getSteps();
            var index = 0;
            this._testStepWidgets.forEach(function (testStepWidget) {
                var testStep = testSteps[index];
                if (testStep === undefined || testStep === null) {
                    testStep = {name: null, data: null};
                }
                ComparisonHelper.compareTextAndAddText(testStepWidget.getTestStepTitle(),
                    testStep.name,
                    testStepWidget.view.getTitleCompare()
                );

                ComparisonHelper.compareTextAndAddText(testStepWidget.getTestStepData(),
                    testStep.data,
                    testStepWidget.view.getDataFieldCompare()
                );
                index++;

                compareVerifyWidgets.call(this, testStepWidget, testStep);
            });
        },

        clearCompareTestSteps: function () {
            this._testStepWidgets.forEach(function (testStepWidget) {
                testStepWidget.view.getTitleCompare().setText('');
                testStepWidget.view.getDataFieldCompare().setText('');
                testStepWidget.getTestStepVerifyWidgets().forEach(function (verifyStepWidget) {
                    verifyStepWidget.clearCompare();
                });
            });
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function clearSteps () {
        this._testStepWidgets.forEach(function (testStepWidget) {
            testStepWidget.destroy();
        });
        this._testStepWidgets = [];
    }

    function createSteps (steps) {
        if (!steps) {
            this.steps = [];
            return;
        }
        this.steps = steps;

        steps.forEach(function (stepObj, index) {
            var testStepWidget = new TestStepWidget({
                testStepObj: stepObj,
                itemIndex: index + 1,
                showResult: this.showResult
            });
            testStepWidget.attachTo(this.view.getTestStepsBlock());

            this._testStepWidgets.push(testStepWidget);
        }.bind(this));
    }

    function compareVerifyWidgets (testStepWidget, testStep) {
        var verifyIndex = 0;
        testStepWidget.getTestStepVerifyWidgets().forEach(function (verifyStepWidget) {
            var verifyStep = {};
            if (testStep.verifies) {
                verifyStep = testStep.verifies[verifyIndex];
                if (verifyStep === undefined || verifyStep === null) {
                    verifyStep = {name: null};
                }
            } else {
                verifyStep = {name: null};
            }
            ComparisonHelper.compareTextAndAddText(verifyStepWidget.getVerifyStepTitle(),
                verifyStep.name,
                verifyStepWidget.view.getTitleCompare()
            );

            verifyIndex++;
        });
    }
});
