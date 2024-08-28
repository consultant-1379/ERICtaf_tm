define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseDetailsBarRegionView',
    'widgets/SelectBox',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Animation',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink',
    '../../../../common/Navigation',
    '../../../../common/ModelHelper',
    '../../models/testCases/TestCaseVersionsCollection',
    '../../../../common/models/AssociatedComments/PostsCollection',
    '../../../../common/models/UserProfileModel'
], function (ActionBarRegion, View, SelectBox, Constants, ContextFilter, Animation, ActionIcon, ActionLink,
             Navigation, ModelHelper, TestCaseVersionsCollection, PostsCollection, UserProfileModel) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,
        APPROVED: 'Approved',
        REVIEW: 'Review',
        PRELIMINARY: 'Preliminary',
        REJECTED: 'Rejected',
        CANCELLED: 'Cancelled',

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;

            this.objectId = null;

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            initCreateActionLink.call(this);
            initVersions.call(this);
            initEditActionLink.call(this);
            initRemoveActionLink.call(this);
            initSendForReview.call(this);
            initCopyActionLink.call(this);
            initShowCommentsActionLink.call(this);
            initShowTestPlansActionLink.call(this);
            initAddToTestCampaignActionLink.call(this);
            initApprovals.call(this);
            initCancelReview.call(this);
            initSubscribeActionLink.call(this);
            initUnsubscribeActionLink.call(this);
            initCopyURLActionLink.call(this);
            initChangeReviewer.call(this);

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.hideOn(Constants.events.HIDE_VIEW_TEST_CASE,
                this.stopCommentsRefresh.bind(this));

            this.animation.showOn(Constants.events.SHOW_VIEW_TEST_CASE,
                this.startCommentsRefresh.bind(this));

            this.animation.markCurrentOn(Constants.events.MARK_VIEW_TEST_CASE_CURRENT,
                this.startCommentsRefresh.bind(this));

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.goToPreviousPage();
            }, this);

            this.eventBus.subscribe(Constants.events.TEST_CASE_COMMENTS_ON_ID_FETCHED,
                this.onGetIdForComments.bind(this));
            this.eventBus.subscribe(Constants.events.TEST_CASE_UPDATE_VERSIONS, this.onUrlItemIdReceived.bind(this));
            this.eventBus.subscribe(Constants.events.TEST_CASE_REVIEW_GROUP_USERS, this.onTestCaseUsers.bind(this));
            this.eventBus.subscribe(Constants.events.TEST_CASE_STATUS, this.onTestCaseStatusType.bind(this));
            this.eventBus.subscribe(Constants.events.USER_IS_SUBSCRIBED_TO_TEST_CASE,
                this.showUnsubscribeButton.bind(this));
            this.eventBus.subscribe(Constants.events.USER_IS_NOT_SUBSCRIBED_TO_TEST_CASE,
                this.showSubscribeButton.bind(this));

            document.addEventListener('copy', function (clipboardEvent) {
                if (this.copyURLSelected) {
                    clipboardEvent.preventDefault();
                    clipboardEvent.clipboardData.setData('Text', this.copyURL);
                }
            }.bind(this));
        },

        onGetIdForComments: function (params) {
            this.objectId = params;
            refreshShowCommentsActionLinkLabel.call(this);
        },

        stopCommentsRefresh: function () {
            this.refresh = false;
        },

        startCommentsRefresh: function () {
            this.refresh = true;
        },

        onUrlItemIdReceived: function () {
            this.editActionLink.setUrl(Navigation.getTestCaseEditUrl(this.getUrlItemId()));
            this.versionsCollection.setTestCaseId(this.getUrlItemId());
            this.versionsCollection.fetch({
                reset: true
            });
        },

        onRemoveTestCaseClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_REMOVE_REQUEST);
        },

        onCopyTestCaseClick: function (event) {
            event.preventDefault();
            var versionObj = this.versionsSelectBox.getValue();
            Navigation.navigateTo(Navigation.getTestCaseCopyUrl(this.getUrlItemId(), versionObj.value));
        },

        onVersionsReset: function (collection) {

            var items = [],
                selected = null,
                version = this.getFlag('version'),
                maxSequenceNumber = 0;

            collection.each(function (model) {
                var sequenceNumber = model.getSequenceNumber();

                var item = createVersionObj.call(this, sequenceNumber);
                items.push(item);

                maxSequenceNumber = Math.max(sequenceNumber, maxSequenceNumber);

                if (version === sequenceNumber) {
                    selected = item;
                }
            });

            if (items.length > 0) {
                this.versionsSelectBox.setItems(items);
                this.versionsSelectBox.setValue(selected || items[0]);
            }

            hideOrShowButtons.call(this, selected, maxSequenceNumber.toFixed(1));
        },

        onVersionsChange: function () {
            var item = this.versionsSelectBox.getValue();
            if (item != null) {
                Navigation.replaceUrlWith(Navigation.getTestCaseVersionUrl(this.getUrlItemId(), item.value));
            }
        },

        onShowTestPlansClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.SHOW_ASSOCIATED_TEST_PLANS_REQUEST);
        },

        onShowCommentsClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.SHOW_ASSOCIATED_COMMENTS_REQUEST);
        },

        onAddToTestPlanLinkClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_ATTACH_TO_TEST_CAMPAIGN_REQUEST);
        },

        onReviewClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.SEND_FOR_REVIEW_REQUEST);
        },

        onCancelReviewClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.CANCEL_REVIEW_REQUEST);
        },

        onApproveClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_APPROVE_REQUEST);
        },

        onSubscribeClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.SUBSCRIBE_TO_TEST_CASE);
        },

        onUnsubscribeClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.UNSUBSCRIBE_FROM_TEST_CASE);
        },

        onCopyURLClick: function () {
            this.baseURL = window.location.href;
            if (this.baseURL.includes('/version')) {
                this.baseURL = window.location.href.split('/version')[0];
            }
            this.copyURL = this.baseURL;
            this.copyURLSelected = true;
            document.execCommand('copy');
            this.copyURLSelected = false;
        },

        showSubscribeButton: function () {
            this.view.showSubscribeButton();
        },

        showUnsubscribeButton: function () {
            this.view.showUnsubscribeButton();
        },

        onTestCaseUsers: function (users, userProfile, statusType) {
            var currentUser = userProfile.toJSON();
            var found = false;
            if (users.length === 0 || statusType.title === this.APPROVED) {
                this.view.hideApproveButton();
            } else {
                for (var index in users) {
                    if (users[index].userId === currentUser.userId) {
                        this.view.showApproveButton();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.view.hideApproveButton();
                }
            }
        },

        onTestCaseStatusType: function (statusType) {
            if (statusType) {
                switch (statusType.title) {
                    case this.PRELIMINARY:
                    case this.CANCELLED:
                    case this.REJECTED:
                        hideOrShowEditReviewButtons.call(this, true);
                        hideOrShowChangeReviewerButtons.call(this, false);
                        this.view.hideCancelReviewButton();
                        break;
                    case this.REVIEW:
                        hideOrShowEditReviewButtons.call(this, false);
                        hideOrShowChangeReviewerButtons.call(this, true);
                        this.view.showCancelReviewButton();
                        break;
                    case this.APPROVED:
                        hideOrShowEditReviewButtons.call(this, false);
                        hideOrShowChangeReviewerButtons.call(this, false);
                        this.view.hideCancelReviewButton();
                        break;
                }
            }
        }

    });

    function initCreateActionLink () {
        this.createActionLink = new ActionLink({
            icon: {iconKey: 'plus', interactive: true, title: 'Create New Test Case'},
            link: {text: 'Create New Test Case'},
            url: Navigation.getTestCaseCreateUrl()
        });
        this.createActionLink.attachTo(this.view.getCreateTestCaseLinkHolder());
    }

    function initVersions () {
        this.versionsSelectBox = new SelectBox();
        this.versionsSelectBox.attachTo(this.view.getVersionsHolder());
        this.versionsSelectBox.addEventHandler('change', this.onVersionsChange, this);
        this.versionsSelectBox.view.getButton().setAttribute('id', 'TMS_TestCaseDetails_ViewTestCaseBar-versionSelect');

        this.versionsCollection = new TestCaseVersionsCollection();
        this.versionsCollection.addEventHandler('reset', this.onVersionsReset, this);
    }

    function initEditActionLink () {
        this.editActionLink = new ActionLink({
            icon: {iconKey: 'edit', interactive: true, title: 'Edit'},
            link: {text: 'Edit'},
            url: '#'
        });
        this.editActionLink.attachTo(this.view.getEditLinkHolder());
    }

    function initRemoveActionLink () {
        this.removeActionLink = new ActionLink({
            icon: {iconKey: 'delete', interactive: true, title: 'Delete'},
            link: {text: 'Delete'},
            action: this.onRemoveTestCaseClick.bind(this)
        });
        this.userProfile = new UserProfileModel();
        this.userProfile.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                200: function (data) {
                    if (data.administrator) {
                        this.removeActionLink.attachTo(this.view.getRemoveLinkHolder());
                    }
                }.bind(this)
            })
        });
    }

    function initCopyActionLink () {
        this.copyActionLink = new ActionLink({
            icon: {iconKey: 'copy', interactive: true, title: 'Copy'},
            link: {text: 'Copy'},
            action: this.onCopyTestCaseClick.bind(this)
        });
        this.copyActionLink.attachTo(this.view.getCopyLinkHolder());
    }

    function initShowTestPlansActionLink () {
        this.showTestPlansActionLink = new ActionLink({
            icon: {iconKey: 'expand', interactive: true, title: 'Show Associated Test Campaigns'},
            link: {text: 'Show Test Campaigns'},
            action: this.onShowTestPlansClick.bind(this)
        });
        this.showTestPlansActionLink.attachTo(this.view.getShowTestPlansLinkHolder());
    }

    function initShowCommentsActionLink () {
        this.showCommentsActionLink = new ActionLink({
            icon: {iconKey: 'expand', interactive: true, title: getCommentButtonLabel()},
            link: {text: getCommentButtonLabel()},
            action: this.onShowCommentsClick.bind(this)
        });
        this.showCommentsActionLink.attachTo(this.view.getShowCommentsLinkHolder());
    }

    function initSendForReview () {
        this.sendForReview = new ActionLink({
            icon: {iconKey: 'eye', interactive: true, title: 'Send For Review'},
            link: {text: 'Send For Review'},
            action: this.onReviewClick.bind(this)
        });
        this.sendForReview.attachTo(this.view.getSendForReviewHolder());
    }

    function initChangeReviewer () {
        this.changeReviewer = new ActionLink({
            icon: {iconKey: 'eyeLine', interactive: true, title: 'Reassign Reviewer'},
            link: {text: 'Reassign Reviewer'},
            action: this.onReviewClick.bind(this)
        });
        this.changeReviewer.attachTo(this.view.getChangeReviewerHolder());
    }

    function initSubscribeActionLink () {
        this.subscribeActionLink = new ActionLink({
            icon: {iconKey: 'mail', interactive: true, title: 'Subscribe to changes'},
            link: {text: 'Subscribe to changes'},
            action: this.onSubscribeClick.bind(this)
        });
        this.subscribeActionLink.attachTo(this.view.getSubscribeHolder());
    }

    function initUnsubscribeActionLink () {
        this.unsubscribeActionLink = new ActionLink({
            icon: {iconKey: 'undo', interactive: true, title: 'Unsubscribe'},
            link: {text: 'Unsubscribe'},
            action: this.onUnsubscribeClick.bind(this)
        });
        this.unsubscribeActionLink.attachTo(this.view.getUnsubscribeHolder());
    }

    function initCopyURLActionLink () {
        this.copyURLActionLink = new ActionLink({
            icon: {iconKey: 'link', interactive: true, title: 'Copy Link (Latest Version)'},
            link: {text: 'Copy Link (Latest Version)'},
            action: this.onCopyURLClick.bind(this)
        });
        this.copyURLActionLink.attachTo(this.view.getCopyURLLinkHolder());
    }

    function initApprovals () {
        this.approvalActionLink = new ActionLink({
            icon: {iconKey: 'simpleGreenTick', interactive: true, title: 'Approve'},
            link: {text: 'Approve'},
            action: this.onApproveClick.bind(this)
        });
        this.approvalActionLink.attachTo(this.view.getApprovalHolder());
    }

    function initCancelReview () {
        this.cancelReview = new ActionLink({
            icon: {iconKey: 'close_red', interactive: true, title: 'Cancel Review'},
            link: {text: 'Cancel Review'},
            action: this.onCancelReviewClick.bind(this)
        });
        this.cancelReview.attachTo(this.view.getCancelReviewHolder());
    }

    function refreshShowCommentsActionLinkLabel () {
        if (this.objectId) {
            this.refreshCommentsLabelTimer = setTimeout(function () {
                if (this.refresh) {
                    refreshShowCommentsActionLinkLabel.call(this);
                }
            }.bind(this), 5000);
        } else {
            setButtonLabel.call(this);
        }

        if (!this.showCommentsActionLink || !this.objectId) {
            return;
        }

        var posts = new PostsCollection();
        posts.setResourceRoot(PostsCollection.TEST_CASES_ROOT);
        posts.setObjectId(this.objectId);

        posts.fetch({
            reset: true,
            data: {
                view: 'detailed'
            },
            statusCode: ModelHelper.statusCodeHandler(this.eventBus, {
                200: function () {
                    setButtonLabel.call(this, posts.getTotalCommentsCount());
                    this.eventBus.publish(Constants.events.TEST_CASE_UPDATE_COMMENTS_REQUEST, posts);
                }.bind(this)
            })
        });
    }

    function hideOrShowButtons (selected, maxSequenceNumber) {
        // ~ if latest version sequence used
        if (selected) {
            if (selected.value === maxSequenceNumber) {
                this.view.showEditButton();
            } else {
                this.view.hideEditButton();
            }
        }
    }

    function getCommentButtonLabel (postsCount) {
        var labelString = 'Show Comments';
        if (!postsCount) {
            return labelString;
        }
        return labelString + ' (' + postsCount + ')';
    }

    function setButtonLabel (postsCount) {
        var labelText = getCommentButtonLabel(postsCount);
        this.showCommentsActionLink.setTitle(labelText);
        this.showCommentsActionLink.setLinkText(labelText);
    }

    function initAddToTestCampaignActionLink () {
        this.addToTestPlanActionLink = new ActionLink({
            icon: {iconKey: 'attach', interactive: true, title: 'Add to Test Campaign'},
            link: {text: 'Add to Test Campaign'},
            action: this.onAddToTestPlanLinkClick.bind(this)
        });
        this.addToTestPlanActionLink.attachTo(this.view.getAddToTestCampaignLinkHolder());
    }

    function hideOrShowEditReviewButtons (show) {
        if (show) {
            this.view.showReviewButton();
        } else {
            this.view.hideReviewButton();
        }
    }

    function hideOrShowChangeReviewerButtons (show) {
        if (show) {
            this.view.showChangeReviewerButton();
        } else {
            this.view.hideChangeReviewerButton();
        }
    }

    function createVersionObj (sequenceNumber) {
        var title = 'Version ' + sequenceNumber;
        var item = {
            name: title,
            value: sequenceNumber,
            title: title
        };

        return item;
    }
});
