define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './TestCaseCreateBarRegionView',
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

            this.view.getBackButton().addEventHandler('click', navigateToTestCases);
        },

        onCreateClick: function (e) {
            e.preventDefault();
            this.eventBus.publish(Constants.events.TEST_CASE_CREATE_REQUEST);
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
            action: navigateToTestCases
        });
        this.cancelActionLink.attachTo(this.view.getCancelLinkHolder());
    }

    function navigateToTestCases () {
        Navigation.navigateTo(
            Navigation.getTestCaseListUrlWithParams(ContextFilter.isAdvancedSearch, ContextFilter.searchQuery)
        );
    }

});
