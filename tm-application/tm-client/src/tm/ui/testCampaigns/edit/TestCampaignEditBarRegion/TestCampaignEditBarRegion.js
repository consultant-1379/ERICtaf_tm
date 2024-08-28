/*global define*/
define([
    'jscore/core',
    './TestCampaignEditBarRegionView',
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

            initSaveActionLink.call(this);
            initCancelActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(this.getUrlItemId()));
            }.bind(this));
        },

        onSaveTestPlanClick: function (event) {
            event.preventDefault();
            this.getContext().eventBus.publish(Constants.events.SAVE_TEST_PLAN);
        }

    });

    function initSaveActionLink () {
        this.saveActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Save'},
            link: {text: 'Save'},
            action: this.onSaveTestPlanClick.bind(this)
        });
        this.saveActionLink.attachTo(this.view.getSaveLinkHolder());
    }

    function initCancelActionLink () {
        this.cancelActionLink = new ActionLink({
            icon: {iconKey: 'undo', interactive: true, title: 'Cancel'},
            link: {text: 'Cancel'},
            action: Navigation.goToPreviousPage
        });
        this.cancelActionLink.attachTo(this.view.getCancelLinkHolder());
    }

});
