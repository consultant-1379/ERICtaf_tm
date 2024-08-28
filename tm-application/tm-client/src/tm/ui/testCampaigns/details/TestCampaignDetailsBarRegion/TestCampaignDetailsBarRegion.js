define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCampaignDetailsBarRegionView',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink'
], function (ActionBarRegion, View, Constants, ContextFilter, Navigation, ActionIcon, ActionLink) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.eventBus = this.getContext().eventBus;
            this.eventBus.subscribe(Constants.events.TEST_PLAN_LOCKED, this.changeTestPlanLockedState, this);

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            initLockActionLink.call(this);
            initEditActionLink.call(this);
            initRemoveActionLink.call(this);
            initExecutionActionLink.call(this);
            initCopyActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', navigateToTestPlans);
        },

        onRemoveTestPlanClick: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.DELETE_TEST_PLAN);
        },

        onEditTestPlanClick: function (e) {
            e.preventDefault();
            this.eventBus.publish('refreshTestCasesTable');
            Navigation.navigateTo(Navigation.getTestPlanEditUrl(this.getUrlItemId()));
        },

        onTestPlanExecutionClick: function (e) {
            e.preventDefault();
            Navigation.navigateTo(Navigation.getTestPlanExecutionUrl(this.getUrlItemId()));
        },

        onTestPlanLockLinkClick: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.LOCK_TEST_PLAN);
        },

        onTestPlanCopyClick: function (e) {
            e.preventDefault();
            Navigation.navigateTo(Navigation.getTestPlanCopyUrl(this.getUrlItemId()));
        },

        changeTestPlanLockedState: function (isLocked) {
            if (isLocked) {
                this.lockActionLink.getActionIcon().setIcon('unlock');
                this.lockActionLink.setLinkText('Unlock');
                this.view.getEditLinkHolder().setStyle('display', 'none');
            } else {
                this.lockActionLink.getActionIcon().setIcon('lock');
                this.lockActionLink.setLinkText('Lock');
                this.view.getEditLinkHolder().removeStyle('display');
            }
        }
    });

    function navigateToTestPlans () {
        Navigation.navigateTo(Navigation.getTestPlansListUrlWithParams(ContextFilter.projectIdParam));
    }

    function initLockActionLink () {
        this.lockActionLink = new ActionLink({
            icon: {iconKey: 'lock', interactive: true, title: 'Lock'},
            link: {text: 'Lock'},
            action: this.onTestPlanLockLinkClick.bind(this)
        });
        this.lockActionLink.attachTo(this.view.getStatusChangeBlock());
    }

    function initEditActionLink () {
        this.editActionLink = new ActionLink({
            icon: {iconKey: 'edit', interactive: true, title: 'Edit'},
            link: {text: 'Edit'},
            action: this.onEditTestPlanClick.bind(this)
        });
        this.editActionLink.attachTo(this.view.getEditLinkHolder());
    }

    function initRemoveActionLink () {
        this.removeActionLink = new ActionLink({
            icon: {iconKey: 'delete', interactive: true, title: 'Remove'},
            link: {text: 'Remove'},
            action: this.onRemoveTestPlanClick.bind(this)
        });
        this.removeActionLink.attachTo(this.view.getRemoveLinkHolder());
    }

    function initExecutionActionLink () {
        this.executionActionLink = new ActionLink({
            icon: {iconKey: 'menu', interactive: true, title: 'Test Campaign Execution'},
            link: {text: 'Test Campaign Execution'},
            action: this.onTestPlanExecutionClick.bind(this)
        });
        this.executionActionLink.attachTo(this.view.getTestPlanExecutionLinkHolder());
    }

    function initCopyActionLink () {
        this.copyActionLink = new ActionLink({
            icon: {iconKey: 'copy', interactive: true, title: 'Copy Test Campaign'},
            link: {text: 'Copy'},
            action: this.onTestPlanCopyClick.bind(this)
        });
        this.copyActionLink.attachTo(this.view.getCopyLinkHolder());
    }
});
