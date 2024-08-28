/*global define*/
define([
    'jscore/core',
    './TestCampaignCopyBarRegionView',
    '../../../../common/ActionBarRegion/ActionBarRegion',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionLink/ActionLink'
], function (core, View, ActionBarRegion, ActionIcon, Constants, Animation, Navigation, ActionLink) {
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

            this.view.getBackButton().addEventHandler('click', Navigation.goToPreviousPage);
        },

        onCreateTestPlanClick: function (event) {
            event.preventDefault();
            this.getContext().eventBus.publish(Constants.events.COPY_TEST_PLAN);
        },

        onCancelCopyClick: function (event) {
            event.preventDefault();
            Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(this.getUrlItemId()));
        }

    });

    function initCreateActionLink () {
        this.createActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Create'},
            link: {text: 'Create'},
            action: this.onCreateTestPlanClick.bind(this)
        });
        this.createActionLink.attachTo(this.view.getCreateLinkHolder());
    }

    function initCancelActionLink () {
        this.cancelActionLink = new ActionLink({
            icon: {iconKey: 'undo', interactive: true, title: 'Cancel'},
            link: {text: 'Cancel'},
            action: this.onCancelCopyClick.bind(this)
        });
        this.cancelActionLink.attachTo(this.view.getCancelLinkHolder());
    }

});
