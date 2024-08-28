define([
    'jscore/core',
    'tablelib/Table',
    '../../../../../common/table/TableHelper',
    '../../../../../common/table/filters/StringFilterCell/StringFilterCell',
    'widgets/Pagination',
    './ReviewInboxAssignmentsWidgetView',
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

        LS_COLUMNS_WIDTHS: 'ReviewInbox-reviews-colWidths',

        View: View,

        init: function () {
            this.collection = this.options.collection;
            this.region = this.options.region;

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (model) {
                            return '#' + Navigation.getTestCaseDetailsUrl(model.testCaseId);
                        }
                    }),
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Title',
                    attribute: 'title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Version',
                    attribute: 'version',
                    cellType: CellFactory.object(),
                    sortable: true
                },
                {
                    title: 'Review Group',
                    attribute: 'reviewGroup.name',
                    cellType: CellFactory.object(),
                    sortable: true
                },
                {
                    title: 'Reviewer',
                    attribute: 'reviewUser.userName',
                    cellType: CellFactory.object(),
                    sortable: true
                }
            ];
        },

        onViewReady: function () {
            createReviewTable.call(this);
            setupReviewTable.call(this);
        },

        refreshTableInfo: function () {
            this.tableHelper.increaseRequestsCount();
        }

    });

    function createReviewTable () {
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);

        this.reviewTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                tableId: 'TMS_ReviewInbox_ReviewTable'
            })
        });

        this.collection.addEventHandler('reset', function (collection) {
            this.reviewTable.setData(collection.toJSON());
        }, this);

        this.reviewTable.addEventHandler('columnresize', function (column) {
            TableHelper.updateColumnWidth(this.LS_COLUMNS_WIDTHS, column);
        }, this);
    }

    function setupReviewTable () {
        this.reviewTable.attachTo(this.getElement());

        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.getElement(),
            table: this.reviewTable,
            isPaginated: true
        });
        this.tableHelper.applyColumnResize();
        this.tableHelper.applySortAndFilter();
        this.tableHelper.applyCollectionReset();
    }

});
