define([
    'jscore/core',
    './TestCampaignCreateBarRegionView',
    '../../../../common/ActionBarRegion/ActionBarRegion',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/Constants',
    '../../../../common/ContextFilter',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionLink/ActionLink'
], function (core, View, ActionBarRegion, ActionIcon, Constants, ContextFilter, Navigation, ActionLink) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */
        View: View,

        onStart: function () {
            this.view.afterRender();

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            initCreateActionLink.call(this);
            initCancelActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', navigateToTestPlans);
        },

        onCreateTestPlanClick: function (event) {
            event.preventDefault();
            this.getContext().eventBus.publish(Constants.events.CREATE_TEST_PLAN);
        }

    });

    function initCancelActionLink () {
        this.cancelActionLink = new ActionLink({
            icon: {iconKey: 'undo', interactive: true, title: 'Cancel'},
            link: {text: 'Cancel'},
            action: navigateToTestPlans
        });
        this.cancelActionLink.attachTo(this.view.getCancelLinkHolder());
    }

    function initCreateActionLink () {
        this.createActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Create'},
            link: {text: 'Create'},
            action: this.onCreateTestPlanClick.bind(this)
        });
        this.createActionLink.attachTo(this.view.getCreateLinkHolder());
    }

    function navigateToTestPlans () {
        Navigation.navigateTo(Navigation.getTestPlansListUrlWithParams(ContextFilter.projectIdParam));
    }

});
