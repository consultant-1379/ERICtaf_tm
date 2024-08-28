define([
    'jscore/core',
    'tablelib/Table',
    '../../../../common/table/TableHelper',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../../common/cells/CellFactory',
    '../../../../common/Navigation',
    '../../../../common/DateHelper',
    './TestCampaignListWidgetView',
    '../../../../common/Constants',
    '../../../../common/widgets/TableProgressBar/TableProgressBar'
], function (core, Table, TableHelper, StringFilterCell, CellFactory, Navigation, DateHelper, View, Constants,
             TableProgressBar) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        LS_COLUMNS_WIDTHS: 'TestPlans-colWidths',

        View: View,

        init: function (options) {
            this.region = options.region;
            this.eventBus = this.region.getEventBus();
            this.collection = options.collection;

            this.collection.addEventHandler('reset', this.updateTotalCount, this);

            this.columns = [
                {
                    title: 'Name',
                    attribute: 'name',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: false,
                        url: function (model) {
                            return '#' + Navigation.getTestPlanDetailsUrl(model.id);
                        }
                    }),
                    width: '150px',
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Description',
                    attribute: 'description',
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Environment',
                    attribute: 'environment',
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Drop',
                    cellType: CellFactory.object(),
                    attribute: 'drop.name',
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Start Date',
                    attribute: 'startDate',
                    cellType: CellFactory.format({
                        mapper: function (timeString) {
                            return DateHelper.formatStringToDate(timeString);
                        }
                    }),
                    resizable: true
                },
                {
                    title: 'End Date',
                    attribute: 'endDate',
                    cellType: CellFactory.format({
                        mapper: function (timeString) {
                            return DateHelper.formatStringToDate(timeString);
                        }
                    }),
                    resizable: true
                },
                {
                    title: 'Pass Rate',
                    attribute: 'testCampaignItems',
                    cellType: CellFactory.widget({
                        widget: TableProgressBar
                    }),
                    width: '140px',
                    resizable: true
                },
                {
                    cellType: CellFactory.icons({
                        icons: [
                            {
                                title: 'Test Campaign Execution',
                                icon: 'play',
                                action: this.onTestPlanExecutionAction.bind(this)
                            },
                            {
                                title: 'Delete',
                                icon: 'delete',
                                action: this.onDeleteCellAction.bind(this)
                            }
                        ]
                    }),
                    width: '70px'
                }
            ];
        },

        onViewReady: function () {
            createTestPlansTable.call(this);
            setupTestPlansTable.call(this);
        },

        onDOMAttach: function () {
            this.region.refreshTestPlans();
        },

        onDeleteCellAction: function (model) {
            this.region.deleteTestPlanDialog(model.id);
        },

        onTestPlanExecutionAction: function (model) {
            this.region.openTestPlanExecution(model.id);
        },

        refreshTableInfo: function () {
            this.tableHelper.increaseRequestsCount();
        },

        updateTotalCount: function () {
            var totalCount = this.collection.getTotalCount().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + totalCount + ')');
        }

    });

    function createTestPlansTable () {
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);

        this.testPlansTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                tableId: 'TMS_TestCampaignList_TestCampaignTable',
                tableRowAttribute: 'name'
            })
        });
    }

    function setupTestPlansTable () {
        this.testPlansTable.attachTo(this.view.getContent());

        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            eventBus: this.eventBus,
            collection: this.collection,
            parent: this.getElement(),
            table: this.testPlansTable,
            isPaginated: true,
            filtersChangedEvent: Constants.events.TEST_CAMPAIGNS_FILTER_CHANGED,
            data: {
                view: 'detailed'
            }
        });
        this.tableHelper.applyColumnResize();
        this.tableHelper.applySortAndFilter();
        this.tableHelper.applyCollectionReset();
    }
});
