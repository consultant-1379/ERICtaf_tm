define([
    'jscore/core',
    'jscore/ext/net',
    './TestCaseDetailsContentRegionView',
    'jscore/ext/utils/base/underscore',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../TestCaseDetailsWidget/TestCaseDetailsWidget',
    '../TestCaseStepsWidget/TestCaseStepsWidget',
    '../../models/testCases/TestCaseModel',
    '../../../../common/widgets/progressBlock/ProgressBlock',
    '../../../../common/DeleteDialog/DeleteDialog',
    'container/api',
    '../TestPlanLinksWidget/TestPlanLinksWidget',
    '../../../../common/ModelHelper',
    '../../../../common/AssociatedComments/CommentsWidget/AssociatedCommentsWidget',
    '../../../../common/models/AssociatedComments/PostsCollection',
    '../../../../common/models/UserProfileModel',
    '../../../../common/widgets/GenericFileUpload/FileUpload',
    'widgets/Dialog',
    '../../models/testCases/TestCasesListModel',
    '../../search/AddTestCaseTestCampaignWidget/AddTestCaseTestCampaignWidget',
    '../../../../common/notifications/NotificationRegion/NotificationRegion',
    '../../models/testCases/ReviewTestCaseModel',
    '../../../../common/AutocompleteInput/AutocompleteInput',
    '../../../../common/models/completion/CompletionsCollection',
    'widgets/SelectBox',
    '../../../../common/ComparisonHelper',
    '../../../../common/RadioButtons/RadioButtons'
], function (core, net, View, _, Constants, ContextFilter, Animation, Navigation, TestCaseDetailsWidget,
             TestCaseStepsWidget,
             TestCaseModel, ProgressBlock, DeleteDialog, containerApi, TestPlanLinksWidget, ModelHelper,
             AssociatedCommentsWidget, PostsCollection, UserProfileModel, FileUpload, Dialog, TestCasesListModel,
             AddTestCaseTestCampaignWidget, Notifications, ReviewTestCaseModel,
             AutocompleteInput, CompletionsCollection, SelectBox, ComparisonHelper, RadioButtons) {

    'use strict';

    return core.Region.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.currentModel = new TestCaseModel();
            this.compareModel = new TestCaseModel();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.detailsWidget = new TestCaseDetailsWidget({
                region: this,
                model: this.currentModel,
                references: this.options.references
            });
            this.detailsWidget.attachTo(this.view.getDetailsBlock());

            this.testStepsWidget = new TestCaseStepsWidget({
                region: this,
                model: this.currentModel
            });
            this.testStepsWidget.attachTo(this.view.getTestStepsBlock());

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_VIEW_TEST_CASE, this.updateTestCaseModel.bind(this));
            this.animation.hideOn(Constants.events.HIDE_VIEW_TEST_CASE);
            this.animation.markCurrentOn(Constants.events.MARK_VIEW_TEST_CASE_CURRENT,
                this.updateTestCaseModel.bind(this));

            this.eventBus.subscribe(Constants.events.TEST_CASE_REMOVE_REQUEST, this.onTestCaseRemoveRequest, this);
            this.eventBus.subscribe(Constants.events.SHOW_ASSOCIATED_TEST_PLANS_REQUEST, this.onShowTestPlansRequest, this);
            this.eventBus.subscribe(Constants.events.SHOW_ASSOCIATED_COMMENTS_REQUEST, this.onShowCommentsRequest, this);

            this.eventBus.subscribe(Constants.events.TEST_CASE_ATTACH_TO_TEST_CAMPAIGN_REQUEST, this.requestAddToTestPlan, this);

            this.eventBus.subscribe(Constants.events.TEST_CASE_UPDATE_COMMENTS_REQUEST, this.onUpdateComment, this);
            this.eventBus.subscribe(Constants.events.TEST_CASE_APPROVE_REQUEST, this.onApproveTestCase, this);
            this.eventBus.subscribe(Constants.events.SEND_FOR_REVIEW_REQUEST, this.onSendForReview, this);
            this.eventBus.subscribe(Constants.events.CANCEL_REVIEW_REQUEST, this.onCancelReview, this);
            this.eventBus.subscribe(Constants.events.SUBSCRIBE_TO_TEST_CASE, this.onSubscribe, this);
            this.eventBus.subscribe(Constants.events.UNSUBSCRIBE_FROM_TEST_CASE, this.onUnsubscribe, this);

            this.fileUploadWidget = new FileUpload({
                region: this,
                model: this.currentModel,
                editMode: false,
                category: 'test-cases'
            });
            this.fileUploadWidget.attachTo(this.view.getFilesHolder());

            createAutoComplete.call(this);
            getCurrentUser.call(this);

            this.view.getCompareCheckBox().addEventHandler('click', function () {
                if (this.view.getCompareCheckBox().getProperty('checked')) {
                    createCompareSelectBox.call(this);
                    var version = this.compareSelectBox.getValue();
                    getModelToCompare.call(this, version.title);
                } else {
                    destroyCompareSelectBox.call(this);
                    clearComparisons.call(this);
                    this.testStepsWidget.clearCompareTestSteps();
                }
            }.bind(this));
        },

        onUpdateComment: function (postsCollection) {
            if (this.associatedCommentsWidget) {
                this.associatedCommentsWidget.updatePosts(postsCollection);
            }
        },

        updateTestCaseModel: function (params) {
            var testCaseId = null;
            var version = null;
            if (params != null) {
                testCaseId = params.testCaseId || '';
                version = params.options.version || null;
            }

            if (this.view.getCompareCheckBox().getProperty('checked')) {
                this.view.getCompareCheckBox().trigger('click');
            }

            updateCurrentModel.call(this, testCaseId, version);
        },

        onTestCaseRemoveRequest: function () {
            if (!this.testCaseDeleteDialog) {
                this.removeTestCaseProgress = new ProgressBlock();
                this.testCaseDeleteDialog = new DeleteDialog({
                    item: 'Test Case',
                    optionalContent: this.removeTestCaseProgress,
                    action: function () {
                        submitRemoveTestCase.call(this);
                    }.bind(this)
                });
            }
            this.testCaseDeleteDialog.show();
        },

        onShowTestPlansRequest: function () {
            var testPlans = this.currentModel.getAssociatedTestPlans();
            containerApi.getEventBus().publish('flyout:show', {
                header: 'Associated Test Campaigns:',
                content: new TestPlanLinksWidget({
                    testPlans: testPlans
                })
            });
        },

        onShowCommentsRequest: function () {
            if (this.associatedCommentsWidget) {
                this.associatedCommentsWidget.destroy();
            }
            this.associatedCommentsWidget = new AssociatedCommentsWidget({
                eventBus: this.eventBus,
                resourceRoot: PostsCollection.TEST_CASES_ROOT
            });

            containerApi.getEventBus().publish('flyout:show', {
                header: 'Associated Comments:',
                content: this.associatedCommentsWidget
            });

            this.associatedCommentsWidget.setPostFetchParameters(
                this.currentModel.getId());
            this.associatedCommentsWidget.setCurrentUser(this.userProfile);
        },

        requestAddToTestPlan: function () {
            var addTestCaseTestCampaignWidget = new AddTestCaseTestCampaignWidget({
                profileProduct: this.currentModel.getProduct()
            });

            showSelectTestPlanDialog.call(this, addTestCaseTestCampaignWidget);
        },

        onSendForReview: function () {
            showReviewDialog.call(this);
        },

        onCancelReview: function () {
            showCancelReviewDialog.call(this);
        },

        onApproveTestCase: function () {
            showApprovalDialog.call(this);
        },

        onGroupCompletion: function (search, cb) {
            this.groupCompletionCollection.setSearch(search);
            this.groupCompletionCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function (groups) {
                        var data = createGrouprData(groups);
                        cb(data);
                    }.bind(this)
                })
            });
        },

        onUserCompletion: function (search, cb) {
            this.userCompletionCollection.setSearch(search);
            this.userCompletionCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                    200: function (users) {
                        var data = createUserData(users);
                        cb(data);
                    }.bind(this)
                })
            });
        },

        updateSelectedGroup: function (group) {
            this.group = group;
        },

        onSubscribe: function () {
            sendSubscriptionRequest.call(this, 'post', true);
        },

        onUnsubscribe: function () {
            sendSubscriptionRequest.call(this, 'delete', false);
        }
    });

    function checkIfUserIsSubscribed () {
        var testCaseId = this.currentModel.toJSON().testCaseId;
        var userId = this.userProfile.toJSON().userId;

        net.ajax({
            url: Navigation.TEST_CASE_SUBSCRIPTION_LINK,
            dataType: 'json',
            data: {
                testCaseId: testCaseId,
                userId: userId
            },
            success: function (data) {
                publishSubscriptionEvent.call(this, data);
            }.bind(this)
        });
    }

    function sendSubscriptionRequest (type, subscribe) {
        var testCaseId = this.currentModel.toJSON().testCaseId;
        var userId = this.userProfile.toJSON().userId;
        var requestData = {testCaseId: testCaseId, userId: userId, subscribed: subscribe};
        var json = JSON.stringify(requestData);

        net.ajax({
            url: Navigation.TEST_CASE_SUBSCRIPTION_LINK,
            contentType: 'application/json',
            dataType: 'json',
            type: type,
            data: json,
            success: function (data) {
                publishSubscriptionEvent.call(this, data);
            }.bind(this)
        });
    }

    function publishSubscriptionEvent (data) {
        if (data.subscribed === true) {
            this.eventBus.publish(Constants.events.USER_IS_SUBSCRIBED_TO_TEST_CASE);
        } else {
            this.eventBus.publish(Constants.events.USER_IS_NOT_SUBSCRIBED_TO_TEST_CASE);
        }
    }

    function submitRemoveTestCase () {
        this.removeTestCaseProgress.showProgress();

        this.currentModel.destroy({
            success: function () {
                this.removeTestCaseProgress.hideProgress();
                this.testCaseDeleteDialog.hide();
                Navigation.navigateTo(Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch,
                    ContextFilter.searchQuery));
            }.bind(this),
            error: function (model, data) {
                var messages = [];
                messages.push(data.getResponseJSON().message);
                this.removeTestCaseProgress.showMessages(messages);
            }.bind(this),
            statusCode: {
                401: function () {
                    this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                }.bind(this)
            }
        });
    }

    function updateCurrentModel (testCaseId, version) {
        this.eventBus.publish(Constants.events.TEST_CASE_COMMENTS_ON_ID_FETCHED, null);
        this.view.getElement().removeModifier('loaded');
        this.currentModel.setId(testCaseId);
        this.currentModel.fetch({
            data: {
                version: version,
                view: 'detailed'
            },
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function (data) {
                    this.eventBus.publish(Constants.events.TEST_CASE_COMMENTS_ON_ID_FETCHED, this.currentModel.getId());
                    this.eventBus.publish(Constants.events.CHANGE_HEADING_TITLE, data.title);
                    var users = [];
                    if (data.reviewGroup) {
                        users = data.reviewGroup.users;
                    } else if (data.reviewUser) {
                        users = [data.reviewUser];
                    }
                    this.eventBus.publish(Constants.events.TEST_CASE_REVIEW_GROUP_USERS, users,
                        this.userProfile, data.testCaseStatus);
                    this.eventBus.publish(Constants.events.TEST_CASE_STATUS, data.testCaseStatus);
                    this.eventBus.publish(Constants.events.TEST_CASE_UPDATE_VERSIONS);
                    this.detailsWidget.updateFields();
                    this.testStepsWidget.redrawSteps(data.testSteps);
                    this.view.getElement().setModifier('loaded');
                    checkIfUserIsSubscribed.call(this);
                    fetchFiles.call(this);
                    Navigation.replaceUrlWith(Navigation.getTestCaseVersionUrl(data.testCaseId, data.version));
                }.bind(this),
                401: function () {
                    this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                }.bind(this),
                404: function () {
                    showMissingTestCaseDialog.call(this);
                }.bind(this)
            })
        });
    }

    function getModelToCompare (version) {
        var id = this.currentModel.getId();
        this.compareModel.setId(id);
        this.compareModel.fetch({
            data: {
                version: version,
                view: 'detailed'
            },
            reset: true,
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    compareModels.call(this, this.compareModel);
                }.bind(this),
                401: function () {
                    this.eventBus.publish(Constants.events.AUTHENTICATION_REQUIRED);
                }.bind(this),
                404: function () {
                    showMissingTestCaseDialog.call(this);
                }.bind(this)
            })
        });
    }

    function getCurrentUser () {
        this.userProfile = new UserProfileModel();
        this.userProfile.fetch({
            reset: true
        });
    }

    function fetchFiles () {
        this.fileUploadWidget.fetchFiles(this.currentModel);
    }

    function showSelectTestPlanDialog (content) {
        var dialog = new Dialog({
            header: 'Select Test Campaign',
            type: 'information',
            content: content,
            optionalContent: 'Please choose a test campaign based on the filters for drop and feature.',
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
                        var selected = this.currentModel;
                        if (selected.length === 0) {
                            return;
                        }

                        addTestCaseToTestPlan.call(this, selected, testPlanId, testPlanTitle);
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

    function showApprovalDialog () {
        var reviewGroup = this.currentModel.getReviewGroup();
        var reviewGroupId = null;
        var reviewUser = this.currentModel.getReviewUser();
        var reviewUserId = null;
        if (reviewUser) {
            reviewUserId = reviewUser.userId;
        }

        if (reviewGroup) {
            reviewGroupId = reviewGroup.id;
        }

        var dialog = new Dialog({
            header: 'Test Case Approval',
            type: 'confirmation',
            content: 'Please choose how you want to approve.',
            buttons: [
                {
                    caption: 'Approve Minor Version',
                    color: 'green',
                    action: function () {
                        sendReview.call(this, 'Approved', 'minor', reviewGroupId, reviewUserId);
                        dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Approve Major Version',
                    color: 'blue',
                    action: function () {
                        sendReview.call(this, 'Approved', 'major', reviewGroupId, reviewUserId);
                        dialog.hide();
                    }.bind(this)
                },
                {
                    caption: 'Reject',
                    color: 'red',
                    action: function () {
                        sendReview.call(this, 'Rejected', 'unknown', reviewGroupId, reviewUserId);
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

    function showReviewDialog () {
        var userSelect = false;
        if (this.radioWidget) {
            this.radioWidget.destroy();
        }

        this.radioWidget = new RadioButtons({
            radioOptions: ['I want to send to a single user']
        });

        var dialog = new Dialog({
            header: 'Please select a review group',
            type: 'information',
            content: this.groupSelectBox,
            optionalContent: this.userSelectBox,
            buttons: [
                {
                    caption: 'Send For Review',
                    color: 'blue',
                    action: function () {
                        if (userSelect) {
                            var user = this.userSelectBox.getValueObj();
                            sendReview.call(this, 'Review', 'unknown', null, user.userId);
                        } else {
                            var group = this.groupSelectBox.getValueObj();
                            sendReview.call(this, 'Review', 'unknown', group.id);
                        }

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

        this.radioWidget.addEventHandler('selectedCheckBox', function () {
            dialog.setContent(this.userSelectBox);
            userSelect = true;
        }.bind(this));

        dialog.setOptionalContent(this.radioWidget);
        dialog.show();
    }

    function showCancelReviewDialog () {
            var dialog = new Dialog({
                header: 'Cancel Review',
                type: 'warning',
                content: 'Are you sure you want to cancel',
                buttons: [
                    {
                        caption: 'Yes',
                        color: 'blue',
                        action: function () {
                           sendReview.call(this, 'Cancelled', 'unknown');
                           dialog.hide();
                        }.bind(this)
                    },
                    {
                        caption: 'No',
                        action: function () {
                            dialog.hide();
                        }
                    }
                ]
            });

            dialog.show();
        }

    function addTestCaseToTestPlan (testCase, testPlanId, testPlanTitle) {
        var testCasesCollection = new TestCasesListModel();
        testCasesCollection.setTestPlanId(testPlanId);
        testCasesCollection.setTestCases([testCase]);
        testCasesCollection.save([], {
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                204: function () {
                    var options = Notifications.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION, 'Test case added to ' + testPlanTitle, options);
                    updateCurrentModel.call(this, this.currentModel.getId(), '');
                }.bind(this)
            })
        });
    }

    function sendReview (status, type, reviewGroupId, userId) {
        var testCaseModel = new ReviewTestCaseModel();
        testCaseModel.setTestCaseId(this.currentModel.getId());
        testCaseModel.setStatus(status);
        testCaseModel.setType(type);

        if (reviewGroupId) {
            testCaseModel.setReviewGroupId(reviewGroupId);
        }

        if (userId) {
            testCaseModel.setReviewUserId(userId);
        }

        testCaseModel.save({}, {
            type: 'PUT',
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    var options = Notifications.NOTIFICATION_TYPES.success;
                    options.canDismiss = true;
                    this.eventBus.publish(Constants.events.NOTIFICATION,
                        'Test case was successfully updated with status: ' + status, options);
                    updateCurrentModel.call(this, this.currentModel.getId(), '');
                }.bind(this)
            })
        });
    }

    function createAutoComplete () {
        this.groupCompletionCollection = new CompletionsCollection();
        this.groupCompletionCollection.setResource('review-group');

        this.userCompletionCollection = new CompletionsCollection();
        this.userCompletionCollection.setResource('users');

        this.groupSelectBox = new AutocompleteInput({
            placeholder: 'Enter review group name',
            completions: this.completions,
            refresh: this.onGroupCompletion.bind(this),
            userObject: this.updateSelectedGroup.bind(this)
        });

        this.userSelectBox = new AutocompleteInput({
            placeholder: 'Enter user name',
            completions: this.completions,
            refresh: this.onUserCompletion.bind(this),
            userObject: this.updateSelectedGroup.bind(this)
        });
    }

    function createGrouprData (groups) {
        var groupList = [];
        if (groups.length > 0) {
            groups.forEach(function (group) {
                var groupObj = {};
                groupObj.value = group.name;
                groupObj.name = group.name;
                groupObj.id = group.id;
                groupList.push(groupObj);
            });
        }
        return groupList;
    }

    function createUserData (users) {
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
    }

    function createCompareSelectBox () {
        destroyCompareSelectBox.call(this);
        this.compareSelectBox = new SelectBox();

        var versions = this.currentModel.getTestCaseVersions();
        versions.forEach(function (data) {
            data.name = 'Version ' + data.title;
        }.bind(this));

        this.compareSelectBox.addEventHandler('change', function () {
            var version = this.compareSelectBox.getValue();
            getModelToCompare.call(this, version.title);
        }.bind(this));

        if (versions.length > 1) {
            this.compareSelectBox.setItems(versions);
            this.compareSelectBox.setValue(versions[1]);
            this.compareSelectBox.attachTo(this.view.getDifferenceDiv());
        } else {
            this.view.getCompareCheckBox().setProperty('checked', false);
            var options = Notifications.NOTIFICATION_TYPES.warning;
            options.canDismiss = true;
            this.eventBus.publish(Constants.events.NOTIFICATION, 'Test case only has 1 version', options);
        }
    }

    function destroyCompareSelectBox () {
        if (this.compareSelectBox) {
            this.compareSelectBox.destroy();
        }
    }

    function showMissingTestCaseDialog () {
        this.missingTestCaseDialog = new Dialog({
            header: 'Test case does not exist',
            type: 'error',
            content: ' ',
            buttons: [
                {
                    caption: 'OK',
                    color: 'darkBlue',
                    action: function () {
                        this.missingTestCaseDialog.destroy();
                        Navigation.goToPreviousPage();
                    }.bind(this)
                }
            ]
        });
        this.missingTestCaseDialog.show();
    }

    function addToArrayIfExists (list, item) {
        if (item !== null) {
            list.push(item);
        }
    }

    function compareModels (model) {
        clearComparisons.call(this);
        this.testStepsWidget.clearCompareTestSteps();
        this.comparison = [];

        compareGroup1.call(this, model);
        compareGroup2.call(this, model);
        compareGroup3.call(this, model);
    }

    function clearComparisons () {
        if (this.comparison) {
            this.comparison.forEach(function (element) {
                element.setText('');
            }.bind(this));
        }
    }

    function compareGroup1 (model) {
        var result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getTitle(),
            model.getTitle(),
            this.detailsWidget.view.getTitleCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getDescription(),
            model.getDescription(),
            this.detailsWidget.view.getDescriptionCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getType(),
            model.getType(),
            this.detailsWidget.view.getTypeCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getFeatureTitle(),
            model.getFeatureTitle(),
            this.detailsWidget.view.getFeatureCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getComponentTitles().join(','),
            model.getComponentTitles().join(','),
            this.detailsWidget.view.getComponentCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getPriority(),
            model.getPriority(),
            this.detailsWidget.view.getPriorityCompare());

        addToArrayIfExists(this.comparison, result);
    }

    function compareGroup2 (model) {
        var result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getGroups().join(', '),
            model.getGroups().join(', '),
            this.detailsWidget.view.getGroupCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getContexts().join(', '),
            model.getContexts().join(', '),
            this.detailsWidget.view.getContextCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getPreCondition(),
            model.getPreCondition(),
            this.detailsWidget.view.getPreConditionCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getExecutionType(),
            model.getExecutionType(),
            this.detailsWidget.view.getExecutionTypeCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareBooleanAndAddText(
            this.currentModel.getAutomationCandidateTitle(),
            model.getAutomationCandidateTitle(),
            this.detailsWidget.view.getAutomationCandidateCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getTestCaseStatus(),
            model.getTestCaseStatus(),
            this.detailsWidget.view.getTestCaseStatusCompare());

        addToArrayIfExists(this.comparison, result);
    }

    function compareGroup3 (model) {
        var result = ComparisonHelper.compareBooleanAndAddText(
            this.currentModel.isIntrusive(),
            model.isIntrusive(),
            this.detailsWidget.view.getIntrusiveCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getIntrusiveComment(),
            model.getIntrusiveComment(),
            this.detailsWidget.view.getIntrusiveCommentCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getTeamName(),
            model.getTeamName(),
            this.detailsWidget.view.getTeamCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getSuiteName(),
            model.getSuiteName(),
            this.detailsWidget.view.getSuiteCompare());

        addToArrayIfExists(this.comparison, result);

        result = ComparisonHelper.compareTextAndAddText(
            this.currentModel.getRequirementIds().join(', '),
            model.getRequirementIds().join(', '),
            this.detailsWidget.view.getRequirementsCompare());

        addToArrayIfExists(this.comparison, result);

        this.testStepsWidget.compareTestSteps(model);
    }
});
