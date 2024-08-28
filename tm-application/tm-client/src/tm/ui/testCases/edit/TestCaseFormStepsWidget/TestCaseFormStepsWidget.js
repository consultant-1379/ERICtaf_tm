define([
    'jscore/core',
    './TestCaseFormStepsWidgetView',
    './TestStepEditWidget/TestStepEditWidget'
], function (core, View, TestStepEditWidget) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this._testStepWidgets = [];
            this.testStepActions = {
                dragEnd: dragEnd,
                itemDrop: itemDrop,
                addToList: addItemToList,
                destroy: destroyWidget,
                copy: copyWidget
            };
            this.steps = options.steps || [];
        },

        onViewReady: function () {
            this.view.afterRender();
            this.redrawSteps(this.steps);
            this.view.getAddLink().addEventHandler('click', this.onAddTestStepClick, this);
        },

        redrawSteps: function (steps) {
            clearSteps.call(this);
            createSteps.call(this, steps);
        },

        getValues: function () {
            var testSteps = [];

            this._testStepWidgets.forEach(function (testStepWidget) {
                var titleObj = testStepWidget.getValue();
                if (!titleObj.isDeleted) {
                    testSteps.push(titleObj);
                }
            }.bind(this));

            return testSteps;
        },

        onTestStepAction: function (actionName) {
            if (this.testStepActions[actionName] !== undefined) {
                var args = Array.prototype.slice.call(arguments, 1);
                this.testStepActions[actionName].apply(this, args);
            }
        },

        onAddTestStepClick: function (event) {
            event.preventDefault();
            var index = this._testStepWidgets.length;
            var testStepWidget = createTestStepWidget.call(this, {}, index);
            testStepWidget.attachTo(this.view.getTestStepsBlock());
            this._testStepWidgets.push(testStepWidget);
        },

        onCopyTestStepClick: function (dataToCopy, parentIndex) {
            var index = parentIndex + 1;
            var testStepWidget = createTestStepWidget.call(this, dataToCopy, index);
            testStepWidget.markAsCopy();
            insertCopiedTestStep.call(this, parentIndex, testStepWidget);
        }
    });

    /*************** PRIVATE FUNCTIONS ******************/

    function createTestStepWidget (testStepObj, index) {
        var testStepWidget = new TestStepEditWidget({
            isNew: true,
            testStepObj: testStepObj,
            itemIndex: index,
            itemsCount: index + 1
        });
        testStepWidget.addEventHandler('action', this.onTestStepAction, this);

        return testStepWidget;
    }

    function insertCopiedTestStep (parentIndex, testStepWidget) {
        this._testStepWidgets.splice(parentIndex + 1, 0, testStepWidget);
        for (var i = parentIndex + 2; i < this._testStepWidgets.length; i++) {
            var itemIndex = this._testStepWidgets[i].getIndex();
            this._testStepWidgets[i].setIndex(itemIndex + 1);
        }
        redrawTestSteps.call(this);
    }

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

        var itemsCount = this.steps.length;

        this.steps.forEach(function (stepObj, index) {
            var testStepWidget = new TestStepEditWidget({
                testStepObj: stepObj,
                itemIndex: index,
                itemsCount: itemsCount
            });
            testStepWidget.addEventHandler('action', this.onTestStepAction, this);
            testStepWidget.attachTo(this.view.getTestStepsBlock());

            this._testStepWidgets.push(testStepWidget);
        }.bind(this));
    }

    function dragEnd () {
        this._testStepWidgets.forEach(function (testStepWidget) {
            testStepWidget.disableTestStepDragAndDrop();
        });
    }

    function itemDrop (testStepObj, itemDragIndex) {
        var itemDropIndex = testStepObj.getIndex(),
            itemToDrop = this._testStepWidgets[itemDragIndex];

        if (itemDragIndex === itemDropIndex) {
            return;
        } else {
            var minItemIndex = itemDragIndex > itemDropIndex ? itemDropIndex : itemDragIndex + 1,
                maxItemIndex = itemDragIndex < itemDropIndex ? itemDropIndex : itemDragIndex - 1,
                increment = itemDragIndex > itemDropIndex ? 1 : -1,
                widgetsToChange = this._testStepWidgets.slice(minItemIndex, maxItemIndex + 1);

            this._testStepWidgets[itemDropIndex] = itemToDrop;
            for (var i = minItemIndex, j = 0; i <= maxItemIndex; i++, j++) {
                this._testStepWidgets[i + increment] = widgetsToChange[j];
            }
        }
        redrawTestSteps.call(this);
    }

    function redrawTestSteps () {
        var itemsCount = this._testStepWidgets.length;

        this._testStepWidgets.forEach(function (testStepWidget) {
            testStepWidget.detach();
        });
        this._testStepWidgets.forEach(function (testStepWidget, currIndex) {
            testStepWidget.updateOrder(currIndex, itemsCount);
        });
        this._testStepWidgets.forEach(function (testStepWidget) {
            testStepWidget.attachTo(this.view.getTestStepsBlock());
        }, this);
    }

    function addItemToList () {
        var itemsCount = this._testStepWidgets.length;

        this._testStepWidgets.forEach(function (titleWidget, currIndex) {
            titleWidget.updateOrder(currIndex, itemsCount);
        });
    }

    function destroyWidget (titleObj) {
        var index = titleObj.getIndex();

        // destroy widgets
        this._testStepWidgets[index].destroy();

        // remove from arrays
        this._testStepWidgets.splice(index, 1);
    }

    function copyWidget (stepInfo) {
        stepInfo.name = 'COPY - ' + stepInfo.name;
        this.onCopyTestStepClick(stepInfo, stepInfo.itemIndex);
    }

});
