define([
    'jscore/core',
    'template!./UserAccessWidget.html',
    'styles!./UserAccessWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableholder = element.find('.eaTM-UserAccessWidget-tableHolder');
            this.name = element.find('.eaTM-UserAccessWidget-input');
            this.addButton = element.find('.eaTM-UserAccessWidget-addButton');
            this.inputlabel = element.find('.eaTM-UserAccessWidget-label');
        },

        getTemplate: function () {
            return template(this.options);
        },

        getStyle: function () {
            return styles;
        },

        getAddButton: function () {
            return this.addButton;
        },

        getName: function () {
            return this.name;
        },

        getTableHolder: function () {
            return this.tableholder;
        },

        getInputHolder: function () {
            return this.inputlabel;
        }

    });

});
