/*global define*/
define([
    'jscore/core',
    'text!./TestCaseDetailsBarRegion.html',
    'styles!./TestCaseDetailsBarRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.backButton = element.find('.ebQuickActionBar-back');
            this.backIcon = element.find('.ebQuickActionBar-backIcon');
            this.createLinkHolder = element.find('.eaTM-ViewTestCaseBar-createNewTestCaseLinkHolder');
            this.versionsHolder = element.find('.eaTM-ViewTestCaseBar-versionsHolder');
            this.editLinkHolder = element.find('.eaTM-ViewTestCaseBar-editLinkHolder');
            this.removeLinkHolder = element.find('.eaTM-ViewTestCaseBar-removeLinkHolder');
            this.showTestPlansHolder = element.find('.eaTM-ViewTestCaseBar-showTestPlansHolder');
            this.showCommentsHolder = element.find('.eaTM-ViewTestCaseBar-showCommentsHolder');
            this.copyLinkHolder = element.find('.eaTM-ViewTestCaseBar-copyLinkHolder');
            this.addToTestCampaignLinkHolder = element.find('.eaTM-ViewTestCaseBar-addToTestCampaignLinkHolder');
            this.sendForReviewHolder = element.find('.eaTM-ViewTestCaseBar-sendForReviewHolder');
            this.changeReviewerHolder = element.find('.eaTM-ViewTestCaseBar-changeReviewerHolder');
            this.approvalHolder = element.find('.eaTM-ViewTestCaseBar-approvalHolder');
            this.subscribeHolder = element.find('.eaTM-ViewTestCaseBar-subscribeLinkHolder');
            this.unsubscribeHolder = element.find('.eaTM-ViewTestCaseBar-unsubscribeLinkHolder');
            this.copyURLHolder = element.find('.eaTM-ViewTestCaseBar-copyURLLinkHolder');
            this.cancelReviewHolder = element.find('.eaTM-ViewTestCaseBar-cancelReviewHolder');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getBackButton: function () {
            return this.backButton;
        },

        getBackIcon: function () {
            return this.backIcon;
        },

        getCreateTestCaseLinkHolder: function () {
            return this.createLinkHolder;
        },

        getVersionsHolder: function () {
            return this.versionsHolder;
        },

        getEditLinkHolder: function () {
            return this.editLinkHolder;
        },

        getRemoveLinkHolder: function () {
            return this.removeLinkHolder;
        },

        getShowTestPlansLinkHolder: function () {
            return this.showTestPlansHolder;
        },

        getShowCommentsLinkHolder: function () {
            return this.showCommentsHolder;
        },

        getCopyLinkHolder: function () {
            return this.copyLinkHolder;
        },

        hideEditButton: function () {
            this.getEditLinkHolder().setStyle('display', 'none');
        },

        showEditButton: function () {
            this.getEditLinkHolder().removeStyle('display');
        },

        getAddToTestCampaignLinkHolder: function () {
            return this.addToTestCampaignLinkHolder;
        },

        getSendForReviewHolder: function () {
            return this.sendForReviewHolder;
        },

        getChangeReviewerHolder: function () {
            return this.changeReviewerHolder;
        },

        getApprovalHolder: function () {
            return this.approvalHolder;
        },

        getCancelReviewHolder: function () {
            return this.cancelReviewHolder;
        },

        getSubscribeHolder: function () {
            return this.subscribeHolder;
        },

        getUnsubscribeHolder: function () {
            return this.unsubscribeHolder;
        },

        getCopyURLLinkHolder: function () {
            return this.copyURLHolder;
        },

        showSubscribeButton: function () {
            this.getUnsubscribeHolder().setModifier('hidden', undefined, 'eaTM-ViewTestCaseBar-unsubscribeLinkHolder');
            this.getSubscribeHolder().removeModifier('hidden', 'eaTM-ViewTestCaseBar-subscribeLinkHolder');
        },

        showUnsubscribeButton: function () {
            this.getSubscribeHolder().setModifier('hidden', undefined, 'eaTM-ViewTestCaseBar-subscribeLinkHolder');
            this.getUnsubscribeHolder().removeModifier('hidden', 'eaTM-ViewTestCaseBar-unsubscribeLinkHolder');
        },

        showReviewButton: function () {
            this.getSendForReviewHolder().removeStyle('display');
        },

        hideReviewButton: function () {
            this.getSendForReviewHolder().setStyle('display', 'none');
        },

        showChangeReviewerButton: function () {
            this.getChangeReviewerHolder().removeStyle('display');
        },

        hideChangeReviewerButton: function () {
            this.getChangeReviewerHolder().setStyle('display', 'none');
        },

        hideApproveButton: function () {
            this.getApprovalHolder().setStyle('display', 'none');
        },

        showApproveButton: function () {
            this.getApprovalHolder().removeStyle('display');
        },

        hideCancelReviewButton: function () {
            this.getCancelReviewHolder().setStyle('display', 'none');
        },

        showCancelReviewButton: function () {
            this.getCancelReviewHolder().removeStyle('display');
        }

    });

});
