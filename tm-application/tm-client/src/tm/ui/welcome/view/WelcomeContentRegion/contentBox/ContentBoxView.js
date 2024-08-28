define([
    'jscore/core',
    'text!./ContentBox.html',
    'styles!./ContentBox.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.contentBox = element.find('.eaTM-ContentBox');
            this.title = element.find('.eaTM-ContentBox-title');
            this.list = element.find('.eaTM-ContentBox-list');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getContentBox: function () {
            return this.contentBox;
        },

        getTitle: function () {
            return this.title;
        },

        getList: function () {
            return this.list;
        }
    });

});
