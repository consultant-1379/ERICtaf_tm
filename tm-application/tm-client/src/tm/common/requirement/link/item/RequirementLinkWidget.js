define([
    'jscore/core',
    'widgets/Tooltip',
    '../../../Constants',
    './RequirementLinkWidgetView',
    '../../../../ext/widgets/IconPopup/IconPopup',
    '../../RequirementCommonHelper'
], function (core, Tooltip, Constants, View, IconPopup, RequirementCommonHelper) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function (options) {
            if (options) {
                this.externalId = options.externalId;
                this.title = options.title;
                this.statusName = options.statusName;
                this.label = options.label;
                this.deliveredIn = options.deliveredIn;
            }
        },

        onViewReady: function () {
            this.view.afterRender();

            this.iconPopup = new IconPopup({
                interactive: true,
                width: '300px'
            });
            this.iconPopup.attachTo(this.view.getIconHolder());

            this.prepareToView();
        },

        prepareToView: function () {
            this.iconPopup.setContent(this.composeTooltipString());
            this.iconPopup.setIcon(this.iconNameByStatus(this.statusName));
            this.iconPopup.setTitle(this.statusName ? this.statusName : 'N/A');

            this.view.getRequirementId().setText(this.externalId);
            this.view.getLabel().setText(this.label);
            if (this.deliveredIn) {
                this.view.getDeliveredIn().setText(this.deliveredIn ? this.deliveredIn : '');
                this.view.getDeliveredIn().removeModifier('hide');
            } else {
                this.view.getDeliveredIn().setModifier('hide');
            }

        },

        setExternalId: function (externalId) {
            this.externalId = externalId;
        },

        setTitle: function (title) {
            this.title = title;
        },

        setStatusName: function (status) {
            this.statusName = status;
        },

        setLabel: function (label) {
            this.label = label;
        },

        setDeliveredIn: function (deliveredIn) {
            this.deliveredIn = deliveredIn;
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

        clear: function () {
            this.setExternalId('');
            this.setTitle('');
            this.setStatusName('');
            this.setLabel('');
            this.setDeliveredIn('');
        }

    });
})
;
