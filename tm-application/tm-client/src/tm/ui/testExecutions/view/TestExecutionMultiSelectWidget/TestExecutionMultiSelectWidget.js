define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './TestExecutionMultiSelectWidgetView',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SortableHeader',
    'tablelib/plugins/ColorBand',
    'widgets/Notification',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../../common/table/plugins/IdentifiableTable/IdentifiableTable',
    '../../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    '../../../../common/table/TableHelper',
    '../../../../common/Constants',
    '../../../../common/cells/CellFactory',
    '../../../../common/Navigation',
    'widgets/SelectBox',
    '../../models/TestExecutionModel',
    '../../models/TestCasesExecutionsModel',
    '../../../../common/ModelHelper',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../models/DefectIdsCollection',
    '../../../../common/widgets/multiSelect/MultiSelect',
    '../../../../ext/stringUtils'
], function (core, _, View, Table, Selection, SortableHeader, ColorBand, Notification, StringFilterCell,
             IdentifiableTable, IdentifiableRows, TableHelper, Constants, CellFactory, Navigation, SelectBox,
             TestExecutionModel, TestCasesExecutionsModel, ModelHelper, NotificationRegion, DefectIdsCollection,
             MultiSelect, stringUtils) {
    'use strict';
    /*jshint validthis: true */

    return core.Widget.extend({

        View: View,

        init: function (options) {
            this.region = options.region;
            this.eventBus = options.eventBus;
            this.testCasesCollection = options.testCaseCollection;
            this.references = options.references;
            this.testPlanId = options.testPlanId;
            this.testExecutionModel = new TestExecutionModel();
            this.testCasesExecutionsModel = new TestCasesExecutionsModel();
            this.prevLatestExecutions = {};
            this.isoCollection = options.isoCollection;

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCase.testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (modelObj) {
                            return '#' + Navigation.getTestCaseVersionUrl(
                                    modelObj.testCase.id,
                                    modelObj.testCase.sequenceNumber
                                );
                        }
                    }),
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Title',
                    attribute: 'testCase.title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: '',
                    attribute: 'result',
                    cellType: CellFactory.colored({
                        replaceMap: Constants.executionStatusMap,
                        defaultValue: Constants.executionStatusMap['']
                    }),
                    sortable: true,
                    width: '18px'
                }
            ];
        },

        onViewReady: function () {
            this.view.afterRender();

            createTestCasesTable.call(this);
            createResultSelectBox.call(this);
            createIsoSelect.call(this);
            createDefectIdSelect.call(this);

            this.view.getClearButton().addEventHandler('click', this.clearSelection, this);
            this.view.getCreateButton().addEventHandler('click', this.saveExecutions, this);

            this.testCaseTable.attachTo(this.view.getTableHolder());
            this.testCaseTable.setData(this.testCasesCollection.toJSON());

            getPreviousTestExecutions.call(this);
        },

        saveExecutions: function () {
            var selected = getSelectedRows.call(this);
            var result = this.selectBox.getValue();
            var iso = this.isoSelect.getValue();
            var defects = getArrayFromMultiSelect.call(this, this.defectIdMultiSelect.getSelectedItems());
            var warningOptions = getWarningOptions.call(this);

            if (!result.itemObj) {
                this.notify('Please select an execution result', warningOptions);
            } else if (selected.length > 0) {
                var models = createExecutionModels.call(this, selected, result, iso, defects);
                saveExecutions.call(this, models);
            } else {
                this.notify('No Test Cases were selected', warningOptions);
            }
        },

        clearSelection: function () {
            this.testCaseTable.unselectAllRows();
        },

        setTableData: function (data) {
            this.testCaseTable.setData(data);
        },

        notify: function (text, options) {
            var localNotification = NotificationRegion.create(text, options);
            localNotification.attachTo(this.view.getNotificationHolder());
        },

        updateIsoSelect: function (isos) {
            this.isoSelect.setItems(isos);
        },

        showIsoSelect: function () {
            this.view.showIsoSelect();
        },

        hideIsoSelect: function () {
            this.view.hideIsoSelect();
        },

        RESULT_DEFAULT_VALUE: {name: 'Select result...', title: 'Select result...'}

    });

    function createTestCasesTable () {
        this.testCaseTable = new Table({
            plugins: [
                new ColorBand({
                    color: function (row) {
                        return row.getData().rowColor ? Constants.color.GREEN : Constants.color.WHITE;
                    }
                }),
                new Selection({
                    checkboxes: true,
                    selectableRows: false,
                    multiselect: true,
                    bind: true
                }),
                new IdentifiableTable({
                    attribute: 'id',
                    identifier: 'TMS_TestCampaignExecution_TestExecutionMultiSelectTable'
                }),
                new IdentifiableRows({
                    rowIdentifier: 'testCase.testCaseId'
                })

            ],
            modifiers: [
                {name: 'striped'}
            ],
            tooltips: true,
            columns: this.columns
        });

        this.tableHelper = new TableHelper({
            eventBus: this.eventBus,
            collection: this.testCasesCollection,
            parent: this.view.getTableHolder(),
            table: this.testCaseTable,
            isPaginated: false
        });

        this.tableHelper.applyColumnResize();
    }

    function createResultSelectBox () {
        var statusReference = this.references.getById('executionResult') || {
                getItems: function () {
                }
            };

        this.selectBox = new SelectBox({
            items: statusReference.getItems(),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.selectBox.setValue(this.RESULT_DEFAULT_VALUE);
        this.selectBox.attachTo(this.view.getResultHolder());
        this.selectBox.view.getButton().setAttribute('id',
            'TMS_TestCampaignExecution_TestExecutionMultiSelectWidget-resultSelect');

        this.selectBox.addEventHandler('change', function () {
            var item = this.selectBox.getValue();

            if (item.title === 'Fail') {
                this.view.showDefectSelect();
            } else {
                this.view.hideDefectSelect();
            }
        }.bind(this));
    }

    function createIsoSelect () {
        this.isoSelect = new SelectBox({
            items: this.isoCollection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.isoSelect.attachTo(this.view.getIsoSelectHolder());
    }

    function getWarningOptions () {
        var warningOptions = NotificationRegion.NOTIFICATION_TYPES.warning;
        warningOptions.canDismiss = true;
        warningOptions.canClose = true;

        return warningOptions;
    }

    function getSuccessOptions () {
        var options = NotificationRegion.NOTIFICATION_TYPES.success;
        options.canDismiss = true;
        options.canClose = true;

        return options;
    }

    function createExecutionModels (selected, result, iso, defects) {
        var newExecutions = [];

        selected.forEach(function (selection) {
            var newExecution = {};
            var testCaseModel = this.testCasesCollection.getModel(selection.id);
            var model = this.prevLatestExecutions[selection.id];
            if (model) {
                newExecution = createModel.call(this, model, model.testCase,
                    this.testPlanId, result.itemObj, iso.itemObj, defects);
            } else {
                newExecution = createModel.call(this, testCaseModel.toJSON(), selection.testCase.id, this.testPlanId,
                    result.itemObj, iso.itemObj, defects);
            }
            newExecutions.push(newExecution.toJSON());
        }.bind(this));

        return newExecutions;
    }

    function saveExecutions (models) {
        this.testCasesExecutionsModel.save({}, {
            data: JSON.stringify(models),
            contentType: 'application/json',
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                201: function () {
                    this.notify('Executions created successfully', getSuccessOptions.call(this));
                    this.testCasesCollection.setTestPlanId(this.testPlanId);
                    this.testCasesCollection.fetch({
                        reset: true,
                        data: {
                            view: 'detailed'
                        }
                    });
                }.bind(this)
            })
        });
    }

    function createModel (data, testCaseId, testPlanId, result, iso, defects) {
        var model = new TestExecutionModel();
        model.setTestCaseId(testCaseId);
        model.setTestPlanId(testPlanId);
        model.setExecutionResult(result);
        model.setComment(data.testCase.comment ? data.testCase.comment : null);
        model.setExecutionTime(data.executionTime ? data.executionTime : null);
        if (result.title === 'Pass') {
            model.setDefectIds([]);
            model.setRequirementIds([]);
        } else {
            if (defects.length > 0) {
                model.setDefectIds(defects);
            } else {
                model.setDefectIds(data.defectIds);
            }
            model.setRequirementIds(data.requirementIds);
        }
        var testStepExecutions = data.testStepExecutions;
        var verifyStepExecutions = data.verifyStepExecutions;
        if (testStepExecutions) {
            testStepExecutions.forEach(function (testStep) {
                testStep.id = null;
            });
            model.setTestStepExecutions(testStepExecutions);
        }
        if (verifyStepExecutions) {
            verifyStepExecutions.forEach(function (verifyStep) {
                verifyStep.id = null;
            });
            model.setVerifyStepExecutions(verifyStepExecutions);
        }
        if (iso) {
            model.setIso(iso);
        }
        return model;
    }

    function getPreviousTestExecutions () {
        this.testCasesExecutionsModel = new TestCasesExecutionsModel();
        this.testCasesExecutionsModel.setTestPlanId(this.testPlanId);

        this.testCasesExecutionsModel.fetch({
            data: {
                view: 'detailed'
            },
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function (data) {
                    this.prevLatestExecutions = {};
                    data.forEach(function (fetchedData) {
                        if (fetchedData) {
                            this.prevLatestExecutions[fetchedData.testCase] = fetchedData;
                        }
                    }.bind(this));
                }.bind(this)
            })
        });
    }

    function getSelectedRows () {
        var checkedRows = this.testCaseTable.getCheckedRows(),
            checkedObjects = [];

        checkedRows.forEach(function (checkedRow) {
            var objectData = checkedRow.getData();
            objectData._rowIndex = checkedRow.getIndex();
            checkedObjects.push(objectData);
        });
        return checkedObjects;
    }

    function createDefectIdSelect () {
        var collection = new DefectIdsCollection();
        this.defectIdMultiSelect = new MultiSelect({
            items: collection,
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        this.defectIdMultiSelect.attachTo(this.view.getDefectIdMultiSelect());
        this.defectIdMultiSelect.addEventHandler(MultiSelect.SEARCH_CHANGED, function (searchValue) {
            searchValue = stringUtils.trim(searchValue);
            collection.setSearchValue(searchValue);
            collection.fetch({reset: true});
            this.defectIdMultiSelect.prepareComponentList();
        }, this);

        this.view.hideDefectSelect();
    }

    function getArrayFromMultiSelect (objectsArray) {
        var values = [];
        if (objectsArray && objectsArray.length > 0) {
            objectsArray.forEach(function (obj) {
                values.push(obj.value);
            });
        }
        return values;
    }

});
