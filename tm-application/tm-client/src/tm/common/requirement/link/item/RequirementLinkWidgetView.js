define([
    'jscore/core',
    'text!./RequirementLinkWidget.html',
    'styles!./RequirementLinkWidget.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.requirementId = this.getElement().find('.eaTM-RequirementLinkWidget-requirementId');
            this.title = this.getElement().find('.eaTM-RequirementLinkWidget-label');
            this.iconHolder = this.getElement().find('.eaTM-RequirementLinkWidget-iconHolder');
            this.deliveredIn = this.getElement().find('.eaTM-RequirementLinkWidget-deliveredIn');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        getRequirementId: function () {
            return this.requirementId;
        },

        getLabel: function () {
            return this.title;
        },

        getIconHolder: function () {
            return this.iconHolder;
        },
        getDeliveredIn: function () {
            return this.deliveredIn;
        }

    });
});
