define([
    'jscore/core',
    'jscore/base/jquery',
    'jscore/ext/utils/base/underscore',
    './TestCampaignEditContentRegionView',
    'widgets/Dialog',
    'widgets/SelectBox',
    'widgets/Notification',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../../common/ContextFilter',
    '../../../../common/Logger',
    '../../../../common/ObjectHelper',
    '../../../../common/AutocompleteInput/AutocompleteInput',
    '../../../../common/models/completion/CompletionsCollection',
    '../../models/TestCampaignModel',
    '../../models/AssignmentModel',
    '../../models/AssignmentsCollection',
    '../../../testCases/models/testCases/TestCaseModel',
    '../TestCampaignFormWidget/TestCampaignFormWidget',
    '../TestCampaignTestCasesWidget/TestCampaignTestCasesWidget',
    '../../../../common/dialogs/DialogFactory',
    '../../../testCases/models/testCases/TestCaseByIdCollection',
    '../../../testCases/models/testCases/TestCaseModel'

], function (core, $, _, View, Dialog, SelectBox, Notification, Constants, Animation, Navigation, ModelHelper,
             ContextFilter, Logger, ObjectHelper, AutocompleteInput, CompletionsCollection, TestPlan, Assignment,
             Assignments, TestCase, FormWidget, TestCasesWidget, DialogFactory, TestCaseByIdCollection, TestCaseModel) {
    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.user = {};
            this.eventBus = this.getContext().eventBus;
            this.testPlan = new TestPlan();
            this.testCases = new Assignments();
            this.messagesWidgets = [];
            this.copy = false;
        },

        onViewReady: function () {
            this.view.afterRender();

            this.eventBus.subscribe(Constants.events.CREATE_TEST_PLAN, this.createTestPlan, this);
            this.eventBus.subscribe(Constants.events.SAVE_TEST_PLAN, this.saveTestPlan, this);
            this.eventBus.subscribe(Constants.events.COPY_TEST_PLAN, this.copyTestPlanData, this);

            this.eventBus.subscribe(Constants.events.TEST_CAMPAIGN_FORM_FILTER_LOADED, this.setAutoCompleteCriteria, this);

            this.eventBus.subscribe('refreshTestCasesTable', this.refreshTestCaseTable, this);

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_TEST_PLAN_FORM, this.redrawPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_TEST_PLAN_FORM, this.onHideContent.bind(this));
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_TEST_PLAN_FORM, this.redrawPage.bind(this));

            this.detailsWidget = new FormWidget({
                region: this,
                model: this.testPlan,
                eventBus: this.eventBus
            });
            this.detailsWidget.attachTo(this.view.getDetailsBlock());

            this.testCaseCompletions = new CompletionsCollection();
            this.testCaseCompletions.setResource('test-cases');
            this.testCasesWidget = new TestCasesWidget({
                region: this,
                collection: this.testCases,
                completions: this.testCaseCompletions,
                completionRefresh: this.onTestCaseCompletion.bind(this)
            });
            this.testCasesWidget.attachTo(this.view.getTestCasesBlock());
        },

        onHideContent: function () {
            this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);
        },

        redrawPage: function (params) {
            if (Constants.pages.TEST_PLAN_CREATE === params.screenId || !params) {
                this.testPlan.clear();
                this.testCases.setModels([]);
            }

            updateCurrentView.call(this, params.itemId, params.screenId);
            this.detailsWidget.updateReferences();
        },

        onTestCaseCompletion: function (search, cb) {
            if (this.testCaseCompletions.featureIds && this.testCaseCompletions.productId) {
                this.testCaseCompletions.setSearch(search);
                this.testCaseCompletions.fetch({
                    reset: true,
                    statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                        200: function () {
                            var data = this.testCaseCompletions.toJSON();
                            cb(data);
                        }.bind(this)
                    })
                });
            } else {
                cb([]);
            }
        },

        onUserCompletion: function (search, cb) {
            this.userCompletionCollection.setSearch(search);
            this.userCompletionCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function (users) {
                        var data = this.createUserData(users);
                        cb(data);
                    }.bind(this)
                })
            });
        },

        updateSelectedUser: function (user) {
            this.user = user;
        },

        createUserData: function (users) {
            var userList = [];
            if (users.length > 0) {
                users.forEach(function (user) {
                    var userObj = {};
                    userObj.value = user.userName;
                    userObj.name = user.userName;
                    userObj.userId = user.externalId;
                    userObj.userName = user.userName;
                    userList.push(userObj);
                });
            }
            return userList;
        },

        linkTestCase: function (testCaseId) {
            if (!testCaseId) {
                return;
            }
            if (this.testCases.findWhere({'testCase.testCaseId': testCaseId})) {
                return;
            }
            var testCase = new TestCase();
            testCase.setId(testCaseId);
            testCase.fetch({
                reset: true,
                data: {
                    view: 'version'
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        var assignment = new Assignment();
                        applyAssignmentFields(testCase, assignment, true);
                        this.testCases.addModel(assignment);
                    }.bind(this)
                })
            });
        },

        removeTestCase: function (model) {
            var foundModel = {};
            this.testCases.each(function (modelItem, index) {
                if (modelItem.getTestCasePk() === model.testCase.id) {
                    foundModel = this.testCases.getAtIndex(index);
                }
            }.bind(this));
            this.testCases.removeModel(foundModel);
        },

        refreshTestCaseTable: function () {
            var models = this.testPlan.getTestPlanItems();
            this.testCases.setModels(models);
            this.testCasesWidget.updateCollection(this.testCases);
        },

        setProject: function () {
            var projectId = ContextFilter.getActiveProjectId();
            if (projectId) {
                this.testPlan.setProject({externalId: projectId});
            }
        },

        createTestPlan: function () {
            this.detailsWidget.updateProductDropAndFeatureDetails();
            var testCases = this.testCases.toJSON();
            this.testPlan.setTestPlanItems(testCases);
            this.testPlan.setHostname(getUrl.call(this));
            this.setProject();
            this.testPlan.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    201: function () {
                        Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(this.testPlan.getId()));
                    }.bind(this)
                })
            });
        },

        saveTestPlan: function () {
            var testCases = this.testCases.toJSON();
            if (this.copy) {
                testCases.forEach(function (testCase) {
                    // Remove model id to mimic "new" model.
                    testCase.id = null;
                });
            }
            this.detailsWidget.updateProductDropAndFeatureDetails();
            this.testPlan.setTestPlanItems(testCases);
            this.testPlan.setHostname(getUrl.call(this));
            this.testPlan.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    201: function (model) {
                        this.detailsWidget.clear();
                        Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(model.id));
                    }.bind(this),

                    200: function () {
                        this.detailsWidget.clear();
                        Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(this.testPlan.getId()));
                    }.bind(this)
                })
            });
        },

        copyTestPlanData: function () {
            this.saveTestPlan();
        },

        setAutoCompleteCriteria: function (selection) {
            if (selection.product && selection.feature) {
                this.testCaseCompletions.setProductId(selection.product.id);
                var featureIds = getFeatureIds.call(this, selection.feature);
                this.testCaseCompletions.setFeatureIds(featureIds);
            } else {
                this.testCaseCompletions.setProductId(null);
                this.testCaseCompletions.setFeatureIds(null);
            }

            var ids = _.map(selection.components, function (component) {
                return component.id;
            });
            this.testCaseCompletions.setComponentIds(ids);
        },

        requestAssignToUserAction: function () {
            this.userCompletionCollection = new CompletionsCollection();
            this.userCompletionCollection.setResource('users');

            this.userSelectBox = new AutocompleteInput({
                placeholder: 'Enter User ID',
                completions: this.completions,
                refresh: this.onUserCompletion.bind(this),
                userObject: this.updateSelectedUser.bind(this)
            });

            var selectedRows = this.testCasesWidget.getSelectedRows();
            bulkAction.call(this, selectedRows, showSelectUserDialog.bind(this, this.userSelectBox, selectedRows));
        },

        requestUpdateVersionAction: function () {
            var selectedRows = this.testCasesWidget.getSelectedRows();
            bulkAction.call(this, selectedRows, showUpdateTestCaseDialog.bind(this, selectedRows));
        },

        updateVersions: function (selectedTestCases) {
            if (selectedTestCases.length < 1) {
                return;
            }
            var ids = ObjectHelper.findAll(selectedTestCases, 'testCase.id'),
                testCases = new TestCaseByIdCollection();

            testCases.setIds(ids);
            testCases.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        testCases.each(function (newTestCase) {
                            var testCaseToEdit = this.testCases.findBy(function (model) {
                                return model.getTestCase().id === newTestCase.getId();
                            });
                            if (testCaseToEdit != null) {
                                applyAssignmentFields(newTestCase, testCaseToEdit, false);
                            }
                        }.bind(this));
                        this.testCases.trigger('reset');
                    }.bind(this)
                })
            });
        },

        updateTestCaseVersion: function (testCase, version) {
            var testCaseModel = new TestCaseModel();

            testCaseModel.setId(testCase.id);
            testCaseModel.fetch({
                reset: true,
                data: {
                    version: version
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        var testCaseToEdit = this.testCases.findBy(function (model) {
                            return model.getTestCase().id === testCase.id;
                        });
                        if (testCaseToEdit != null) {
                            applyAssignmentFields(testCaseModel, testCaseToEdit, false);
                        }

                        this.testCases.trigger('reset');
                    }.bind(this)
                })
            });
        },

        updateSelection: function (selectedItems) {
            this.testCases.each(function (model) {
                var testCaseId = model.getId();
                var foundItem = _.find(selectedItems, function (selectedItem) {
                    var selectedTestCaseId = selectedItem.options.model.id;
                    return testCaseId === selectedTestCaseId;
                });
                model.setAttribute('selected', foundItem !== undefined);
            });
        },

        addMultipleTestCases: function (data) {
            data.forEach(function (row) {
                var assignment = new Assignment();
                applyAssignmentFields(row, assignment, true);
                if (this.testCases.findWhere({'testCase.id': row.getId()})) {
                    return;
                }
                assignment.setAttribute('user', null);
                this.testCases.addModel(assignment);
            }.bind(this));
        }
    });

    function updateCurrentView (testPlanId, screenId) {
        switch (screenId) {
            case Constants.pages.TEST_PLAN_EDIT:
                ContextFilter.profileReady
                    .then(fetchTestCampaign.bind(this, testPlanId))
                    .then(updateWidgets.bind(this));
                break;
            case Constants.pages.TEST_PLAN_COPY:
                ContextFilter.profileReady
                    .then(fetchTestCampaign.bind(this, testPlanId, true))
                    .then(updateWidgets.bind(this));
                break;
            case Constants.pages.TEST_PLAN_CREATE:
            /* falls through */
            default:
                ContextFilter.profileReady
                    .then(updateWidgets.bind(this));
                break;
        }
    }

    function fetchTestCampaign (id, isCopy) {
        var df = $.Deferred();

        this.testPlan.setId(id);
        this.testPlan.fetch({
            data: {
                view: 'detailed'
            },
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    if (isCopy === true) {
                        this.testPlan.clearIds();
                        this.testPlan.setName(this.testPlan.getName() + ' - COPY');
                    }
                    var models = this.testPlan.getTestPlanItems();
                    this.testCases.setModels(models);
                    this.testCases.trigger('reset');
                    this.testCaseCompletions.setProductId(this.testPlan.getProductId());
                    this.testCaseCompletions.setComponentIds(getComponentIds.call(this));
                    df.resolve();
                }.bind(this)
            }, df)
        });
        return df.promise();
    }

    function getComponentIds () {
        var components = this.testPlan.getComponents();
        return _.map(components, function (component) {
            return component.id;
        });
    }

    function updateWidgets () {
        this.detailsWidget.updateUnboundFields();
    }

    function applyAssignmentFields (newTestCase, assignmentToEdit, addModelVersions) {
        assignmentToEdit.setTestCase({});
        assignmentToEdit.setTestCasePk(newTestCase.getId());
        assignmentToEdit.setTestCaseId(newTestCase.getTestCaseId());
        assignmentToEdit.setTestCaseTitle(newTestCase.getTitle());
        assignmentToEdit.setTestCaseComment(newTestCase.getComment());
        assignmentToEdit.setTestCaseDescription(newTestCase.getDescription());
        assignmentToEdit.setTestCaseSequenceNumber(newTestCase.getSequenceNumber());
        assignmentToEdit.setFeature(newTestCase.getFeature());
        assignmentToEdit.setTechnicalComponents(newTestCase.getTechnicalComponents());
        assignmentToEdit.setUser(null);

        if (addModelVersions) {
            assignmentToEdit.setTestCaseVersions(newTestCase.getTestCaseVersions());
        }
    }

    function showUpdateTestCaseDialog (selectedTestCases) {
        var dialog = new Dialog({
            header: 'Update Test Cases',
            type: 'confirmation',
            content: 'Update ' + selectedTestCases.length + ' selected test case(s) in this test Campaign to latest version?',
            optionalContent: new Notification({
                label: 'This action cannot be undone.',
                icon: 'warning',
                color: 'yellow',
                showCloseButton: false,
                autoDismiss: false
            }),
            buttons: [{
                caption: 'Update',
                color: 'blue',
                action: function () {
                    this.updateVersions(selectedTestCases);
                    this.testCasesWidget.clearAssignmentCheckboxes();
                    dialog.hide();
                }.bind(this)
            }, {
                caption: 'Cancel',
                action: function () {
                    dialog.hide();
                }.bind(this)
            }]
        });
        dialog.show();
    }

    function showSelectUserDialog (testPlanSelectBox, selectedTestCases) {
        var dialog = new Dialog({
            header: 'Select User',
            type: 'information',
            content: testPlanSelectBox,
            buttons: [
                {
                    caption: 'Assign',
                    color: 'green',
                    action: function () {
                        var selectedValueObj = testPlanSelectBox.getValueObj();
                        if (_.isEmpty(selectedValueObj)) {
                            return;
                        }
                        this.testCasesWidget.clearAssignmentCheckboxes();
                        applyAssignmentUser.call(this, selectedTestCases, selectedValueObj);
                        dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Unassign',
                    color: 'blue',
                    action: function () {
                        this.testCasesWidget.clearAssignmentCheckboxes();
                        applyAssignmentUser.call(this, selectedTestCases, null);
                        dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        this.testCasesWidget.clearAssignmentCheckboxes();
                        dialog.hide();
                    }.bind(this)
                }
            ]
        });

        dialog.show();
    }

    function applyAssignmentUser (selectedTestCases, selectedValueObj) {
        var testCaseToEdit = {};
        selectedTestCases.forEach(function (selectedTestCase) {
            this.testCases.each(function (modelItem, index) {
                if (modelItem.getTestCasePk() === selectedTestCase.testCase.id) {
                    testCaseToEdit = this.testCases.getAtIndex(index);
                }
            }.bind(this));
            testCaseToEdit.setAttribute('user', selectedValueObj);
        }.bind(this));

        this.testCases.trigger('reset');
    }

    function bulkAction (items, fn) {
        if (items.length) {
            fn.call(this);
        } else {
            DialogFactory.info({
                header: 'No Test Cases Selected',
                content: 'Please select test cases for this action.'
            }).show();
        }
    }

    function getUrl () {
        return window.location.protocol + '//' + window.location.host;
    }

    function getFeatureIds (features) {
        var featureIds = [];
        features.forEach(function (feature) {
            featureIds.push(feature.id);
        });

        return featureIds;
    }
});
