define([
    'jscore/core',
    './ReviewInboxContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    './ReviewInboxAssignmentsWidget/ReviewInboxAssignmentsWidget',
    '../../../../common/ModelHelper',
    '../../../testCases/models/testCases/TestCasesCollection',
    '../../../../common/models/UserProfileModel'
], function (core, View, Constants, Animation, Navigation, ContextFilter, ReviewInboxAssignmentsWidget, ModelHelper,
             TestCasesCollection, UserProfileModel) {
    'use strict';

    return core.Region.extend({
        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.collection = new TestCasesCollection();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_REVIEW_INBOX, this.refreshCurrentPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_REVIEW_INBOX);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_REVIEW_INBOX,
                this.refreshCurrentPage.bind(this));

            this.reviewBlock = new ReviewInboxAssignmentsWidget({
                collection: this.collection,
                region: this
            });
            this.reviewBlock.attachTo(this.view.getReviewInboxBlock());
        },

        refreshCurrentPage: function () {
            this.reviewBlock.refreshTableInfo();
            getCurrentUser.call(this);
        }
    });

    function getCurrentUser () {
        /*jshint validthis:true */
        this.userProfile = new UserProfileModel();
        this.userProfile.fetch({
            reset: true,
            statusCode: ModelHelper.authenticationHandler(this.eventBus, {
                200: function (data) {
                    this.collection.resetPage();
                    this.collection.fetch({
                        reset: true,
                        data: {
                            q: 'status=Review|reviewGroup=' + data.userId + '|reviewUser=' + data.userId
                        },
                        statusCode: ModelHelper.statusCodeHandler(this.eventBus)
                    });
                }.bind(this)
            })
        });
    }
});
