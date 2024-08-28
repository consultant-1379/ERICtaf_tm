define([
    'jscore/core',
    'tablelib/Table',
    '../../../../common/table/filters/StringFilterCell/StringFilterCell',
    '../../../../common/cells/CellFactory',
    '../../../../common/Navigation',
    '../../../../common/DateHelper',
    './TestCampaignGroupWidgetView',
    '../../../../common/Constants',
    '../../../../common/widgets/TableProgressBar/TableProgressBar',
    'widgets/Tree',
    '../../../../common/TestExecutionHelper',
    '../../../../common/PaginationHelper',
    'chartlib/charts/Gauge',
    'chartlib/charts/Column',
    'tablelib/plugins/ResizableHeader',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    'tablelib/plugins/Selection',
    'tablelib/plugins/SecondHeader',
    'jscore/ext/utils/base/underscore',
    './../../../../common/ObjectHelper',
    'widgets/ProgressBar'
], function (core, Table, StringFilterCell, CellFactory, Navigation, DateHelper, View, Constants,
             TableProgressBar, Tree, TestExecutionHelper, PaginationHelper, Gauge, Column, ResizableHeader,
             NotificationRegion, Selection, SecondHeader, _, ObjectHelper, ProgressBar) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.region = options.region;
            this.eventBus = this.region.getEventBus();
            this.collection = options.collection;
            this.testExecutionHelper = new TestExecutionHelper();
            this.filters = {};
            this.selectedData = [];
            this.TM_SERVER = '/tm-server/api/testCampaignGroup';

            this.collection.addEventHandler('reset', this.updateData, this);

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
                    resizable: true
                },
                {
                    title: 'Description',
                    attribute: 'description',
                    secondHeaderCellType: StringFilterCell,
                    resizable: true
                },
                {
                    title: 'Drop',
                    cellType: CellFactory.object(),
                    attribute: 'drop.name',
                    secondHeaderCellType: StringFilterCell,
                    resizable: true
                },
                {
                    title: 'Environment',
                    attribute: 'environment',
                    secondHeaderCellType: StringFilterCell,
                    resizable: true
                },
                {
                    title: 'Pass Rate',
                    attribute: 'testCampaignItems',
                    cellType: CellFactory.widget({
                        widget: TableProgressBar
                    }),
                    width: '140px'
                }

            ];
        },

        onViewReady: function () {
            createTestCampaignsTable.call(this);
            this.paginationHelper = new PaginationHelper();

            this.view.getSearch().addEventHandler('keyup', function (e) {
                var searchValue = this.view.getSearch().getValue().trim();
                var keyCode = e.originalEvent.keyCode || e.originalEvent.which;
                if (keyCode === 13) {
                    search.call(this);
                } else if (searchValue <= 0) {
                    this.collection.setPage(1);
                    this.region.refreshTestCampaignGroups();
                }
            }.bind(this));

            this.view.getEditButton().addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                if (selectedItem && !selectedItem.options.parentItem) {
                    this.region.editGroup(selectedItem.options.item);
                } else {
                    notSelectedNotification.call(this);
                }
            }.bind(this));

            this.view.getDeleteButton().addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                if (selectedItem && !selectedItem.options.parentItem) {
                    var group = selectedItem.options.item;
                    this.region.deleteGroup(group);
                } else {
                    notSelectedNotification.call(this);
                }
            }.bind(this));

            this.view.getUrlButton().addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                if (selectedItem && !selectedItem.options.parentItem) {
                    var group = selectedItem.options.item;
                    this.copyURL = this.region.copyUrlGroup(group);
                    this.copyURLSelected = true;
                    document.execCommand('copy');
                    this.copyURLSelected = false;
                } else {
                    notSelectedNotification.call(this);
                }
            }.bind(this));

            document.addEventListener('copy', function (clipboardEvent) {
                if (this.copyURLSelected) {
                    clipboardEvent.preventDefault();
                    clipboardEvent.clipboardData.setData('Text', this.copyURL);
                }
            }.bind(this));

            this.csvExport = this.view.getExportButton();
            this.csvExport.addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                if (selectedItem) {
                    var id = selectedItem.options.item.id;
                    this.csvExport.setAttribute('href',
                        window.location.origin +
                        this.TM_SERVER + '/csv/' + id);
                }
            }.bind(this));

            var testCampaignIdSet = new Set();
            this.testCampaignsTable.addEventHandler('checkend', function (rows) {
                if (rows.length === 0) {
                    testCampaignIdSet.clear();
                }
                rows.forEach(function (row) {
                    var objectData = row.getData();
                    testCampaignIdSet.add(objectData.id);
                });
            }.bind(this));

            this.sprintReport = this.view.getSprintValidationReportButton();
            this.sprintReport.addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                var id = selectedItem.options.item.id;
                var checkedRowId = this.getSelectedRowId();
                var testCampaignId = Array.from(testCampaignIdSet);
                var i = testCampaignId.length;
                while (i--) {
                    if (!checkedRowId.includes(testCampaignId[i])) {
                        testCampaignId.splice(i, 1);
                    }}
                if (selectedItem && testCampaignId.length) {
                    requestDocReport.call(this, id, testCampaignId);
                } else {
                    requestDocReport.call(this, id);
                }
            }.bind(this));

            this.sovReport = this.view.getSovStatusReportButton();
            this.sovReport.addEventHandler('click', function () {
                var selectedItem = this.tree.getSelectedItem();
                var id = selectedItem.options.item.id;
                var checkedRowId = this.getSelectedRowId();
                var testCampaignId = Array.from(testCampaignIdSet);
                var i = testCampaignId.length;
                while (i--) {
                    if (!checkedRowId.includes(testCampaignId[i])) {
                        testCampaignId.splice(i, 1);
                    }}
                if (selectedItem && testCampaignId.length) {
                    requestSovReport.call(this, id, testCampaignId);
                } else {
                    requestSovReport.call(this, id);
                }
            }.bind(this));

            createGaugeChart.call(this, 0);
            createColumnChart.call(this, []);
            createDetailedProgressBars.call(this);
        },

        getSelectedRowId: function () {
            var testCampaignId = '';
            var checkedRows = this.testCampaignsTable.getCheckedRows();
            checkedRows.forEach(function (checkedRow) {
                var objectData = checkedRow.getData();
                testCampaignId += objectData.id + ',';
            });
            return testCampaignId;
        },

        updateData: function () {
            this.view.hideExportButton();
            this.view.hideSprintValidationReportButton();
            this.view.hideSovStatusReportButton();
            this.testCampaignsTable.setData([]);
            this.selectedData = [];
            var title = '(0/0) 0%';
            this.view.getTotalTestCases().setText(title);

            var totalCount = this.collection.getTotalCount().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            this.view.getTotalCount().setText('(' + totalCount + ')');

            var treeData = convertToTreeItem(this.collection.toJSON());
            createTree.call(this, treeData);

            var search = this.view.getSearch().getValue().trim();
            if (treeData.length > 0 && search.length > 0) {
                this.tree.getItems().forEach(function (item) {
                    if (search === item.options.item.label) {
                        item.select();
                    }
                }.bind(this));
            }
        },

        setSearch: function (value) {
            this.view.getSearch().setValue(value);
        }

    });

    function createTestCampaignsTable () {
        this.testCampaignsTable = new Table({
            tooltips: true,
            columns: this.columns,
            modifiers: [
                {name: 'striped'}
            ],
            plugins: [
                new ResizableHeader(),
                new Selection({
                    checkboxes: true,
                    selectableRows: true,
                    multiselect: true,
                    bind: true
                }),
                new SecondHeader()
            ]
        });

        this.testCampaignsTable.attachTo(this.view.getTableHolder());

        this.testCampaignsTable.addEventHandler('checkend', function (rows) {
            var results = [];
            rows.forEach(function (row) {
                results.push(row.getData());
            }.bind(this));

            var testCases = getTestCaseData(results);
            updateCharts.call(this, testCases);
        }.bind(this));

        this.testCampaignsTable.addEventHandler('filter', function (attribute, value, comparator) {
            if (_.isEmpty(value)) {
                delete this.filters[attribute];
            } else {
                this.filters[attribute] = {value: value, comparator: comparator};
            }

            if (_.isEmpty(this.filters)) {
                this.testCampaignsTable.setData(this.selectedData);
                var testCasesData = getTestCaseData(this.selectedData);
                updateCharts.call(this, testCasesData);
                return;
            }

            var data = this.selectedData;

            data = filterData.call(this, this.filters, data);
            this.testCampaignsTable.setData(data);
            var testCases = getTestCaseData(data);
            updateCharts.call(this, testCases);
        }.bind(this));
    }

    function createTree (data) {
        if (this.tree) {
            this.tree.destroy();
        }

        this.tree = new Tree({
            items: data
        });

        this.tree.attachTo(this.view.getTreeHolder());

        this.pagination = this.paginationHelper.createPagination(this.collection, this.view.getPaginationHolder());

        this.tree.addEventHandler('itemselect', function () {
            var selectedItem = this.tree.getSelectedItem();
            var item = selectedItem.options.item.children;
            var testCampaign = selectedItem.options.item.item;
            if (item) {
                var treeData = getTreeDataForTable(item);
                this.testCampaignsTable.setData(treeData);
                this.selectedData = treeData;
                var testCases = getTestCaseData(treeData);
                updateCharts.call(this, testCases);
                this.view.showExportButton();
                this.view.showSprintValidationReportButton();
                this.view.showSovStatusReportButton();

            } else if (testCampaign) {
                this.testCampaignsTable.setData([testCampaign]);
                this.selectedData = [testCampaign];
                var testCasesData = getTestCaseData([testCampaign]);
                updateCharts.call(this, testCasesData);
                this.view.hideExportButton();
                this.view.hideSprintValidationReportButton();
                this.view.hideSovStatusReportButton();

            } else {
                this.testCampaignsTable.setData([]);
                this.selectedData = [];
            }
        }.bind(this));
    }

    function convertToTreeItem (items) {
        var treeData = [];
        items.forEach(function (item) {
            var obj = {label: item.name, id: item.id, icon: 'folder'};
            obj.children = addChildren(item.testCampaigns);
            treeData.push(obj);
        }.bind(this));

        return treeData;
    }

    function addChildren (children) {
        var childrenData = [];
        children.forEach(function (item) {
            var obj = {label: item.name, icon: 'newFile', item: item};
            childrenData.push(obj);
        }.bind(this));

        return childrenData;
    }

    function getTreeDataForTable (data) {
        var result = [];
        data.forEach(function (item) {
            result.push(item.item);
        }.bind(this));

        return result;
    }

    function getTestCaseData (data) {
        var result = [];
        data.forEach(function (item) {
            item.testCampaignItems.forEach(function (testCase) {
                result.push(testCase);
            }.bind(this));
        }.bind(this));

        return result;
    }

    function calculateTotal (testCampaignItems) {
        var resultMap = this.testExecutionHelper.calculateTestExecutions(testCampaignItems);
        var resultValue = 0;
        var count = 0;
        var total = testCampaignItems.length;

        if (resultMap.Pass) {
            resultValue += resultMap.Pass.value;
            count += resultMap.Pass.count;
        }
        if (resultMap['Passed with exception']) {
            resultValue += resultMap['Passed with exception'].value;
            count += resultMap['Passed with exception'].count;
        }

        if (resultMap['N/A']) {
            resultValue += resultMap['N/A'].value;
            count += resultMap['N/A'].count;
        }

        if ((count / total) === 1) {
            resultValue = 100;
        }

        return {total: total, value: count, percentage: resultValue, resultMap: resultMap};
    }

    function calculateColumnData (testCampaignItems) {
        var resultMap = this.testExecutionHelper.calculateFeaturePerType(testCampaignItems);
        return this.testExecutionHelper.convertToList(resultMap);
    }

    function createGaugeChart (data) {
        if (this.gaugeChart) {
            this.gaugeChart.destroy();
        }
        this.gaugeChart = new Gauge({
            element: this.view.getPercentageHolder(),
            chart: {
                labels: {
                    value: {
                        format: function (v) {
                            return Math.round(v) + '%';
                        }
                    },
                    title: 'Overall Pass Rate',
                    min: '0%',
                    max: '100%'
                },
                areas: [{
                    fromValue: 0,
                    fill: '#ddd',
                    lineFill: '#e32119'
                }, {
                    fromValue: 100,
                    fill: '#aaa',
                    lineFill: '#89ba17'
                }]
            },
            data: {
                value: data
            }
        });
    }

    function createColumnChart (data) {
        if (this.columnChart) {
            this.columnChart.destroy();
        }

        this.columnChart = new Column({
            element: this.view.getChart(),
            data: data,
            plotOptions: {
                column: {
                    stacked: true
                }
            },
            legend: {
                events: {
                    click: 'toggle'
                }
            },
            tooltip: true
        });
    }

    function createDetailedProgressBars () {
        this.notApplicableBar = new ProgressBar({
            label: Constants.executionLabels.notApplicable,
            value: 0,
            color: 'green'
        });

        this.failedBar = new ProgressBar({
            label: Constants.executionLabels.failed,
            value: 0,
            color: 'red'
        });

        this.inProgressBar = new ProgressBar({
            label: Constants.executionLabels.workInProgress,
            value: 0,
            color: 'red'
        });

        this.blockedBar = new ProgressBar({
            label: Constants.executionLabels.blocked,
            value: 0,
            color: 'red'
        });

        this.notExecutedBar = new ProgressBar({
            label: Constants.executionLabels.notExecuted,
            value: 0,
            color: 'red'
        });

        this.noStartedBar = new ProgressBar({
            label: Constants.executionLabels.notStarted,
            value: 0,
            color: 'red'
        });

        this.passBar = new ProgressBar({
            label: Constants.executionLabels.passed,
            value: 0,
            color: 'green'
        });

        this.passedWithExeception = new ProgressBar({
            label: Constants.executionLabels.passedWithException,
            value: 0,
            color: 'green'
        });

        this.failedBar.attachTo(this.view.getDetailedProgressBar());
        this.blockedBar.attachTo(this.view.getDetailedProgressBar());
        this.notExecutedBar.attachTo(this.view.getDetailedProgressBar());
        this.inProgressBar.attachTo(this.view.getDetailedProgressBar());
        this.noStartedBar.attachTo(this.view.getDetailedProgressBar());
        this.notApplicableBar.attachTo(this.view.getDetailedProgressBar());
        this.passBar.attachTo(this.view.getDetailedProgressBar());
        this.passedWithExeception.attachTo(this.view.getDetailedProgressBar());
    }

    function clearAllProgressBars () {
        this.failedBar.setValue(0);
        this.blockedBar.setValue(0);
        this.notExecutedBar.setValue(0);
        this.inProgressBar.setValue(0);
        this.noStartedBar.setValue(0);
        this.notApplicableBar.setValue(0);
        this.passBar.setValue(0);
        this.passedWithExeception.setValue(0);

        this.failedBar.setLabel(Constants.executionLabels.failed);
        this.blockedBar.setLabel(Constants.executionLabels.blocked);
        this.notExecutedBar.setLabel(Constants.executionLabels.notExecuted);
        this.inProgressBar.setLabel(Constants.executionLabels.workInProgress);
        this.noStartedBar.setLabel(Constants.executionLabels.notStarted);
        this.notApplicableBar.setLabel(Constants.executionLabels.notApplicable);
        this.passBar.setLabel(Constants.executionLabels.passed);
        this.passedWithExeception.setLabel(Constants.executionLabels.passedWithException);
    }

    function updateProgressBars (data) {
        if (data.Fail) {
            this.failedBar.setLabel(prepareCountInLabel(data.Fail.count, Constants.executionLabels.failed));
            this.failedBar.setValue(parseFloat(data.Fail.value.toFixed(1)));
        }
        if (data.Blocked) {
            this.blockedBar.setLabel(prepareCountInLabel(data.Blocked.count, Constants.executionLabels.blocked));
            this.blockedBar.setValue(parseFloat(data.Blocked.value.toFixed(1)));
        }

        if (data['Not executed']) {
            this.notExecutedBar.setLabel(prepareCountInLabel(data['Not executed'].count,
                Constants.executionLabels.notExecuted));
            this.notExecutedBar.setValue(parseFloat(data['Not executed'].value.toFixed(1)));
        }

        if (data['Work in progress']) {
            this.inProgressBar.setLabel(prepareCountInLabel(data['Work in progress'].count,
                Constants.executionLabels.workInProgress));
            this.inProgressBar.setValue(parseFloat(data['Work in progress'].value.toFixed(1)));
        }

        if (data['Not started']) {
            this.noStartedBar.setLabel(prepareCountInLabel(data['Not started'].count,
                Constants.executionLabels.notStarted));
            this.noStartedBar.setValue(parseFloat(data['Not started'].value.toFixed(1)));
        }

        if (data['N/A']) {
            this.notApplicableBar.setLabel(prepareCountInLabel(data['N/A'].count,
                Constants.executionLabels.notApplicable));
            this.notApplicableBar.setValue(parseFloat(data['N/A'].value.toFixed(1)));
        }

        if (data.Pass) {
            this.passBar.setLabel(prepareCountInLabel(data.Pass.count,
                Constants.executionLabels.passed));
            this.passBar.setValue(parseFloat(data.Pass.value.toFixed(1)));
        }

        if (data['Passed with exception']) {
            this.passedWithExeception.setLabel(prepareCountInLabel(data['Passed with exception'].count,
                Constants.executionLabels.passedWithException));
            this.passedWithExeception.setValue(parseFloat(data['Passed with exception'].value.toFixed(1)));
        }
    }

    function search () {
        var searchValue = this.view.getSearch().getValue().trim();
        if (searchValue.length > 0) {
            this.collection.setPage(1);
            this.region.refreshTestCampaignGroups('name~' +
                encodeURIComponent(this.view.getSearch().getValue().trim()));
        } else {
            this.collection.setPage(1);
            this.region.refreshTestCampaignGroups();
        }
    }

    function prepareCountInLabel (value, label) {
        return '(' + value + ') ' + label;
    }

    function updateCharts (testCases) {
        var totalObj = calculateTotal.call(this, testCases);
        var title = '(' + totalObj.value + '/' + totalObj.total + ') ' + Math.round(totalObj.percentage) + '%';
            this.view.getTotalTestCases().setText(title);
        this.gaugeChart.update({value: totalObj.percentage});

        var chartData = calculateColumnData.call(this, testCases);
        this.columnChart.update(chartData);

        clearAllProgressBars.call(this);
        updateProgressBars.call(this, totalObj.resultMap);
    }

    function notSelectedNotification () {
        var options = NotificationRegion.NOTIFICATION_TYPES.warning;
        options.canDismiss = true;
        options.canClose = true;
        this.eventBus.publish(Constants.events.NOTIFICATION, 'Test campaign group not selected', options);
    }

    function filterData (filters, data) {
        _.each(filters, function (filterObj, attribute) {
            data = _.filter(data, function (item) {
                if (filterObj.value) {
                    var dataItem = ObjectHelper.findValue(item, attribute);
                    if (dataItem) {
                        dataItem = dataItem.toLowerCase();
                    }
                    var value = filterObj.value.toLowerCase();

                    switch (filterObj.comparator) {
                        case '=':
                            return dataItem === value;
                        case '~':
                            return compare(dataItem, value);
                        case '!=':
                            return dataItem !== value;
                        default:
                            return false;
                    }
                } else {
                    return true;
                }
            });
        });

        return data;
    }

    function compare (value1, value2) {
        if (!value1 || !value2) {
            return false;
        }

        if (value1.indexOf(value2) !== -1) {
            return true;
        } else {
            return false;
        }
    }

    function requestDocReport (id, opts) {
        requestTestCasesReport.call(this, id , opts);
    }

    function requestSovReport (id, opts) {
        requestSovStatusReport.call(this, id , opts);
    }
    function requestTestCasesReport (id, opts) {
        var url = '/tm-server/api/test-cases.docx/';
        id += '/';
        if (typeof opts !== 'undefined' && opts.length) {
            id += opts;
        }
        url += id;

         this.sprintReport.setAttribute('href', url);
         location.href = this.sprintReport.getAttribute('href');
    }

    function requestSovStatusReport (id, opts) {
        var url = '/tm-server/api/test-cases.docx/';
        id += '/0/0/';
        if (typeof opts !== 'undefined' && opts.length) {
            id += opts;
        }
        url += id;

         this.sovReport.setAttribute('href', url);
               location.href = this.sovReport.getAttribute('href');
    }

});
