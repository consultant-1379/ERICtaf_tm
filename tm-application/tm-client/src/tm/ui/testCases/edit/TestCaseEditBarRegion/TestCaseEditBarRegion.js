define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseEditBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink'
], function (ActionBarRegion, View, Constants, Animation, Navigation, ActionIcon, ActionLink) {
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

            initSaveActionLink.call(this);
            initCancelActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', Navigation.goToPreviousPage);
        },

        onSaveClick: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_SAVE_REQUEST);
        }

    });

    function initSaveActionLink () {
        this.saveActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Save'},
            link: {text: 'Save'},
            action: this.onSaveClick.bind(this)
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
