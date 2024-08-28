define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/utils/domUtils',
    'widgets/Dialog',
    'widgets/SelectBox',
    './TestCaseSearchContentRegionView',
    '../../models/testCases/TestCasesCollection',
    '../../models/testCases/TestCasesListModel',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/ContextFilter',
    '../../../../common/ModelHelper',
    '../../../../common/Logger',
    '../TestCaseSearchFormWidget/TestCaseSearchFormWidget',
    '../TestCaseSearchResultsWidget/TestCaseSearchResultsWidget',
    '../../../../common/models/completion/CompletionsCollection',
    '../../../../common/widgets/progressBlock/ProgressBlock',
    '../../../../ext/domUtils',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../common/RadioButtons/RadioButtons',
    '../AddTestCaseTestCampaignWidget/AddTestCaseTestCampaignWidget',
    'jscore/base/jquery',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../../../routing/LocationHelper'
], function (core, _, widgetDomUtils, Dialog, SelectBox, View, TestCasesCollection, TestCasesListModel,
             Constants, Animation, ContextFilter, ModelHelper, Logger, TestCaseSearchFormWidget,
             TestCaseSearchResultsWidget, CompletionsCollection, ProgressBlock, domUtils, Notifications, RadioButtons,
             AddTestCaseTestCampaignWidget, $, NotificationRegion, LocationHelper) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.testCasesCollection = new TestCasesCollection();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.searchBlock = new TestCaseSearchFormWidget({
                region: this
            });
            this.searchBlock.attachTo(this.view.getSearchContent());

            this.resultsBlock = new TestCaseSearchResultsWidget({
                testCasesCollection: this.testCasesCollection,
                eventBus: this.eventBus,
                region: this
            });
            this.resultsBlock.attachTo(this.view.getResultsPanel());

            this.eventBus.subscribe(Constants.events.SHOW_SEARCH, this.onShowSearch, this);
            this.eventBus.subscribe(Constants.events.HIDE_SEARCH, this.onHideSearch, this);

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_CONTENT, this.onMarkContentCurrent.bind(this));
            this.animation.hideOn(Constants.events.HIDE_CONTENT);
            this.animation.markCurrentOn(Constants.events.MARK_CONTENT_CURRENT, this.onMarkContentCurrent.bind(this));

            core.Window.addEventHandler('resize', this.resizeHandler.bind(this));

            this.radioWidget = new RadioButtons({
                radioOptions: ['Include file content in report']
            });

            this.eventBus.subscribe(Constants.events.CONTEXT_MENU_CHANGE, this.updateSearchQuery, this);
            this.eventBus.subscribe(Constants.events.TEST_CASE_SEARCH_FILTER_CHANGED, function (filters) {
                var params = [];
                for (var key in filters) {
                    if (filters[key]) {
                        params.push(key + filters[key].comparator + filters[key].value);
                    }
                }
                ContextFilter.searchQuery = params.join('&');
                this.eventBus.publish(Constants.events.UPDATE_URL_WITH_SEARCH, ContextFilter.searchQuery, false);
            }.bind(this));
        },

        getEventBus: function () {
            return this.eventBus;
        },

        onDOMAttach: function () {
            this.resizeHandler();
        },

        updateListViewTable: function (searchQuery) {
            if (searchQuery !== undefined) {
                this.testCasesCollection.setSearchQueryAndResetPage(searchQuery);
            }
            this.resultsBlock.refreshDataInfo();

            this.testCasesCollection.fetch({
                reset: true,
                data: {
                    view: 'simple-requirements'
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.searchBlock.updateAdvancedSearch(this.testCasesCollection.getCriterions());
                    }.bind(this)
                })
            });
        },

        runSearch: function (criterionsUrl) {
            this.eventBus.publish(Constants.events.UPDATE_URL_WITH_SEARCH, criterionsUrl, true);
            this.eventBus.publish(Constants.events.CLEAR_SEARCH_INPUT);
        },

        resizeHandler: function () {
            var top = domUtils.offset(this.view.getSearchPanel()).top;
            var dimensions = widgetDomUtils.getWindowDimensions();
            var windowHeight = dimensions.height;
            var height = windowHeight - top - 24;

            this.view.getSlidingPanels().setStyle('min-height', height);
            this.view.getSearchPanel().setStyle('height', height);
            this.view.getSearchContent().setStyle('height', height);

            this.searchBlock.resizeBlock();
        },

        onShowSearch: function () {
            this.view.showSearch();
        },

        onHideSearch: function () {
            this.view.hideSearch();
        },

        onMarkContentCurrent: function (paramsObj) {
            ContextFilter.profileReady
                .then(function () {
                    if (ContextFilter.getActiveProductId() != null && ContextFilter.searchQuery.length === 0) {
                        this.updateListViewTable(Constants.params.PRODUCT_NAME + '=' + ContextFilter.productIdParam);
                    } else if (ContextFilter.searchQuery.length > 0) {
                        this.updateListViewTable(ContextFilter.searchQuery);
                    } else {
                        this.updateListViewTable();
                    }
                    var itemId = '';
                    if (paramsObj) {
                        itemId = paramsObj.itemId;
                    }
                    this.eventBus.publish(Constants.events.UPDATE_SELECTED_PROJECT, itemId);
                }.bind(this));
        },

        updateSelection: function (selectedItems) {
            this.testCasesCollection.each(function (model) {
                var testCaseId = model.getId();
                var foundItem = _.find(selectedItems, function (selectedItem) {
                    var selectedTestCaseId = selectedItem.options.model.id;
                    return testCaseId === selectedTestCaseId;
                });
                model.setAttribute('selected', foundItem !== undefined);
            });
        },

        requestAddToTestPlan: function () {
            var testCasesCollection = this.testCasesCollection;
            if (ContextFilter.profileProduct == null) {
                showSelectProductWarningDialog.call(this);
                return;
            }

            var addTestCaseTestCampaignWidget = new AddTestCaseTestCampaignWidget({
                profileProduct: ContextFilter.profileProduct
            });

            var selected = listOfSelectedTestCases.call(this, this.testCasesCollection);
            var testCaseList = selected.map(function (item) {
                return '"' + item.getTitle() + '"';
            }).join('; ');

            if (selected.length) {
                showSelectTestPlanDialog.call(this, addTestCaseTestCampaignWidget, testCaseList, testCasesCollection);
            } else {
                showSelectTestCaseWarningDialog();
            }
        },

        requestTestCaseReport: function (reportType) {
            if (ContextFilter.searchQuery !== '') {
                createReport.call(this, reportType);
                return;
            }
        },

        requestTestCaseExportReport: function () {
            var url = '/tm-server/api/test-cases/export';

            if (ContextFilter.searchQuery !== '') {
                url += '?q=' + encodeURIComponent(ContextFilter.searchQuery);
            }

            createExcelReport.call(this, url);
        },

        updateSearchQuery: function (selection) {
            var params = [];
            var paramMap = LocationHelper.parseQueryWithOperator(ContextFilter.searchQuery);

            if (selection.product) {
                ContextFilter.productIdParam = selection.product.name;
                paramMap[Constants.params.PRODUCT_NAME] = {operator: '=',  value: ContextFilter.productIdParam};
            }

            if (selection.feature) {
                ContextFilter.featureIdParam = selection.feature.name;
                paramMap[Constants.params.FEATURE_NAME] = {operator: '=', value: ContextFilter.featureIdParam};
            }

            if (selection.components) {
                var components = [];
                selection.components.forEach(function (component) {
                    components.push(Constants.params.COMPONENT_NAME + '=' + component.name);
                });
                paramMap[Constants.params.COMPONENT_NAME] = {operator: '=', value: components.join('&')};
            }

            for (var key in paramMap) {
                if (paramMap[key]) {
                    params.push(key + paramMap[key].operator + paramMap[key].value);
                }
            }

            ContextFilter.searchQuery = params.join('&');

            ContextFilter.isAdvancedSearch = false;

            this.eventBus.publish(
                Constants.events.UPDATE_URL_WITH_SEARCH,
                ContextFilter.searchQuery,
                ContextFilter.isAdvancedSearch
            );

        },

        removeSearch: function (model) {
            $.ajax({
                url: '/tm-server/api/users/saved-search/' + ContextFilter.userId + '/' + model.id,
                type: 'DELETE',
                contentType: 'application/json',
                dataType: 'json',
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    204: function () {
                        var options = NotificationRegion.NOTIFICATION_TYPES.success;
                        options.canDismiss = true;
                        options.canClose = true;
                        this.eventBus.publish(Constants.events.NOTIFICATION, 'Search has been deleted', options);
                        this.eventBus.publish(Constants.events.RELOAD_USER_PROFILE);
                    }.bind(this)
                })
            });
        }
    });

    function addTestCasesToTestPlan (selected, testPlanId, testPlanTitle) {
        var testCasesCollection = new TestCasesListModel();
        testCasesCollection.setTestPlanId(testPlanId);
        testCasesCollection.setTestCases(selected);
        testCasesCollection.save([], {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                204: function () {
                    testCasesSuccessfullySaved.call(this, testPlanTitle);
                }.bind(this)
            })
        });
    }

    function createReport (reportType) {
        this.radioWidget.reset();
        var totalCount = this.testCasesCollection.getTotalCount().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');

        var optionsDialog = new Dialog({
            header: 'Report Options',
            type: 'warning',
            content: this.radioWidget,
            optionalContent: 'You are about to export ' + '(' + totalCount + ') records!',
            buttons: [
                {
                    caption: 'Export',
                    color: 'green',
                    action: function () {
                        if (this.radioWidget.getValue().length > 0) {
                            requestTestCasesReport.call(this, true, reportType);
                        } else {
                            requestTestCasesReport.call(this, false, reportType);
                        }
                        optionsDialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        optionsDialog.hide();
                    }
                }
            ]
        });

        optionsDialog.show();
    }

    function createExcelReport (url) {
            var totalCount = this.testCasesCollection.getTotalCount().toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');

            var optionsDialog = new Dialog({
                header: 'Excel Export',
                type: 'warning',
                content: 'You are about to export ' + '(' + totalCount + ') records!',
                buttons: [
                    {
                        caption: 'Export',
                        color: 'green',
                        action: function () {
                            this.resultsBlock.view.getExportLink().setAttribute('href', url);
                            window.location = this.resultsBlock.view.getExportLink().getAttribute('href');
                            optionsDialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'Cancel',
                        action: function () {
                            optionsDialog.hide();
                        }
                    }
                ]
            });

            optionsDialog.show();
        }

    function listOfSelectedTestCases (testCases) {
        var selected = [];
        testCases.each(function (model) {
            var checked = model.getAttribute('selected');
            if (checked) {
                selected.push(model);
            }
        });
        return selected;
    }

    function requestTestCasesReport (view, reportType) {
        var url = '/tm-server/api/test-cases.docx';

        url += '?reportType=' + reportType;

        if (ContextFilter.searchQuery !== '') {
            url += '&q=' + encodeURIComponent(ContextFilter.searchQuery);
        }

        if (ContextFilter.searchQuery !== '' && view) {
            url += '&view=detailed';
        } else if (ContextFilter.searchQuery === '' && view) {
            url += '&view=detailed';
        }

        this.resultsBlock.view.getExportLink().setAttribute('href', url);
        window.location = this.resultsBlock.view.getExportLink().getAttribute('href');
    }

    function showSelectTestPlanDialog (content, testCaseList, testCasesCollection) {
        var dialog = new Dialog({
            header: 'Select Test Campaign',
            type: 'information',
            content: content,
            optionalContent: 'Following test cases will be attached to the selected test campaign : [' + testCaseList + ']',
            buttons: [
                {
                    caption: 'Add',
                    color: 'green',
                    action: function () {
                        var selectedValue = content.getSelectedTestCampaign();
                        if (!selectedValue.value) {
                            return;
                        }

                        var testPlanId = selectedValue.value;
                        var testPlanTitle = selectedValue.name;
                        var selected = listOfSelectedTestCases.call(this, testCasesCollection);
                        if (selected.length === 0) {
                            return;
                        }

                        addTestCasesToTestPlan.call(this, selected, testPlanId, testPlanTitle);
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
    }

    function showSelectTestCaseWarningDialog () {
        var warningDialog = new Dialog({
            header: 'Select Test Cases',
            content: 'Please select Test Cases for a Test Campaign',
            type: 'information',
            buttons: [
                {
                    caption: 'OK',
                    color: 'blue',
                    action: function () {
                        warningDialog.hide();
                    }.bind(this)
                }
            ]
        });
        warningDialog.show();
    }

    function showSelectProductWarningDialog () {
        var warningDialog = new Dialog({
            header: 'Please select a product',
            content: 'Please select a product to search against',
            type: 'information',
            buttons: [
                {
                    caption: 'OK',
                    color: 'blue',
                    action: function () {
                        warningDialog.hide();
                    }.bind(this)
                }
            ]
        });
        warningDialog.show();
    }

    function testCasesSuccessfullySaved (testPlanTitle) {
        var options = Notifications.NOTIFICATION_TYPES.success;
        options.canDismiss = true;
        this.eventBus.publish(Constants.events.NOTIFICATION, 'Test cases added to ' + testPlanTitle, options);
    }
});
