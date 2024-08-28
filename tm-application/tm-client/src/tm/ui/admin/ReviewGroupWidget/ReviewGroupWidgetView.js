define([
    'jscore/core',
    'template!./ReviewGroupWidget.html',
    'styles!./ReviewGroupWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.groupTableholder = element.find('.eaTM-ReviewGroupWidget-groupTableHolder');
            this.userTableholder = element.find('.eaTM-ReviewGroupWidget-userTableHolder');
            this.name = element.find('.eaTM-ReviewGroupWidget-input');
            this.addButton = element.find('.eaTM-ReviewGroupWidget-createButton');
            this.editButton = element.find('.eaTM-ReviewGroupWidget-editButton');
            this.inputlabel = element.find('.eaTM-ReviewGroupWidget-nameLabel');
            this.groupInput = element.find('.eaTM-ReviewGroupWidget-groupInput');
        },

        getTemplate: function () {
            return template(this.options);
        },

        getStyle: function () {
            return styles;
        },

        getCreateButton: function () {
            return this.addButton;
        },

        getEditButton: function () {
            return this.editButton;
        },

        getName: function () {
            return this.name;
        },

        getGroupTableHolder: function () {
            return this.groupTableholder;
        },

        getInputHolder: function () {
            return this.inputlabel;
        },

        getGroupInput: function () {
            return this.groupInput;
        },

        getUserTableHolder: function () {
            return this.userTableholder;
        }

    });

});
