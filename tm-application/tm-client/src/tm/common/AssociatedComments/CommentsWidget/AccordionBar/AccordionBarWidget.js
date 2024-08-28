define([
    'jscore/core',
    './AccordionBarWidgetView',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/DateHelper'
], function (core, View, ActionIcon, DateHelper) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.post = options.post;
            this.eventBus = options.eventBus;
            this.currentUser = options.currentUser;
            this.parentWidget = options.parentWidget;

            this.userName = this.post.getUser().userName;
        },

        onViewReady: function () {
            this.view.afterRender();
            populatePostData.call(this, this.userName, this.post.getCreatedAt());
            initRemoveActionLink.call(this);
            setVisibleConfirmBlock.call(this, false);
        },

        onRemoveCommentClick: function () {
            setVisibleConfirmBlock.call(this, true);
        },

        onRemoveCommentConfirmClick: function () {
            deletePost.call(this);
            setVisibleConfirmBlock.call(this, false);
        },

        onRemoveCommentCancelClick: function () {
            setVisibleConfirmBlock.call(this, false);
        }

    });

    function initRemoveActionLink () {
        if (validateAllowDelete(this.currentUser, this.post.getUser()) === false) {
            return;
        }

        var removeIcon = new ActionIcon({
            iconKey: 'close',
            interactive: true
        });
        removeIcon.attachTo(this.view.getRemoveActionBlock());
        removeIcon.addEventHandler('click', this.onRemoveCommentClick, this);

        var confirmOkIcon = new ActionIcon({
            iconKey: 'delete',
            interactive: true
        });
        confirmOkIcon.attachTo(this.view.getConfirmActionBlock());
        confirmOkIcon.addEventHandler('click', this.onRemoveCommentConfirmClick, this);

        var confirmCancelIcon = new ActionIcon({
            iconKey: 'undo',
            interactive: true
        });
        confirmCancelIcon.attachTo(this.view.getConfirmActionBlock());
        confirmCancelIcon.addEventHandler('click', this.onRemoveCommentCancelClick, this);
    }

    function validateAllowDelete (currentUser, postUser) {
        if (!currentUser || !postUser) {
            return false;
        }
        return currentUser.getUserId() === postUser.userId;

    }

    function setVisibleConfirmBlock (isVisible) {
        var confirmActionBlock = this.view.getConfirmActionBlock(),
            removeActionBlock = this.view.getRemoveActionBlock();
        if (isVisible) {
            confirmActionBlock.setStyle('display', 'block');
            removeActionBlock.setStyle('display', 'none');
        } else {
            confirmActionBlock.setStyle('display', 'none');
            removeActionBlock.setStyle('display', 'block');
        }
    }

    function populatePostData (userName, createdAt) {
        this.view.getUserName().setText(userName);
        this.view.getCreatedAt().setText(DateHelper.formatStringToDatetime(createdAt));
    }

    function deletePost () {
        this.parentWidget.deletePost(this.post);
    }

});
