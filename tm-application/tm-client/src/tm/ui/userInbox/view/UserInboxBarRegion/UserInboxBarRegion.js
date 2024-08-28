define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './UserInboxBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    '../../../../common/widgets/actionIcon/ActionIcon'
], function (ActionBarRegion, View, Constants, Animation, Navigation, ContextFilter, ActionIcon) {
    'use strict';

    return ActionBarRegion.extend({

        View: View,

        onStart: function () {
            this.eventBus = this.getContext().eventBus;

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            this.view.getBackButton().addEventHandler('click', navigateToTestCases);
        },

        show: function () {
            ActionBarRegion.prototype.show.call(this);
        }

    });

    function navigateToTestCases () {
        Navigation.navigateTo(
            Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch, ContextFilter.searchQuery)
        );
    }

});
