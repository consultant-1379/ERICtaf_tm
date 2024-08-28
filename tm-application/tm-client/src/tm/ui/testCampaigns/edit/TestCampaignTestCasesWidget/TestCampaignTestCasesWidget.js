define([
    'jscore/core',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    '../../../../common/table/TableHelper',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    './TestCampaignTestCasesWidgetView',
    '../../../../common/cells/CellFactory',
    '../../../../common/AutocompleteInput/AutocompleteInput',
    '../../../../common/DeleteDialog/DeleteDialog',
    'widgets/table/CheckboxHeaderCell',
    'widgets/table/CheckboxCell',
    '../../../../common/widgets/actionLink/ActionLink',
    '../../../../common/Constants',
    '../../../testCases/search/TestCaseSearchWidget/TestCaseSearchWidget',
    'container/api'
], function (core, Table, Selection, TableHelper, StringFilterCell, View, CellFactory, AutocompleteInput, DeleteDialog,
             CheckboxHeaderCell, CheckboxCell, ActionLink, Constants, TestCaseSearchWidget, containerApi) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.region = options.region;
            this.collection = options.collection;
            this.testCaseSearchWidget = new TestCaseSearchWidget({
                region: this.region,
                collection: this.collection
            });

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCase.testCaseId',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    width: '150px',
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Title',
                    attribute: 'testCase.title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Feature',
                    attribute: 'testCase.feature.name',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Component',
                    attribute: 'testCase.technicalComponents.title',
                    cellType: CellFactory.arrayCell({
                        searchAttribute: 'title',
                        collectionAttribute: 'testCase.technicalComponents'}
                    ),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Version',
                    attribute: 'testCase.version',
                    cellType: CellFactory.versionCell({
                        action: function (testcase, version) {
                            this.updateTestCaseVersion(testcase, version);
                        }.bind(this)
                    }),
                    width: '200px',
                    resizable: true

                },
                {
                    title: 'Assigned',
                    attribute: 'user.userName',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    width: '100px',
                    resizable: true,
                    sortable: true
                },
                {
                    cellType: CellFactory.icons({
                        icons: [{
                            title: 'Delete',
                            icon: 'delete',
                            action: this.onRemoveCellAction.bind(this)
                        }]
                    }),
                    width: '50px'
                }
            ];
        },

        onViewReady: function () {
            createTestCasesTable.call(this);
            setupTestCasesTable.call(this);

            this.collection.addEventHandler('reset', this.onCollectionReset, this);
            this.collection.addEventHandler('add', this.onModelAdd, this);
            this.collection.addEventHandler('remove', this.onModelRemove, this);

            this.autocompleteInput = new AutocompleteInput({
                placeholder: 'Enter Test Case ID',
                completions: this.options.completions,
                refresh: this.options.completionRefresh
            });
            this.autocompleteInput.addEventHandler('submit', this.onTestCaseSubmit, this);
            this.autocompleteInput.attachTo(this.view.getTestCaseInput());

            initAssignToUserActionLink.call(this);
            initUpdateVersionActionLink.call(this);

            this.view.getLinkButton().addEventHandler('click', this.onTestCaseSubmit, this);
            this.view.getSearchButton().addEventHandler('click', this.onShowTestCasesAdvancedSearch, this);

            this.testCaseSearchWidget.addEventHandler('addSelected', this.region.addMultipleTestCases, this.region);

            this.testCasesTable.addEventHandler('localFilter', this.updateTotalCount, this);
        },

        onCollectionReset: function () {
            var data = this.collection.toJSON();
            this.testCasesTable.setData(data);
            this.view.getTotalCount().setText('(' + data.length + ')');
        },

        updateCollection: function (testCases) {
            this.collection = testCases;
            this.onCollectionReset();
        },

        updateTestCaseVersion: function (testcase, version) {
            this.region.updateTestCaseVersion(testcase, version);
        },

        onModelAdd: function (newModel) {
            this.tableHelper.increaseRequestsCount();
            this.testCasesTable.addRow(newModel.toJSON());
            this.tableHelper.decreaseRequestsCount();
        },

        onModelRemove: function (modelToRemove) {
            this.tableHelper.increaseRequestsCount();

            var rowIndexToRemove = null;

            this.testCasesTable.getRows().forEach(function (itemObj, index) {
                if (modelToRemove.getTestCasePk() === itemObj.getData().testCase.id) {
                    rowIndexToRemove = index;
                }
            });
            if (rowIndexToRemove != null) {
                this.testCasesTable.removeRow(rowIndexToRemove);
            }
            this.tableHelper.decreaseRequestsCount();
        },

        onTestCaseSubmit: function () {
            var testCaseId = this.autocompleteInput.getValue();
            this.region.linkTestCase(testCaseId);
            this.autocompleteInput.setValue('');
        },

        onRemoveCellAction: function (model) {
            this.region.removeTestCase(model);
        },

        onAssignToUserActionLinkClick: function (event) {
            event.preventDefault();
            this.region.requestAssignToUserAction();
        },

        onUpdateVersionActionLinkClick: function (event) {
            event.preventDefault();
            this.region.requestUpdateVersionAction();
        },

        clearAssignmentCheckboxes: function () {
            this.testCasesTable.unselectAllRows();
            this.testCasesTable.setData(this.collection.toJSON());
        },

        getSelectedRows: function () {
            var checkedRows = this.testCasesTable.getCheckedRows(),
                checkedObjects = [];

            checkedRows.forEach(function (checkedRow) {
                var objectData = checkedRow.getData();
                objectData._rowIndex = checkedRow.getIndex();
                checkedObjects.push(objectData);
            });
            return checkedObjects;
        },

        onShowTestCasesAdvancedSearch: function () {
            containerApi.getEventBus().publish('flyout:show', {
                header: 'Add Multiple',
                width: '100rem',
                content: this.testCaseSearchWidget
            });
        },

        updateTotalCount: function (collection) {
            var count = collection.toJSON().length.toString();
            var formattedCount = count.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + formattedCount + ')');
        }

    });

    function initAssignToUserActionLink () {
        this.assignToUserActionLink = new ActionLink({
            icon: {iconKey: 'user', interactive: true, title: 'Assign to user'},
            link: {text: 'Assign to user'},
            action: this.onAssignToUserActionLinkClick.bind(this)
        });
        this.assignToUserActionLink.attachTo(this.view.getHeadingCommandsBlock());
    }

    function initUpdateVersionActionLink () {
        this.updateVersionActionLink = new ActionLink({
            icon: {iconKey: 'refresh', interactive: true, title: 'Update version'},
            link: {text: 'Update version'},
            action: this.onUpdateVersionActionLinkClick.bind(this)
        });
        this.updateVersionActionLink.attachTo(this.view.getHeadingCommandsBlock());
    }

    function createTestCasesTable () {
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);
        this.testCasesTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                tableId: 'TMS_TestCampaignList_testCaseTable',
                tableRowAttribute: 'testCase.id'
            }, [
                new Selection({
                    checkboxes: true,
                    selectableRows: true,
                    multiselect: true,
                    bind: true
                })
            ])
        });

        this.testCasesTable.addEventHandler('checkend', function (checkedRows) {
            this.region.updateSelection(checkedRows);
        }, this);
    }

    function setupTestCasesTable () {
        this.testCasesTable.attachTo(this.view.getTestCaseList());

        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            collection: this.collection,
            table: this.testCasesTable,
            isPaginated: false
        });
        this.tableHelper.applyLocalFilter();
        this.tableHelper.applyLocalSort();
    }

});
