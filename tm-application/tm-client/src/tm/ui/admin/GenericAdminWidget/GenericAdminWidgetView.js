define([
    'jscore/core',
    'template!./GenericAdminWidget.html',
    'styles!./GenericAdminWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableholder = element.find('.eaTM-GenericAdminWidget-tableHolder');
            this.content = element.find('.eaTM-GenericAdminWidget-content');
            this.productHolder = element.find('.eaTM-GenericAdminWidget-productHolder');
            this.featureHolder = element.find('.eaTM-GenericAdminWidget-featureHolder');
            this.name = element.find('.eaTM-GenericAdminWidget-input');
            this.createButton = element.find('.eaTM-GenericAdminWidget-createButton');
            this.editButton = element.find('.eaTM-GenericAdminWidget-editButton');
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

        getFeatureHolder: function () {
            return this.featureHolder;
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
