define([
    'jscore/core',
    'template!./GenericProductWidgetExternalId.html',
    'styles!./GenericProductWidgetExternalId.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableholder = element.find('.eaTM-GenericProductWidgetExternalId-tableHolder');
            this.content = element.find('.eaTM-GenericProductWidgetExternalId-content');
            this.productHolder = element.find('.eaTM-GenericProductWidgetExternalId-productHolder');
            this.name = element.find('.eaTM-GenericProductWidgetExternalId-input');
            this.externalId = element.find('.eaTM-GenericProductWidgetExternalId-externalInput');
            this.createButton = element.find('.eaTM-GenericProductWidgetExternalId-createButton');
            this.editButton = element.find('.eaTM-GenericProductWidgetExternalId-editButton');
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
        },

        getExternalId: function () {
            return this.externalId;
        }

    });

});
