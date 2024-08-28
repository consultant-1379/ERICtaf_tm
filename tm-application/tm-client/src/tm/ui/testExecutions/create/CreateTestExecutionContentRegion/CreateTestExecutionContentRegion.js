define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'widgets/Accordion',
    'widgets/Notification',
    './CreateTestExecutionContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../../testCampaigns/models/TestCampaignModel',
    '../../../testCampaigns/models/TestCampaignTestCaseModel',
    '../../models/TestExecutionModel',
    '../../common/widgets/TestCampaignDetailsWidget/TestCampaignDetailsWidget',
    '../TestCaseMajorDetailsWidget/TestCaseMajorDetailsWidget',
    '../../../testCases/details/TestCaseStepsWidget/TestCaseStepsWidget',
    '../TestExecutionResultWidget/TestExecutionResultWidget',
    '../../models/TestExecutionsCollection',
    '../../../../common/jira/DefectFormWidget/DefectFormWidget',
    'widgets/Dialog',
    '../../../../common/models/jira/MetadataCollection',
    '../../../../common/models/UserProfileModel',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    'widgets/Button',
    '../../../../common/ReferenceHelper',
    'widgets/SelectBox',
    '../../../../common/models/ReferencesCollection',
    'container/api',
    '../../../../common/models/iso/IsoCollection',
    '../../../../common/widgets/GenericFileUpload/FileUpload'
], function (core, _, Accordion, Notification, View, Constants, Animation, Navigation, ModelHelper, TestPlanModel,
             TestPlanTestCaseModel, TestExecutionModel, TestPlanDetails, TestCaseMajorDetails, TestCaseSteps,
             TestExecutionResult, TestExecutionsCollection, DefectFormWidget, Dialog, MetadataCollection,
             UserProfile, Notifications, Button, ReferenceHelper, SelectBox, ReferencesCollection, containerApi,
             IsoCollection, FileUpload) {
    'use strict';

    return core.Region.extend({
        /* jshint validthis: true */

        View: View,

        init: function (options) {
            this.eventBus = this.getContext().eventBus;
            this.references = options.references;
            this.testPlanId = '';
            this.testCaseId = '';
            this.testPlan = new TestPlanModel();
            this.testCase = new TestPlanTestCaseModel();
            this.testExecutionModel = new TestExecutionModel();
            this.testExecutionCollection = new TestExecutionsCollection();
            this.userProfile = new UserProfile();
            this.metadataCollection = new MetadataCollection();
            this.messagesWidgets = [];
            this.projectId = '';
            this.projectCollection = options.projectCollection;
            this.isoCollection = new IsoCollection();
        },

        onViewReady: function () {
            this.view.afterRender();

            createWidgets.call(this);

            var animatedRefresh = function (params) {
                    this.testExecutionModel.clear();
                    this.testResult.updateFields();
                    this.refreshTestExecution(params.itemId);
                }.bind(this),
                hideErrorBlock = function () {
                    this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);
                }.bind(this);
            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_CREATE_TEST_EXECUTION, animatedRefresh.bind(this));
            this.animation.hideOn(Constants.events.HIDE_CREATE_TEST_EXECUTION, hideErrorBlock.bind(this));
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_CREATE_TEST_EXECUTION, animatedRefresh.bind(this));

            this.eventBus.subscribe(Constants.events.REFERENCES_RECEIVED, this.onReferencesReceived, this);
            this.eventBus.subscribe(Constants.events.SAVE_TEST_EXECUTION_RESULT, this.onSaveTestExecutionResult, this);
            this.eventBus.subscribe(Constants.events.JIRA_DEFECT_CREATED, this.updateResultWidgetDefectIds, this);
            this.eventBus.subscribe(Constants.events.PROJECT_CHANGED, this.verifyCurrentProject, this);

            createBugButton.call(this);
            hideDefectButton.call(this);
            fetchProjectReferences.call(this);
        },

        refreshTestExecution: function (idsString) {
            this.view.getElement().removeModifier('loaded');

            var ids = idsString.split(':');

            this.testPlanId = ids[0];
            this.testCaseId = ids[1];

            this.testPlan.setId(this.testPlanId);
            this.testPlan.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.testPlanDetails.updateUnboundParams();
                        updateTestResultIsos.call(this);
                    }.bind(this)
                })
            });

            this.testCase.setTestPlanId(this.testPlanId);
            this.testCase.setId(this.testCaseId);
            this.testCase.fetch({
                reset: true,
                data: {
                    view: 'detailed-test-case'
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.eventBus.publish(Constants.events.CHANGE_HEADING_TITLE, this.testCase.getTitle());
                        this.testCaseMajorDetails.updateFields();
                        this.testCaseSteps.redrawSteps(this.testCase.getSteps());
                        this.fetchTestExecutions();
                    }.bind(this)
                })
            });

            this.testExecutionModel.setTestPlanId(this.testPlanId);
            this.testExecutionModel.setTestCaseId(this.testCaseId);

            this.testExecutionCollection.setTestPlanId(this.testPlanId);
            this.testExecutionCollection.setTestCaseId(this.testCaseId);
            this.testExecutionCollection.setPerPage(1);
            this.testExecutionCollection.setPage(1);
        },

        fetchTestExecutions: function () {
            this.testExecutionCollection.fetch({
                reset: true,
                data: {
                    view: 'detailed'
                },
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function () {
                        this.populateExecutionModel();
                        this.populateTestExecutionWidget();
                        this.populateTestCaseStepsWidget();
                        this.view.getElement().setModifier('loaded');
                    }.bind(this)
                })
            });
        },

        populateExecutionModel: function () {
            this.latestTestExecutionModel = null;
            if (this.testExecutionCollection.size() > 0) {
                this.latestTestExecutionModel = this.testExecutionCollection.getAtIndex(0);
            }
        },

        populateTestExecutionWidget: function () {
            if (this.latestTestExecutionModel == null) {
                this.testResult.clear();
                return;
            }
            this.testResult.populateFields(this.latestTestExecutionModel);
        },

        populateTestCaseStepsWidget: function () {
            var testStepExecutions = [],
                verifyStepExecutions = [];
            if (this.latestTestExecutionModel != null) {
                testStepExecutions = this.latestTestExecutionModel.getAttribute('testStepExecutions');
                verifyStepExecutions = this.latestTestExecutionModel.getAttribute('verifyStepExecutions');
            }
            this.testCaseSteps.setExecutionResults(testStepExecutions, verifyStepExecutions);
        },

        onReferencesReceived: function () {
            this.testResult.updateReferencesData();
        },

        onSaveTestExecutionResult: function () {
            this.eventBus.publish(Constants.events.HIDE_ERROR_BLOCK);
            this.testResult.updateModelData();

            var testExecutionResults = this.testCaseSteps.getExecutionResults();
            this.testExecutionModel.setTestStepExecutions(testExecutionResults.testStepExecutions);
            this.testExecutionModel.setVerifyStepExecutions(testExecutionResults.verifyStepExecutions);

            this.saveTestExecution();
        },

        saveTestExecution: function () {
            this.testExecutionModel.save({}, {
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    201: function () {
                        this.fileWidget.setModel(this.testExecutionModel);
                        this.fileWidget.saveFiles();
                        var testPlanId =  this.testPlanId;
                        sendNotification.call(this, 'success', 'The result has successfully been updated.');
                        setTimeout(function () { Navigation.navigateTo(Navigation.getTestPlanExecutionUrl(testPlanId)); }, 2000);
                    }.bind(this)
                })
            });
        },

        verifyCurrentProject: function () {
            hideDefectButton.call(this);
            this.userProfile.fetch({
                reset: true,

                success: function (model) {
                    var currentProject = model;
                    fetchMetadata.call(this, currentProject);
                }.bind(this),

                error: function () {
                    sendNotification.call(this, 'error', 'Creating JIRA bug functionality is not available. ' +
                        'Could not fetch metadata from server');
                }.bind(this)
            });
        },

        updateResultWidgetDefectIds: function (defectId) {
            this.testResult.addDefectIdToMultiSelect(defectId);
        }

    });

    /*************** PRIVATE FUNCTIONS ******************/

    function createWidgets () {
        this.testPlanDetails = new TestPlanDetails({
            region: this,
            model: this.testPlan
        });

        this.accordion = new Accordion({
            title: 'Test Campaign Details',
            content: this.testPlanDetails
        });
        this.accordion.attachTo(this.view.getDetailsBlock());

        this.testCaseMajorDetails = new TestCaseMajorDetails({
            region: this,
            model: this.testCase
        });
        this.testCaseMajorDetails.attachTo(this.view.getTestCaseDetails());

        this.testCaseSteps = new TestCaseSteps({
            region: this,
            model: this.testCase,
            showResult: true
        });
        this.testCaseSteps.attachTo(this.view.getTestCaseSteps());

        this.fileWidget = new FileUpload({
            region: this,
            model: this.testExecutionModel,
            editMode: true,
            eventBus: this.eventBus,
            category: 'test-executions'
        });

        this.testResult = new TestExecutionResult({
            region: this,
            model: this.testExecutionModel,
            references: this.references,
            isoCollection: this.isoCollection,
            fileWidget: this.fileWidget
        });
        this.testResult.attachTo(this.view.getTestResult());

        this.referenceHelper = new ReferenceHelper({
            references: this.references
        });
    }

    function updateTestResultIsos () {
        this.testResult.updateIsoSelect([]);
        if (this.testPlan.getProduct().dropCapable && !this.testPlan.getDrop().defaultDrop && !this.testPlan.getProduct().name.includes('Ericsson Orchestrator')) {
            this.testResult.showIsoSelect();
            this.isoCollection.setDropId(this.testPlan.getDropId());
            this.isoCollection.fetch({
                reset: true,
                success: function (data) {
                    this.testResult.updateIsoSelect(data.toJSON());
                }.bind(this)
            });
        } else {
            this.isoCollection.reset();
            this.testResult.hideIsoSelect();
        }
    }

    function fetchMetadata (projectModel) {
        this.projectId = projectModel.getProjectExternalId();
        this.metadataCollection.setProjectId(this.projectId);
        this.metadataCollection.fetch({
            reset: true,

            success: function () {
                createDefectFormWidget.call(this, projectModel.getProject());
                showDefectButton.call(this);
            }.bind(this),

            error: function () {
                sendNotification.call(this, 'warning', 'Creating JIRA bug functionality is not available. ' +
                                                        'Could not fetch metadata from server');

                hideDefectButton.call(this);
            }.bind(this)

        });
    }

    function createBugButton () {
        this.button = new Button({
            caption: 'Create Bug',
            modifiers: [{name: 'color', value: 'red'}]
        });
        this.button.addEventHandler('click', function () {
            if (this.projectId == null) {
                createDialogBoxWithProjectSelect.call(this);
                return;
            }

            containerApi.getEventBus().publish('flyout:show', {
                header: 'Create Bug',
                content: this.defectForm,
                width: '50rem'
            });
        }, this);
        this.button.attachTo(this.view.getDefectButtonBlock());
    }

    function createDefectFormWidget (project) {
        this.defectForm = new DefectFormWidget({
            eventBus: this.eventBus,
            userProfile: this.userProfile,
            metadataCollection: this.metadataCollection,
            references: this.references
        });
        if (project != null) {
            this.defectForm.setProjectSelect(project);
        }
    }

    function sendNotification (type, message) {
        var options = Notifications.NOTIFICATION_TYPES[type];
        options.canDismiss = true;
        options.canClose = true;
        this.eventBus.publish(Constants.events.NOTIFICATION, message, options);
    }

    function showDefectButton () {
        this.view.showDefectButton();
    }

    function hideDefectButton () {
        this.view.hideDefectButton();
    }

    function createDialogBoxWithProjectSelect () {
        var projectSelectBox = createProjectSelectBox.call(this);

        this.dialog = new Dialog({
            header: 'Select a project you are working on:',
            content: projectSelectBox,
            buttons: [
                {
                    caption: 'OK',
                    color: 'green',
                    action: function () {
                        var selectedProject = projectSelectBox.getValue();
                        if (_.isEmpty(selectedProject)) {
                            return;
                        }
                        this.eventBus.publish(
                            Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                            selectedProject
                        );
                        this.dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        this.dialog.hide();
                    }.bind(this)
                }
            ]
        });
        this.dialog.show();
    }

    function createProjectSelectBox () {
        var referenceHelper = new ReferenceHelper({
            references: this.projectReferences
        });

        var selectBox = new SelectBox({
            items: referenceHelper.getReferenceItems('project'),
            modifiers: [
                {name: 'width', value: 'fullBlock'}
            ]
        });
        selectBox.view.getButton().setAttribute('id', 'TMS_CreateBug_DialogBox-projectSelect');
        return selectBox;
    }

    function fetchProjectReferences () {
        this.projectReferences = new ReferencesCollection();
        this.projectReferences.addReferences('project');
        this.projectReferences.fetch({
            reset: true
        });
    }

});
