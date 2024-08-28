/*global define*/
define([
    'jscore/core',
    './TestCaseSearchResultsWidgetView',
    './ListResultsWidget/ListResultsWidget',
    '../../../../common/widgets/actionLink/ActionLink',
    '../TestCaseReportsWidget/TestCaseReportsWidget',
    '../../../../common/Constants',
    'container/api',
    'widgets/Dialog',
    '../../../../common/inputWidget/InputWidget',
    'tablelib/Table',
    '../../../../common/cells/CellFactory',
    '../../../../common/ContextFilter'
], function (core, View, ListResultsWidget, ActionLink, TestCaseReportsWidget, Constants, containerApi, Dialog,
             InputWidget, Table, CellFactory, ContextFilter) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.eventBus = this.options.eventBus;
            this.region = this.options.region;
            this.testCasesCollection = this.options.testCasesCollection;
            this.listResults = new ListResultsWidget({
                collection: this.testCasesCollection,
                region: this.region
            });
            this.listResults.attachTo(this.view.getContentBlock());

            initAddToTestPlanActionLink.call(this);
            initSaveSearchActionLinkActionLink.call(this);
            initSaveSearchActionMenuActionLink.call(this);
            initGenerateReportActionLinkActionLink.call(this);

            this.testCasesCollection.addEventHandler('reset', this.onTestCollectionReset, this);

            this.eventBus.subscribe(Constants.events.UPDATE_SAVED_SEARCH, this.onSavedSearchUpdate, this);

            this.items = [
                {
                    name: 'Create General Acceptance Test (GAT) Report (.docx)',
                    action: function () {
                        this.region.requestTestCaseReport(Constants.report.TestExecution);
                    }.bind(this)
                },
                {
                    name: 'Create Test Specification Report (.docx)',
                    action: function () {
                        this.region.requestTestCaseReport(Constants.report.TestCase);
                    }.bind(this)
                },
                {
                    name: 'Create Report that can be re-imported (.xlsx)',
                    action: function () {
                        this.region.requestTestCaseExportReport();
                    }.bind(this)
                }
            ];

            this.searchTable = new Table({
                tooltips: true,
                modifiers: [
                    {name: 'striped'}
                ],
                columns: [
                    {
                        title: 'Name',
                        attribute: 'name',
                        cellType: CellFactory.link({
                            isObject: true,
                            action: this.onRunSearch.bind(this)
                        }),
                        width: '110px'
                    },
                    {
                        title: 'Query',
                        attribute: 'query'
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
                ]
            });
        },

        onExportLinkClick: function (e) {
            e.preventDefault();
            var message = ContextFilter.searchQuery;
                if (!(message.includes('component'))) {
                     var optionsDialog = new Dialog({
                        header: 'Do not Generate Report',
                        type: 'warning',
                        content: 'Please select Feature and Component',
                        buttons: [
                            {
                                caption: 'Ok',
                                action: function () {
                                    optionsDialog.hide();
                                }
                            }
                        ]
                });
           optionsDialog.show();
          } else {
                var createReport = new TestCaseReportsWidget({
                region: this.region,
                items: this.items
            });

            containerApi.getEventBus().publish('flyout:show', {
            header: 'Generate Reports',
            content: createReport
            });
        }
        },

        onSearchMenuClick: function (e) {
            e.preventDefault();
            if (ContextFilter.profileSearch) {
                this.searchTable.setData(ContextFilter.profileSearch);
            }

            containerApi.getEventBus().publish('flyout:show', {
                header: 'Saved Searches',
                content: this.searchTable
            });
        },

        onRunSearch: function (data) {
            this.eventBus.publish(Constants.events.UPDATE_URL_WITH_SEARCH, data.query, false);
            this.eventBus.publish(Constants.events.CLEAR_SEARCH_INPUT);
        },

        onRemoveCellAction: function (model) {
            if (model) {
                this.region.removeSearch(model);
            }
        },

        onSaveSearchLinkClick: function () {
            var inputWidget = new InputWidget({
                label: 'Name',
                id: 'SaveSearchName'
            });

            var dialog = new Dialog({
                header: 'Save Search',
                type: 'information',
                content: inputWidget,
                buttons: [
                    {
                        caption: 'Save',
                        color: 'green',
                        action: function () {
                            var name = inputWidget.getValue().trim();
                            this.eventBus.publish(Constants.events.SAVE_SAVED_SEARCH, {name: name});
                            dialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'Cancel',
                        action: function () {
                            dialog.hide();
                        }
                    }
                ]
            });

            dialog.show();
        },

        onAddToTestPlanLinkClick: function (event) {
            event.preventDefault();
            this.region.requestAddToTestPlan();
        },

        onSavedSearchUpdate: function () {
            if (ContextFilter.profileSearch) {
                this.searchTable.setData(ContextFilter.profileSearch);
            }
        },

        refreshDataInfo: function () {
            this.listResults.refreshTableInfo();
        },

        onTestCollectionReset: function () {
            this.updateTotalCount();
            initGenerateReportActionLinkActionLink.call(this);
        },

        updateTotalCount: function () {
             var totalCount = this.testCasesCollection.getTotalCount().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + totalCount + ')');
        }

    });

    function initAddToTestPlanActionLink () {
        this.addToTestPlanActionLink = new ActionLink({
            icon: {iconKey: 'attach', interactive: true, title: 'Add to Test Campaign'},
            link: {text: 'Add to Test Campaign'},
            action: this.onAddToTestPlanLinkClick.bind(this)
        });
        this.addToTestPlanActionLink.attachTo(this.view.getAddToTestPlanLinkHolder());
    }

    function initSaveSearchActionLinkActionLink () {
        this.saveSearchActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Save Search'},
            link: {text: 'Save Search'},
            action: this.onSaveSearchLinkClick.bind(this)
        });
        this.saveSearchActionLink.attachTo(this.view.getSaveSearchLinkHolder());
    }

    function initSaveSearchActionMenuActionLink () {
        this.saveSearchActionLink = new ActionLink({
            icon: {iconKey: 'menu', interactive: true, title: 'Search Menu'},
            link: {text: 'Search Menu'},
            action: this.onSearchMenuClick.bind(this)
        });
        this.saveSearchActionLink.attachTo(this.view.getSaveSearchMenuHolder());
    }

    function initGenerateReportActionLinkActionLink () {
        if (!this.generateReportActionLink) {
            this.generateReportActionLink = new ActionLink({
                icon: {iconKey: 'export', interactive: true, title: 'Generate Report'},
                link: {text: 'Generate Report'},
                action: this.onExportLinkClick.bind(this)
            });
        }
        var totalCount = this.testCasesCollection.getTotalCount();
        if (totalCount !== 0) {
            if (ContextFilter.searchQuery.length === 0 && ContextFilter.profileProduct === null) {
                this.generateReportActionLink.setHidden(true);
            } else {
                this.generateReportActionLink.setHidden(false);
            }
        } else {
            this.generateReportActionLink.setHidden(true);
        }
        this.generateReportActionLink.attachTo(this.view.getExportLinkHolder());
    }

});
