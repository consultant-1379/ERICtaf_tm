define([
    'jscore/core',
    'tablelib/Table',
    '../../../../../common/table/TableHelper',
    '../../../../../common/table/filters/StringFilterCell/StringFilterCell',
    'widgets/Pagination',
    './UserInboxAssignmentsWidgetView',
    '../../../../../common/cells/LinkListCell/LinkListCell',
    'widgets/table/CheckboxHeaderCell',
    'widgets/table/CheckboxCell',
    '../../../../../common/Constants',
    '../../../../../common/ModelHelper',
    '../../../../../common/cells/CellFactory',
    '../../../../../common/Navigation'
], function (core, Table, TableHelper, StringFilterCell, Pagination, View, LinkListCell, CheckboxHeaderCell,
             CheckboxCell, Constants, ModelHelper, CellFactory, Navigation) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        LS_COLUMNS_WIDTHS: 'UserInbox-Assignments-colWidths',

        View: View,

        init: function () {
            this.collection = this.options.assignmentsCollection;
            this.region = this.options.region;

            this.columns = [
                {
                    title: 'Assignment ID',
                    attribute: 'id',
                    width: '130px',
                    sortable: true
                },
                {
                    title: 'Test Campaign',
                    attribute: 'testCampaign.title',
                    secondHeaderCellType: StringFilterCell,
                    cellType: new CellFactory.link({
                        isObject: true,
                        url: function (model) {
                            return '#' + Navigation.getTestPlanDetailsUrl(model.testCampaign.id);
                        }
                    }),
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Test Case ID',
                    attribute: 'testCase.testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (model) {
                            return '#' + Navigation.getTestCaseDetailsUrl(model.testCase.id);
                        }
                    }),
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
                    title: 'Version',
                    attribute: 'testCase.version',
                    cellType: CellFactory.object(),
                    width: '90px',
                    sortable: true
                }
            ];
        },

        onViewReady: function () {
            createAssignmentsTable.call(this);
            setupAssignmentsTable.call(this);
        },

        refreshTableInfo: function () {
            this.tableHelper.increaseRequestsCount();
        }

    });

    function createAssignmentsTable () {
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);

        this.assignmentsTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                tableId: 'TMS_UserInbox_AssignmentsTable'
            })
        });

        this.collection.addEventHandler('reset', function (collection) {
            this.assignmentsTable.setData(collection.toJSON());
        }, this);

        this.assignmentsTable.addEventHandler('columnresize', function (column) {
            TableHelper.updateColumnWidth(this.LS_COLUMNS_WIDTHS, column);
        }, this);
    }

    function setupAssignmentsTable () {
        this.assignmentsTable.attachTo(this.getElement());

        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.getElement(),
            table: this.assignmentsTable,
            isPaginated: true
        });
        this.tableHelper.applyColumnResize();
        this.tableHelper.applySortAndFilter();
        this.tableHelper.applyCollectionReset();
    }

});
