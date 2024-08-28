define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'container/api',
    'widgets/Accordion',
    'tablelib/Table',
    'tablelib/TableSettings',
    'tablelib/plugins/Selection',
    '../../../../common/table/plugins/StyledHeaderCell/StyledHeaderCell',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../../common/table/TableHelper',
    './TestCampaignExecutionContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../../common/cells/CellFactory',
    '../../../../common/cells/LinkListCell/LinkListCell',
    '../../../../common/widgets/progressBlock/ProgressBlock',
    '../../../testCampaigns/models/TestCampaignModel',
    '../../../testCampaigns/models/AssignmentsCollection',
    '../../models/TestExecutionsCollection',
    '../../common/widgets/TestCampaignDetailsWidget/TestCampaignDetailsWidget',
    '../TestExecutionWidget/TestExecutionWidget',
    '../../../testCases/search/TestCaseSearchFormWidget/TestCaseSearchFormWidget',
    '../../../../common/requirement/pill/RequirementPillsArrayWidget',
    '../TestExecutionMultiSelectWidget/TestExecutionMultiSelectWidget',
    '../../../../common/models/iso/IsoCollection',
    '../../../../common/widgets/StackedProgressBar/StackedProgressBar',
    '../../../../common/TestExecutionHelper'
], function (core, _, containerApi, Accordion, Table, TableSettings, Selection, StyledHeaderCell, StringFilterCell,
             TableHelper, View, Constants, Animation, Navigation, ModelHelper, CellFactory, LinkListCell, ProgressBlock,
             TestPlan, Assignments, TestExecutions, TestPlanDetails, TestExecutionWidget, TestCaseSearchFormWidget,
             RequirementPillsArrayWidget, TestExecutionMultiSelectWidget, IsoCollection, StackedProgressBar,
             TestExecutionHelper) {

    'use strict';

    var NULL_RESULT_SORT_ORDER = 999;

    return core.Region.extend({
        /* jshint validthis: true */

        LS_COLUMNS_WIDTHS: 'TestExecution-TestCases-colWidths',

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.testPlanId = '';
            this.testPlan = new TestPlan();
            this.testCases = new Assignments();
            this.testExecutions = new TestExecutions();
            this._testCaseExecutions = [];
            this.preventRowClick = false;
            this.onRefreshTestExecutions = _.debounce(this.onRefreshTestExecutions, 100);
            this.TEST_PLAN_CSV = '/test-cases/csv';
            this.TM_SERVER = '/tm-server/api/test-campaigns/'; // TODO: Get from env configuration
            this.FILTER = '?filter=';
            this.searchIsShown = false;
            this.settingsIsShown = false;
            this.filterQuery = '';
            this.references = this.options.references;
            this.currentTestCaseId = '';
            this.isoCollection = new IsoCollection();

            this.columns = [
                {
                    title: 'Result',
                    attribute: 'result',
                    cellType: CellFactory.colored({
                        replaceMap: Constants.executionStatusMap,
                        defaultValue: Constants.executionStatusMap['']
                    }),
                    sortable: true,
                    width: '18px'
                },
                {
                    title: 'Test Case ID',
                    attribute: 'testCase.testCaseId',
                    secondHeaderCellType: StringFilterCell,
                    cellType: CellFactory.link({
                        isObject: true,
                        url: function (modelObj) {
                            return '#' + Navigation.getTestCaseVersionUrl(
                                    modelObj.testCase.testCaseId.trim(),
                                    modelObj.testCase.version
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
                    title: 'Comment',
                    attribute: 'testCase.comment',
                    cellType: CellFactory.object(),
                    secondHeaderCellType: StringFilterCell,
                    sortable: true,
                    resizable: true
                },
                {
                    title: 'Defect IDs',
                    attribute: 'defects',
                    cellType: CellFactory.widget({
                        widget: RequirementPillsArrayWidget
                    }),
                    sortable: true,
                    resizable: true
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
                    title: 'Actions',
                    width: '100px',
                    cellType: CellFactory.button({
                        button: {
                            title: 'Add Test Execution result',
                            label: 'Add Result',
                            color: 'default',
                            action: this.onTestCaseRunCellAction.bind(this)
                        }
                    })
                }
            ];
        },

        onViewReady: function () {
            this.view.afterRender();

            createTestCasesTable.call(this);

            var animatedRefresh = function (params) {
                this.refreshTestExecution(params.itemId);
                this.screenId = params.screenId;
                if (!this.searchExists) {
                    createSearchWidget.call(this, this.screenId);
                    addButtonEventHandlers.call(this);
                }
                this.searchExists = true;
            }.bind(this);
            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_TEST_PLAN_EXECUTION, animatedRefresh.bind(this));
            this.animation.hideOn(Constants.events.HIDE_TEST_PLAN_EXECUTION);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_TEST_PLAN_EXECUTION, animatedRefresh.bind(this));
            this.eventBus.subscribe(Constants.events.REFRESH_TEST_EXECUTIONS, this.onRefreshTestExecutions, this);

            this.testPlanDetails = new TestPlanDetails({
                region: this,
                model: this.testPlan
            });

            this.accordion = new Accordion({
                title: 'Test Campaign Details',
                content: this.testPlanDetails
            });
            this.accordion.attachTo(this.view.getDetailsBlock());
            this.accordion.trigger('expand');

            this.csvExportAll = this.view.getCSVExportAllButton();
            this.csvExportAll.addEventHandler('click', function () {
                this.csvExportAll.setAttribute('href',
                    window.location.origin + this.TM_SERVER + this.testPlanId + this.TEST_PLAN_CSV);
            }.bind(this));

            this.gatExportAll = this.view.getGATExportAllButton();
            this.gatExportAll.addEventHandler('click', function () {
            var productName = this.testPlan.getProductName();
            var featureName = this.testPlan.getFeatureNames();

            var url = this.TM_SERVER + this.testPlanId + '/test-campaigns.docx';
                 url += '?reportType=' + 'TestExecution';
                 url += '&q=' + encodeURIComponent(productName) + encodeURIComponent(featureName);

                    this.gatExportAll.setAttribute('href',
                                          window.location.origin + url);
           }.bind(this));

            this.csvExport = this.view.getCSVExportButton();
            this.csvExport.addEventHandler('click', function () {
                var testCaseIds = [];
                this.testCasesTable.getData().forEach(function (row) {
                    testCaseIds.push(row.testCase.testCaseId);
                });

                this.csvExport.setAttribute('href',
                    window.location.origin +
                    this.TM_SERVER + this.testPlanId + this.TEST_PLAN_CSV + this.FILTER + testCaseIds.join(','));

            }.bind(this));

            this.view.getExecuteMultipleButton().addEventHandler('click', this.showExecuteMultipleFlyout, this);

            setupTestCasesTable.call(this);
            createProgressBlock.call(this);
            createStackedProgressBar.call(this);

            this.testExecutionHelper = new TestExecutionHelper();

            this.testCasesTable.addEventHandler('localFilter', this.updateTotalCount, this);
        },

        refreshTestExecution: function (id) {
            this.setTestPlanId(id);
            this.testExecutions.setTestPlanId(this.testPlanId);

            if (this.filterQuery !== '') {
                this.runSearch(this.filterQuery);
                hideTestCaseExecutions.call(this);
                showAdvancedSearch.call(this);
            } else {
                hideAdvancedSearch.call(this);
                hideSettings.call(this);
                this.testPlan.setId(this.testPlanId);
                updateTableData.call(this);
            }
        },

        onTableRowSelect: function (row, isSelected) {
            if (this.preventRowClick) {
                this.preventRowClick = false;
                return;
            }
            var modelObj = row.getData();
            this.currentTestCaseId = modelObj.testCase.id;

            if (isSelected) {
                showTestCaseExecutions.call(this, this.currentTestCaseId);
                hideAdvancedSearch.call(this);
                hideSettings.call(this);
            } else {
                hideTestCaseExecutions.call(this);
            }
        },

        onFilterButtonClick: function () {
            if (this.searchIsShown) {
                hideAdvancedSearch.call(this);
            } else {
                showAdvancedSearch.call(this);
                hideTestCaseExecutions.call(this);
            }

            if (this.settingsIsShown) {
                hideSettings.call(this);
            }
        },

        onSettingsButtonClick: function () {
            if (this.settingsIsShown) {
                hideSettings.call(this);
            } else {
                showSettings.call(this);
                hideTestCaseExecutions.call(this);
            }
            if (this.searchIsShown) {
                hideAdvancedSearch.call(this);
            }
        },

        onTestCaseRunCellAction: function (modelObj) {
            this.preventRowClick = true;
            Navigation.navigateTo(Navigation.getTestExecutionCreateUrl(this.testPlanId, modelObj.testCase.id));
        },

        onRefreshTestExecutions: function () {
            if (this.testExecutions.getTestCaseId() !== '') {
                destroyTestExecutions.call(this);
                this.progressBlock.showProgress();
                fetchTestExecutions.call(this);
            }
        },

        updateTableData: function () {
            if (this.testCasesTable) {
                this.tableHelper.increaseRequestsCount();
                this.testCasesTable.setData(this.testCases.toJSON());
                this.tableHelper.decreaseRequestsCount();
            }
        },

        runSearch: function (criterionsUrl) {
            this.tableHelper.clearFilterText();
            this.testCases.setTestPlanId(this.testPlanId);
            this.testCases.setSearchQuery(criterionsUrl);
            this.testCases.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        setSortOrder.call(this);
                        this.updateTableData();
                    }.bind(this)
                })
            });
            this.filterQuery = criterionsUrl;
        },

        showExecuteMultipleFlyout: function () {
            this.testExecutionMultiSelectWidget = new TestExecutionMultiSelectWidget({
                testCaseCollection: this.testCases,
                references: this.references,
                testPlanId: this.testPlanId,
                eventBus: this.eventBus
            });

            if (this.testPlan.getProduct().dropCapable && !this.testPlan.getDrop().defaultDrop && !this.testPlan.getProduct().name.includes('Ericsson Orchestrator')) {
                this.testExecutionMultiSelectWidget.showIsoSelect();
                fetchIsos.call(this);
            } else {
                this.isoCollection.reset();
                this.testExecutionMultiSelectWidget.hideIsoSelect();
            }

            containerApi.getEventBus().publish('flyout:show', {
                header: 'Execute Multiple Test Cases',
                width: '100rem',
                content: this.testExecutionMultiSelectWidget
            });

            if (this.tableHelper.isFiltered()) {
                this.testExecutionMultiSelectWidget.setTableData(this.testCasesTable.getData());
            }

        },

        setTestPlanId: function (id) {
            if (this.testPlanId !== id) {
                this.testPlanId = id;
                this.filterQuery = '';
                if (this.searchWidget) {
                    this.searchWidget.clearCriteria();
                }
            }
        },

        setStackedProgressBar: function (items) {
            this.setStackedProgressBar.setItems(items);
        },

        updateTotalCount: function (collection) {
            var count = collection.toJSON().length.toString();
            var formattedCount = count.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + formattedCount + ')');
        }

    });

    function updateTableData () {
        this.testPlan.fetch({
            data: {
                view: 'detailed'
            },
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    this.eventBus.publish(Constants.events.CHANGE_HEADING_TITLE, this.testPlan.getName());
                    var models = this.testPlan.getTestPlanItems();
                    this.testCases.setModels(models);
                    setSortOrder.call(this);
                    if (this.tableHelper.isFiltered()) {
                        this.tableHelper.setDataWithFilter(this.testCases);
                        showTestCaseExecutions.call(this, this.currentTestCaseId);
                    } else {
                        this.updateTableData();
                        if (this.currentTestCaseId > 0) {
                            showTestCaseExecutions.call(this, this.currentTestCaseId);
                        } else {
                            hideTestCaseExecutions.call(this);
                        }
                    }
                    this.testPlanDetails.updateUnboundParams();

                    var items = this.testExecutionHelper.calculateTestExecutions(models);

                    this.stackedProgressBar.setItems(this.testExecutionHelper.convertToList(items));
                    this.view.getTotalCount().setText('(' + models.length + ')');
                }.bind(this)
            })
        });
    }

    function fetchIsos () {
        this.isoCollection.setDropId(this.testPlan.getDropId());
        this.isoCollection.fetch({
            success: function (data) {
                this.testExecutionMultiSelectWidget.updateIsoSelect(data.toJSON());
            }.bind(this)
        });
    }

    function updateTestCaseResultAttribute () {
        if (this.testExecutions.size() === 0) {
            return;
        }
        var firstModel = this.testExecutions.slice(0, 1)[0];
        var result = firstModel.getExecutionResultObj(),
            testCaseId = firstModel.getAttribute('testCase');

        var testCaseList = this.testCases.search(testCaseId, ['testCasePk']);
        if (testCaseList.length === 1) {
            var testCaseModel = this.testCases.getModel(testCaseList[0].id);
            testCaseModel.setAttribute('result', result);
        }
    }

    function fetchTestExecutions () {
        this.testExecutions.fetch({
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    this.progressBlock.hideProgress();
                    populateTestExecutionsBlock.call(this);
                    updateTestCaseResultAttribute.call(this);
                }.bind(this)
            })
        });
    }

    function showTestCaseExecutions (testCaseId) {
        this.view.getSlidingPanels().setModifier('executions');
        destroyTestExecutions.call(this);
        this.progressBlock.attachTo(this.view.getTestExecutions());
        this.progressBlock.showProgress();
        this.testExecutions.setTestCaseId(testCaseId);
        fetchTestExecutions.call(this);
    }

    function hideTestCaseExecutions () {
        this.testExecutions.setTestCaseId('');
        this.view.getSlidingPanels().removeModifier('executions');
        destroyTestExecutions.call(this);
    }

    function destroyTestExecutions () {
        this._testCaseExecutions.forEach(function (testCaseExecution) {
            testCaseExecution.destroy();
        });
        this._testCaseExecutions = [];

        var testExecutionsEl = this.view.getTestExecutions();
        testExecutionsEl.removeModifier('noResult');
        testExecutionsEl.setText('');
    }

    function createSearchWidget (screenId) {
        this.searchWidget = new TestCaseSearchFormWidget({
            region: this,
            screenId: screenId
        });
        this.searchWidget.attachTo(this.view.getFilterContent());
    }

    function addButtonEventHandlers () {
        this.view.getFilterButton().addEventHandler('click', this.onFilterButtonClick, this);
        this.view.getHideButton().addEventHandler('click', this.onFilterButtonClick, this);
        this.view.getSettingsButton().addEventHandler('click', this.onSettingsButtonClick, this);
        this.view.getHideSettingsButton().addEventHandler('click', this.onSettingsButtonClick, this);
    }

    function showAdvancedSearch () {
        this.view.getSlidingPanels().setModifier('search');
        this.view.getFilterButton().setModifier('hidden');
        this.view.getHideButton().removeModifier('hidden');
        this.searchIsShown = true;
    }

    function hideAdvancedSearch () {
        this.view.getSlidingPanels().removeModifier('search');
        this.view.getHideButton().setModifier('hidden');
        this.view.getFilterButton().removeModifier('hidden');
        this.searchIsShown = false;
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
        TableHelper.applyColumnsWidths(this.LS_COLUMNS_WIDTHS, this.columns);
        this.testCasesTable = new Table({
            tooltips: true,
            columns: columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: TableHelper.plugins({
                tableId: 'TMS_TestCampaignExecution_TestCaseTable',
                tableRowAttribute: 'testCase.testCaseId'
            }, [
                new StyledHeaderCell({
                    headerClasses: {
                        0: 'ebTable-noPadding'
                    }
                }),
                new Selection({
                    checkboxes: false,
                    selectableRows: true,
                    multiselect: false,
                    bind: false
                })
            ])
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
        this.testCasesTable.attachTo(this.view.getTestCasesTable());

        this.testCasesTable.addEventHandler('sort', function (order, row) {
            if (row === 'result') {
                row = 'resultOrder';
            }
            this.testCasesTable.sort(order, row);
        }, this);

        this.testCasesTable.addEventHandler('rowselect', this.onTableRowSelect.bind(this));

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

        this.testCases.addEventHandler('reset', function () {
            if (this.tableHelper.isFiltered()) {
                this.testCasesTable.trigger('filter');
            }
            if (this.testExecutionMultiSelectWidget) {
                this.testExecutionMultiSelectWidget.setTableData(this.testCasesTable.getData());
            }
            this.updateTotalCount(this.testCases);

            var models = this.testCases.toJSON();
            var items = this.testExecutionHelper.calculateTestExecutions(models);
            this.stackedProgressBar.setItems(this.testExecutionHelper.convertToList(items));
        }.bind(this));
    }

    function setSortOrder () {
        this.testCases.each(function (model) {
            var resultObj = model.getAttribute('result');
            if (resultObj === null) {
                model.setAttribute('resultOrder', NULL_RESULT_SORT_ORDER);
            } else {
                model.setAttribute('resultOrder', resultObj.sortOrder);
            }
        });
    }

    function createProgressBlock () {
        this.progressBlock = new ProgressBlock();
    }

    function populateTestExecutionsBlock () {
        var testExecutionsEl = this.view.getTestExecutions();
        if (this.testExecutions.size() > 0) {
            this.testExecutions.each(function (testExecutionModel) {
                var statusObj = Constants.executionStatusMap[testExecutionModel.getExecutionResultId()];
                if (statusObj === '') {
                    statusObj = Constants.executionStatusMap[''];
                } else {
                    statusObj.title = testExecutionModel.getExecutionResultTitle();
                }
                var defectsWithLinks = addPermanentUrlToData(testExecutionModel.getDefectIds());
                var slicedDefects = sliceLastDefect(defectsWithLinks);
                var requirementsWithLinks = addPermanentUrlToData(testExecutionModel.getRequirementIds());
                var slicedRequirements = sliceLastDefect(requirementsWithLinks);

                var testCaseExecution = new TestExecutionWidget({
                    model: testExecutionModel,
                    region: this,
                    eventBus: this.eventBus,
                    execution: {
                        id: testExecutionModel.getId(),
                        username: testExecutionModel.getAuthor(),
                        date: testExecutionModel.getDate(),
                        result: statusObj.title,
                        resultIcon: statusObj.icon,
                        iso: testExecutionModel.getIsoVersion(),
                        comment: testExecutionModel.getComment(),
                        defects: slicedDefects.first,
                        lastDefect: slicedDefects.last.pop(),
                        requirements: slicedRequirements.first,
                        lastRequirement: slicedRequirements.last.pop()
                    }
                });
                testCaseExecution.attachTo(testExecutionsEl);

                this._testCaseExecutions.push(testCaseExecution);
            }.bind(this));
        } else {
            testExecutionsEl.setModifier('noResult');
            testExecutionsEl.setText('No executions available.');
        }
    }

    function sliceLastDefect (defectIds) {
        if (defectIds) {
            if (defectIds.length > 1) {
                var first = defectIds.slice(0, defectIds.length - 1);
                var second = defectIds.slice(defectIds.length - 1);
                return {first: first, last: second};
            } else {
                return {last: defectIds};
            }
        }
        return {};
    }

    function addPermanentUrlToData (defectIds) {
        var dataArray = [];
        if (defectIds) {
            defectIds.forEach(function (entry) {
                dataArray.push({
                    permanentUrl: Constants.urls.JIRA_BROWSE_LINK,
                    data: entry
                });
            });
        }
        return dataArray;
    }

    function createStackedProgressBar () {
        this.stackedProgressBar = new StackedProgressBar();
        this.stackedProgressBar.attachTo(this.view.getStackedProgessBarHolder());
        this.stackedProgressBar.setWidth('12rem');
    }
});
