define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseCopyBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/ContextFilter',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink'
], function (ActionBarRegion, View, Constants, Animation, Navigation, ContextFilter, ActionIcon, ActionLink) {
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

            initCreateActionLink.call(this);
            initCancelActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', navigateToTestCaseDetails, this);
        },

        onCreateClick: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_COPY_REQUEST);
        },

        onCancelCopyClick: function (event) {
            event.preventDefault();
            Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(this.getUrlItemId()));
        }

    });

    function initCreateActionLink () {
        this.createActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Create'},
            link: {text: 'Create'},
            action: this.onCreateClick.bind(this)
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

    function navigateToTestCaseDetails () {
        Navigation.navigateTo(Navigation.getTestCaseDetailsUrl(this.getUrlItemId()));
    }

});
