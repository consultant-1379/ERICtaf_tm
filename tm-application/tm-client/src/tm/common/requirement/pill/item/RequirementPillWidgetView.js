define([
    'jscore/core',
    'text!./RequirementPillWidget.html',
    'styles!./RequirementPillWidget.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.requirementId = this.getElement().find('.eaTM-RequirementPillLink-requirementId');
            this.iconHolder = this.getElement().find('.eaTM-RequirementPillIcon-iconHolder');

        },

        getLink: function () {
            return this.requirementId;
        },

        getIconHolder: function () {
            return this.iconHolder;
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        }

    });
});
