define([
    'jscore/core',
    './UserInboxContentRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    '../../models/AssignmentsCollection',
    './UserInboxAssignmentsWidget/UserInboxAssignmentsWidget',
    '../../../../common/ModelHelper'
], function (core, View, Constants, Animation, Navigation, ContextFilter,
             AssignmentsCollection, UserInboxAssignmentsWidget, ModelHelper) {
    'use strict';

    return core.Region.extend({
        View: View,

        init: function () {
            this.eventBus = this.getContext().eventBus;
            this.assignmentsCollection = new AssignmentsCollection();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.animation = new Animation(this.getElement(), this.eventBus);
            this.animation.showOn(Constants.events.SHOW_USER_INBOX, this.refreshCurrentPage.bind(this));
            this.animation.hideOn(Constants.events.HIDE_USER_INBOX);
            this.animation.markCurrentOn(Constants.events.MARK_CURRENT_USER_INBOX, this.refreshCurrentPage.bind(this));

            this.assignmentsBlock = new UserInboxAssignmentsWidget({
                assignmentsCollection: this.assignmentsCollection,
                region: this
            });
            this.assignmentsBlock.attachTo(this.view.getUserInboxAssignmentsBlock());
        },

        refreshCurrentPage: function () {
            this.assignmentsBlock.refreshTableInfo();

            this.assignmentsCollection.resetPage();
            this.assignmentsCollection.fetch({
                reset: true,
                statusCode: ModelHelper.statusCodeHandler(this.eventBus)
            });
        }
    });
});
