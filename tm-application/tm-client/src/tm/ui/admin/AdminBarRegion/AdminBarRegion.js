define([
    '../../../common/ActionBarRegion/ActionBarRegion',
    './AdminBarRegionView',
    '../../../common/Navigation',
    '../../../common/widgets/actionIcon/ActionIcon'
], function (ActionBarRegion, View, Navigation, ActionIcon) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.view.afterRender();

            this.eventBus = this.getContext().eventBus;

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.goToPreviousPage();
            }, this);
        }
    });
});
