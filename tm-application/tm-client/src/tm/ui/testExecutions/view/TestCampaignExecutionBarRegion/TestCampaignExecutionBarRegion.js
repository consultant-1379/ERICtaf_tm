define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCampaignExecutionBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionIcon/ActionIcon'
], function (ActionBarRegion, View, Constants, Animation, Navigation, ActionIcon) {
    'use strict';

    return ActionBarRegion.extend({

        View: View,

        onStart: function () {
            this.eventBus = this.getContext().eventBus;

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getTestPlanDetailsUrl(this.getUrlItemId()));
            }.bind(this));
        }

    });

});
