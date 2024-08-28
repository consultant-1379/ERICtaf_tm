define([
    '../../../../common/ActionBarRegion/ActionBarRegion',
    './CreateTestExecutionBarRegionView',
    '../../../../common/Constants',
    '../../../../common/Animation',
    '../../../../common/Navigation',
    '../../../../common/widgets/actionIcon/ActionIcon',
    '../../../../common/widgets/actionLink/ActionLink',
    'widgets/SelectBox',
    '../../../../common/ReferenceHelper',
    '../../../../common/ContextFilter'
], function (ActionBarRegion, View, Constants, Animation, Navigation, ActionIcon, ActionLink, SelectBox,
             ReferenceHelper, ContextFilter) {
    'use strict';

    return ActionBarRegion.extend({
        /*jshint validthis:true */

        View: View,

        onStart: function () {
            this.eventBus = this.getContext().eventBus;
            this.references = this.options.references;

            var actionIcon = new ActionIcon({
                iconKey: 'leftArrowLarge'
            });
            actionIcon.attachTo(this.view.getBackIcon());

            initCancelActionLink.call(this);
            initSaveActionLink.call(this);

            this.view.getBackButton().addEventHandler('click', function () {
                Navigation.navigateTo(Navigation.getTestPlanExecutionUrl(this.getPrimaryIndex()));
            }.bind(this));

            this.referenceHelper = new ReferenceHelper({
                references: this.references
            });

            this.eventBus.subscribe(Constants.events.REFERENCES_RECEIVED, this.onReferencesReceived, this);
            this.eventBus.subscribe(Constants.events.PROJECT_CHANGED, this.onProjectChange, this);

            this.selectBox = new SelectBox();
            this.selectBox.setItems(this.referenceHelper.getReferenceItems('project'));
            this.selectBox.view.getButton().setAttribute('id', 'TMS_CreateTestExecution_CreateTestExecutionBar-projectSelect');
            this.selectBox.addEventHandler('change', function () {
                this.eventBus.publish(Constants.events.UPDATE_USER_PROFILE_WITH_PROJECT,
                    this.selectBox.getValue());

                this.eventBus.publish(Constants.events.PROJECT_CHANGED);
            }.bind(this));
            this.onProjectChange();
            this.selectBox.attachTo(this.view.getProjectHolder());

        },

        onSaveClick: function (event) {
            event.preventDefault();
            this.eventBus.publish(Constants.events.SAVE_TEST_EXECUTION_RESULT);
        },

        onReferencesReceived: function () {
            this.selectBox.setItems(this.referenceHelper.getReferenceItems('project'));
        },

        onProjectChange: function () {
            if (ContextFilter.profileProject != null) {
                this.selectBox.setValue(ContextFilter.profileProject);
            }
        }

    });

    function initCancelActionLink () {
        this.cancelActionLink = new ActionLink({
            icon: {iconKey: 'undo', interactive: true, title: 'Cancel'},
            link: {text: 'Cancel'},
            action: Navigation.goToPreviousPage
        });
        this.cancelActionLink.attachTo(this.view.getCancelLinkHolder());
    }

    function initSaveActionLink () {
        this.saveActionLink = new ActionLink({
            icon: {iconKey: 'save', interactive: true, title: 'Save'},
            link: {text: 'Save'},
            action: this.onSaveClick.bind(this)
        });
        this.saveActionLink.attachTo(this.view.getSaveLinkHolder());
    }

});
