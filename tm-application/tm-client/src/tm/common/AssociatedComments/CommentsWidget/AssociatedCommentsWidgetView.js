define([
    'jscore/core',
    'text!./AssociatedCommentsWidget.html',
    'styles!./AssociatedCommentsWidget.less'
], function (core, template, style) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.postsHolder = element.find('.eaTM-AssociatedComments-postsHolder');
            this.editAreaHolder = element.find('.eaTM-AssociatedCommentsNew-editHolder');
            this.errorMessage = element.find('.eaTM-AssociatedCommentsNew-error');
            this.editAreaText = element.find('.eaTM-AssociatedCommentsNew-text');
            this.editAreaButtonsHolder = element.find('.eaTM-AssociatedCommentsNew-editButtonsHolder');
        },

        getPostsHolder: function () {
            return this.postsHolder;
        },

        getEditAreaHolder: function () {
            return this.editAreaHolder;
        },

        getErrorMessage: function () {
            return this.errorMessage;
        },

        getEditAreaText: function () {
            return this.editAreaText;
        },

        getEditAreaButtonsHolder: function () {
            return this.editAreaButtonsHolder;
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        }

    });
});
