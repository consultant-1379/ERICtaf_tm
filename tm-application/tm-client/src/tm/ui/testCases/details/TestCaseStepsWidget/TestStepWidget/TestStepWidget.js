define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './TestStepWidgetView',
    './VerifyWidget/VerifyWidget',
    './ResultWidget/ResultWidget'
], function (core, _, View, VerifyWidget, ResultWidget) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.testStepObj = options.testStepObj;
            this.verifies = options.testStepObj.verifies;
            this.itemIndex = options.itemIndex;
            this.dataFieldExpanded = false;
            this.showResult = options.showResult;
            this.verifyWidgets = [];
        },

        onViewReady: function () {
            this.view.afterRender();

            this.testStepObj = this.options.testStepObj;
            this.itemIndex = this.options.itemIndex;

            this.view.getOrderNr().setText(this.itemIndex);
            this.view.getTitle().setText(this.testStepObj.name);

            var testStepData = this.testStepObj.data;
            var testDataFieldHolder = this.view.getDataFieldHolder();
            this.testDataField = this.view.getDataField();
            this.testDataFieldCompare = this.view.getDataFieldCompare();
            this.testDataField.setModifier('short');
            this.testDataFieldCompare.setModifier('short');

            if (testStepData !== null && testStepData !== '') {
                this.testDataField.setText(testStepData);
            } else {
                testDataFieldHolder.setModifier('hide');
            }

            this.testDataField.addEventHandler('dblclick', this.onDataFieldExpanded, this);
            this.testDataFieldCompare.addEventHandler('dblclick', this.onDataFieldExpanded, this);

            createVerifyWidgets.call(this);

            if (this.showResult) {
                this.resultWidget = new ResultWidget();
                this.resultWidget.attachTo(this.view.getResult());
            }
        },

        onDataFieldExpanded: function () {
            if (!this.dataFieldExpanded) {
                this.testDataField.setModifier('long');
                this.testDataFieldCompare.setModifier('long');
                this.testDataField.removeModifier('short');
                this.testDataFieldCompare.removeModifier('short');
                this.dataFieldExpanded = true;
                return;
            }
            this.dataFieldExpanded = false;
            this.testDataField.setModifier('short');
            this.testDataFieldCompare.setModifier('short');
            this.testDataField.removeModifier('long');
            this.testDataFieldCompare.removeModifier('long');
        },

        getTestStepResult: function () {
            if (!this.showResult) {
                return null;
            }
            return this.resultWidget.getResult();
        },

        getVerifyStepsActualResults: function () {
            if (!this.showResult) {
                return null;
            }
            var verifyStepsActualResults = [];
            this.verifyWidgets.forEach(function (verifyWidget) {
                var actualResult = verifyWidget.getActualResult();
                if (actualResult != null) {
                    verifyStepsActualResults.push({
                        verifyStepId: verifyWidget.getVerifyStepId(),
                        actualResult: actualResult
                    });
                }
            });
            return verifyStepsActualResults;
        },

        getTestStepId: function () {
            return this.testStepObj.id;
        },

        getTestStepTitle: function () {
            return this.testStepObj.name;
        },

        getTestStepData: function () {
            return this.testStepObj.data;
        },

        getTestStepVerifyWidgets: function () {
            return this.verifyWidgets;
        },

        applyTestStepResult: function (testStepResult) {
            this.testStepObj.result = testStepResult;
            if (this.resultWidget) {
                this.resultWidget.applyResult(testStepResult);
            }
        },

        applyVerifyStepsActualResults: function (verifyStepsActualResults) {
            this.verifyWidgets.forEach(function (verifyWidget) {
                var verifyStepActualResultObj = _.findWhere(verifyStepsActualResults, {verifyStep: verifyWidget.getVerifyStepId()});
                if (verifyStepActualResultObj) {
                    verifyWidget.applyActualResult(verifyStepActualResultObj.actualResult);
                }
            });
        }

    });

    function createVerifyWidgets () {
        if (this.verifies && this.verifies.length > 0) {
            this.verifies.forEach(function (verifyObj, index) {
                var verifyWidget = new VerifyWidget({
                    stepIndex: this.itemIndex,
                    itemIndex: index + 1,
                    verifyObj: verifyObj,
                    showActualResult: this.showResult
                });
                verifyWidget.attachTo(this.view.getVerifiesList());
                this.verifyWidgets.push(verifyWidget);
            }.bind(this));
        }
    }

});
