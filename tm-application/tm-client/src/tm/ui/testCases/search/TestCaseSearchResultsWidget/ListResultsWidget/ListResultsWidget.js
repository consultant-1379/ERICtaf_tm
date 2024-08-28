define([
    'jscore/core',
    'tablelib/Table',
    'tablelib/plugins/Selection',
    '../../../../../common/table/TableHelper',
    '../../../../../common/table/plugins/IdentifiableRows/IdentifiableRows',
    '../../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../../../common/cells/LinkListCell/LinkListCell',
    'widgets/table/CheckboxHeaderCell',
    'widgets/table/CheckboxCell',
    'widgets/Pagination',
    './ListResultsWidgetView',
    '../../../../../common/Constants',
    '../../../../../common/ModelHelper',
    '../../../../../common/cells/CellFactory',
    '../../../../../common/Navigation',
    '../../../../../common/requirement/pill/RequirementPillsArrayWidget'
], function (core, Table, Selection, TableHelper, IdentifiableRows, StringFilterCell, LinkListCell,
             CheckboxHeaderCell, CheckboxCell, Pagination, ListResultsWidgetView, Constants, ModelHelper,
             CellFactory, Navigation, RequirementPillsArrayWidget) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        LS_COLUMNS_WIDTHS: 'TestCases-colWidths',

        View: ListResultsWidgetView,

        init: function (options) {
            this.itemsCount = 0;

            this.collection = options.collection;
            this.region = options.region;
            this.eventBus = this.region.getEventBus();

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (rowObj) {
                            return '#' + Navigation.getTestCaseDetailsUrl(rowObj.testCaseId);
                        }
                    }),
                    width: '200px',
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Requirement IDs',
                    attribute: 'requirements',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.widget({
                        widget: RequirementPillsArrayWidget
                    }),
                    width: '200px',
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Title',
                    attribute: 'title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Description',
                    attribute: 'description',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Type',
                    attribute: 'type.title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Groups',
                    attribute: 'groups.title',
                    cellType: CellFactory.arrayCell({
                        searchAttribute: 'title',
                        collectionAttribute: 'groups'}
                    ),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                }
            ];
        },

        onViewReady: function () {
            createTestCasesTable.call(this);
            setupTestCasesTable.call(this);
        },

        refreshTableInfo: function () {
            this.tableHelper.increaseRequestsCount();
        }

    });

    function createTestCasesTable () {
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);

        this.testCasesTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                    tableId: 'TMS_TestCaseSearch_ListResultsTable'
                },
                [
                    new Selection({
                        checkboxes: true,
                        selectableRows: true,
                        multiselect: true,
                        bind: true
                    }),
                    new IdentifiableRows({
                        rowIdentifier: 'testCaseId'
                    })
                ])
        });

        this.testCasesTable.addEventHandler('checkend', function (checkedRows) {
            this.region.updateSelection(checkedRows);
        }, this);
    }

    function setupTestCasesTable () {
        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.getElement(),
            table: this.testCasesTable,
            filtersChangedEvent: Constants.events.TEST_CASE_SEARCH_FILTER_CHANGED,
            data: {
                view: 'simple-requirements'
            },
            isPaginated: true
        });
        this.tableHelper.applyColumnResize();
        this.tableHelper.applySortAndFilter();
        this.tableHelper.applyCollectionReset();

        this.testCasesTable.attachTo(this.getElement());
    }

});
