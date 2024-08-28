define([
    'jscore/core',
    'template!./GenericProductWidget.html',
    'styles!./GenericProductWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableholder = element.find('.eaTM-GenericProductWidget-tableHolder');
            this.content = element.find('.eaTM-GenericProductWidget-content');
            this.productHolder = element.find('.eaTM-GenericProductWidget-productHolder');
            this.name = element.find('.eaTM-GenericProductWidget-input');
            this.createButton = element.find('.eaTM-GenericProductWidget-createButton');
            this.editButton = element.find('.eaTM-GenericProductWidget-editButton');
        },

        getTemplate: function () {
            return template(this.options);
        },

        getStyle: function () {
            return styles;
        },

        getContent: function () {
            return this.content;
        },

        getTableHolder: function () {
            return this.tableholder;
        },

        getProductHolder: function () {
            return this.productHolder;
        },

        getCreateButton: function () {
            return this.createButton;
        },

        getEditButton: function () {
            return this.editButton;
        },

        getName: function () {
            return this.name;
        }

    });

});
