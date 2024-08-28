define([
    'jscore/core',
    'text!./AccordionBarWidget.html',
    'styles!./AccordionBarWidget.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.userName = element.find('.eaTM-AccordionTitleHolder-userName');
            this.createdAt = element.find('.eaTM-AccordionTitleHolder-createdAt');
            this.removeAction = element.find('.eaTM-AccordionTitleHolder-removeAction');
            this.confirmAction = element.find('.eaTM-AccordionTitleHolder-confirmAction');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        },

        getUserName: function () {
            return this.userName;
        },

        getCreatedAt: function () {
            return this.createdAt;
        },

        getRemoveActionBlock: function () {
            return this.removeAction;
        },

        getConfirmActionBlock: function () {
            return this.confirmAction;
        }

    });
});
