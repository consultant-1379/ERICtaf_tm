define([
    'jscore/core',
    'widgets/Tooltip',
    '../../../Constants',
    '../../../ContextFilter',
    './RequirementPillWidgetView',
    '../../../../ext/widgets/IconPopup/IconPopup',
    '../../RequirementCommonHelper'
], function (core, Tooltip, Constants, ContextFilter, View, IconPopup, RequirementCommonHelper) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this._isPopupOpened = false;
            if (options) {
                this.externalId = options.externalId;
                this.title = options.title;
                this.statusName = options.statusName;
                this.deliveredIn = options.deliveredIn;
            }
        },

        onViewReady: function () {
            this.view.afterRender();
            this.iconPopup = new IconPopup({
                icon: 'small',
                interactive: true,
                width: '300px'
            });
            this.iconPopup.attachTo(this.view.getIconHolder());

            this.iconPopup.view.getInfoIcon().addEventHandler('click', this.toggleOnIconClick, this);

            this.view.getLink().addEventHandler('click', this.toggleOnLinkClick, this);
            this.prepareToView();
        },

        toggleOnIconClick: function (event) {
            this._isPopupOpened = !this.iconPopup.getVisible();
            checkToCloseInfoPopup.call(this);

            event.preventDefault();
            event.stopPropagation();
        },

        toggleOnLinkClick: function (event) {
            this._isPopupOpened = !this.iconPopup.getVisible();
            this.iconPopup.setVisible(this._isPopupOpened);
            checkToCloseInfoPopup.call(this);

            event.preventDefault();
            event.stopPropagation();
        },

        prepareToView: function () {
            this.iconPopup.setContent(this.composeTooltipString());
            this.iconPopup.setIcon(this.iconNameByStatus(this.statusName));
            this.iconPopup.setTitle(this.statusName ? this.statusName : 'N/A');

            this.view.getLink().setText(this.externalId);
        },

        composeTooltipString: function () {
            return RequirementCommonHelper.composeTooltipString(
                this.statusName,
                this.title,
                this.externalId,
                this.jiraUrl(),
                this.deliveredIn);
        },

        jiraUrl: function () {
            return Constants.urls.JIRA_BROWSE_LINK + this.externalId;
        },

        iconNameByStatus: function (status) {
            return RequirementCommonHelper.iconNameByStatus(status);
        },

        closePopup: function () {
            this.iconPopup.setVisible(false);
        }

    });

    function checkToCloseInfoPopup () {
        if (this === ContextFilter.openedInfoPopup /*&& this.iconPopup.bodyEventId*/) {
            return;
        }

        var isVisible = this.iconPopup.getVisible();
        if (!isVisible) {
            ContextFilter.openedInfoPopup = null;
            return;
        }

        if (ContextFilter.openedInfoPopup != null) {
            ContextFilter.openedInfoPopup.closePopup();
        }

        ContextFilter.openedInfoPopup = this;
    }

});
