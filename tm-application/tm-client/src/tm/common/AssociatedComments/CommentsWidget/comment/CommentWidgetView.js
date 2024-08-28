define([
    'jscore/core',
    'text!./CommentWidget.html',
    'styles!./CommentWidget.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.messageText = this.getElement().find('.eaTM-AssociatedCommentMessage-text');
        },

        getMessageText: function () {
            return this.messageText;
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        }

    });
});
