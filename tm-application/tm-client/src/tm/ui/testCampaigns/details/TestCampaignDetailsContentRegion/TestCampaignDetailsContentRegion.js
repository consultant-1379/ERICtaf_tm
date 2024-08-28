/*global define*/
define([
    'jscore/core',
    'jscore/ext/net',
    'widgets/Accordion',
    './TestCampaignDetailsContentRegionView',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../../common/cells/CellFactory',
    '../../../../common/DeleteDialog/DeleteDialog',
    '../TestCampaignLockDialog/TestCampaignLockDialog',
    'tablelib/Table',
    'tablelib/TableSettings',
    'tablelib/plugins/Selection',
    'tablelib/plugins/FixedHeader',
    '../../../../common/table/TableHelper',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../models/TestCampaignModel',
    '../../models/AssignmentsCollection',
    '../TestCampaignDetailsWidget/TestCampaignDetailsWidget',
    'widgets/Dialog',
    '../../../../common/widgets/StackedProgressBar/StackedProgressBar',
    '../../../../common/TestExecutionHelper'
], function (core, net, Accordion, View, Constants, ContextFilter, Animation, Navigation, ModelHelper, CellFactory, DeleteDialog,
             TestPlanLockDialog, Table, TableSettings, Selection, FixedHeader, TableHelper, StringFilterCell, TestPlan, Assignments, DetailsWidget,
             Dialog, StackedProgressBar, TestExecutionHelper) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        LS_COLUMNS_WIDTHS: 'TP-TestCases-colWidths',

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.testPlan = new TestPlan();
            this.testCases = new Assignments();
            this.settingsIsShown = false;

            this.columns = [
                {
                    title: 'Test Case ID',
                    attribute: 'testCase.testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (rowObj) {
                            return '#' + Navigation.getTestCaseVersionUrl(
                                    rowObj.testCase.testCaseId.trim(),
                                    rowObj.testCase.version
                                );
                        }
                    }),
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
                    resizable: true,
                    sortable: true

                },
                {
                    title: 'Component',
                    attribute: 'testCase.technicalComponents.title',
                    cellType: CellFactory.arrayCell({
                            searchAttribute: 'title',
                            collectionAttribute: 'testCase.technicalComponents'
                        }
                    ),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true,
                    visible: false
                },
                {
                    title: 'Version',
                    attribute: 'testCase.version',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Assigned',
                    attribute: 'user.userName',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Test Type',
                    attribute: 'testCase.type.title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true
                },
                {
                    title: 'Groups',
                    attribute: 'testCase.groups.title',
                    cellType: CellFactory.arrayCell({
                            searchAttribute: 'title',
                            collectionAttribute: 'testCase.groups'
                        }
                    ),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true,
                    visible: false
                },
                {
                    title: 'Execution Type',
                    attribute: 'testCase.executionType.title',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    resizable: true,
                    sortable: true,
                    visible: false
                }
            ];
        },

        onViewReady: function () {
            this.view.afterRender();

            createTestCasesTable.call(this);
            createStackedProgressBar.call(this);

            this.eventBus.subscribe(Constants.events.DELETE_TEST_PLAN, this.onDeleteTestPlanRequest, this);
            this.eventBus.subscribe(Constants.events.LOCK_TEST_PLAN, this.onTestPlanLockRequest, this);

            var animatedRefresh = function (params) {
                this.refreshTestExecution(params.itemId);
            }.bind(this);
            this.view.getSettingsButton().addEventHandler('click', this.onSettingsButtonClick, this);
            this.view.getHideSettingsButton().addEventHandler('click', this.onSettingsButtonClick, this);
            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_TEST_PLAN_DETAILS, animatedRefresh.bind(this));
            this.animation.hideOn(Constants.events.HIDE_TEST_PLAN_DETAILS);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_TEST_PLAN_DETAILS, animatedRefresh.bind(this));

            this.detailsWidget = new DetailsWidget({
                region: this,
                model: this.testPlan
            });
            this.detailsWidget.attachTo(this.view.getDetailsBlock());

            this.accordion = new Accordion({
                title: 'Test Campaign Details',
                content: this.detailsWidget
            });
            this.accordion.attachTo(this.view.getDetailsBlock());
            this.accordion.trigger('expand');

            setupTestCasesTable.call(this);

            initDeleteDialog.call(this);
            initLockDialog.call(this);
            initUnlockDialog.call(this);

            this.testExecutionHelper = new TestExecutionHelper();

            this.testCasesTable.addEventHandler('localFilter', this.updateTotalCount, this);
        },

        refreshTestExecution: function (id) {
            this.testPlan.setId(id);
            hideSettings.call(this);
            this.testPlan.fetch({
                data: {
                    view: 'detailed'
                },
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.eventBus.publish(Constants.events.CHANGE_HEADING_TITLE, this.testPlan.getName());
                        this.tableHelper.increaseRequestsCount();

                        var models = this.testPlan.getTestPlanItems() || [];
                        this.testCases.setModels(models, {reset: true});
                        this.testCasesTable.setData(models);
                        this.eventBus.publish(Constants.events.TEST_PLAN_LOCKED, this.testPlan.getLocked());
                        var items = this.testExecutionHelper.calculateTestExecutions(models);
                        this.stackedProgressBar.setItems(this.testExecutionHelper.convertToList(items));

                        this.tableHelper.decreaseRequestsCount();
                        this.view.getTotalCount().setText('(' + models.length + ')');
                    }.bind(this),
                    404: function () {
                        this.missingTestPlanDialog = new Dialog({
                            header: 'Test Campaign does not exist',
                            type: 'error',
                            content: ' ',
                            buttons: [
                                {
                                    caption: 'OK',
                                    color: 'darkBlue',
                                    action: function () {
                                        this.missingTestPlanDialog.destroy();
                                        Navigation.goToPreviousPage();
                                    }.bind(this)
                                }
                            ]
                        });
                        this.missingTestPlanDialog.show();
                    }.bind(this)
                })
            });
        },

        onDeleteTestPlanRequest: function () {
            this.deleteDialog.show();
        },

        deleteTestPlan: function () {
            this.testPlan.destroy({
                success: function () {
                    Navigation.navigateTo(Navigation.getTestPlansListUrlWithParams(ContextFilter.projectIdParam));
                }.bind(this)
            });
        },

        onTestPlanLockRequest: function () {
            if (!this.testPlan.getLocked()) {
                this.lockDialog.show();
            } else {
                this.unlockDialog.show();
            }
        },

        changeLockState: function () {
            var isLocked = this.testPlan.getLocked();
            net.ajax({
                url: '/tm-server/api/test-campaigns/' + this.testPlan.getId() + '/status',
                type: 'PUT',
                data: JSON.stringify({locked: !isLocked}),
                dataType: 'json',
                contentType: 'application/json',
                statusCode: {
                    200: function () {
                        this.testPlan.setLocked(!isLocked);
                        this.eventBus.publish(Constants.events.TEST_PLAN_LOCKED, this.testPlan.getLocked());
                    }.bind(this)
                }
            });
        },

        updateTotalCount: function (collection) {
            var count = collection.toJSON().length.toString();
            var formattedCount = count.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + formattedCount + ')');
        },

        onSettingsButtonClick: function () {
            if (this.settingsIsShown) {
                hideSettings.call(this);
            } else {
                showSettings.call(this);
            }
        }

    });

    function initLockDialog () {
        this.lockDialog = new TestPlanLockDialog({
            type: 'lock',
            action: function () {
                this.changeLockState();
            }.bind(this)
        });
    }

    function initUnlockDialog () {
        this.unlockDialog = new TestPlanLockDialog({
            type: 'unlock',
            action: function () {
                this.changeLockState();
            }.bind(this)
        });
    }

    function initDeleteDialog () {
        this.deleteDialog = new DeleteDialog({
            item: 'Test Campaign',
            action: function () {
                this.deleteTestPlan();
                this.deleteDialog.hide();
            }.bind(this)
        });
    }

    function showSettings () {
        this.view.getSlidingPanels().setModifier('settings');
        this.view.getSettingsButton().setModifier('hidden');
        this.view.getHideSettingsButton().removeModifier('hidden');
        this.settingsIsShown = true;
    }

    function hideSettings () {
        this.view.getSlidingPanels().removeModifier('settings');
        this.view.getHideSettingsButton().setModifier('hidden');
        this.view.getSettingsButton().removeModifier('hidden');
        this.settingsIsShown = false;
    }

    function createTestCasesTable (newColumns) {
        var columns = this.columns;
        if (newColumns) {
            columns = newColumns;
        }
        if (this.testCasesTable) {
            this.testCasesTable.destroy();
        }
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);

        this.testCasesTable = new Table({
            tooltips: true,
            columns: columns,
            modifiers: [
                {
                    name: 'striped'
                }
            ],
            plugins: TableHelper.plugins({
                    tableId: 'TMS_TestCampaignDetails_testCaseTable',
                    tableRowAttribute: 'testCase.testCaseId'
                }, new FixedHeader({
                    maxHeight: '300px'
                }),
                new Selection({
                    checkboxes: false,
                    selectableRows: true,
                    multiselect: false,
                    bind: false
                }))
        });
        return this.testCasesTable;
    }

    function setupTestCasesTable () {
        this.tableSettings = new TableSettings({
            columns: this.columns
        });
        this.tableSettings.attachTo(this.view.getSettingsContent());

        this.tableSettings.addEventHandler('change', function () {
            this.testCasesTable.destroy();
            this.testCasesTable = createTestCasesTable.call(this, this.tableSettings.getUpdatedColumns());
            this.testCasesTable.setData(this.testCases.toJSON());
            applyTableSettings.call(this);
        }.bind(this));

        applyTableSettings.call(this);
    }

    function applyTableSettings () {
        this.testCasesTable.attachTo(this.view.getTestCaseList());

        this.tableHelper = new TableHelper({
            localStorageNamespace: this.LS_COLUMNS_WIDTHS,
            collection: this.testCases,
            table: this.testCasesTable,
            isPaginated: false
        });

        this.tableHelper.applyColumnResize();
        this.tableHelper.applyLocalFilter();
        this.tableHelper.applyLocalCollectionReset();
        this.tableHelper.applyLocalSort();

    }

    function createStackedProgressBar () {
        this.stackedProgressBar = new StackedProgressBar();
        this.stackedProgressBar.attachTo(this.view.getStackedProgressBarHolder());
        this.stackedProgressBar.setWidth('12rem');
    }

});
