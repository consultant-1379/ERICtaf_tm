/*global define*/
define([
    'jscore/core',
    'text!./_progressBlock.html',
    'styles!./_progressBlock.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.messagesBlock = element.find('.eaTM-ProgressBlock-messages');
            this.progressBlock = element.find('.eaTM-ProgressBlock-progress');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getMessagesBlock: function () {
            return this.messagesBlock;
        },

        getProgressBlock: function () {
            return this.progressBlock;
        }

    });

});
