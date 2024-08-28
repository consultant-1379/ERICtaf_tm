define([
    'jscore/core',
    'template!./ProductWidget.html',
    'styles!./ProductWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.tableholder = element.find('.eaTM-ProductWidget-tableHolder');
            this.content = element.find('.eaTM-ProductWidget-content');
            this.name = element.find('.eaTM-ProductWidget-input');
            this.externalId = element.find('.eaTM-ProductWidget-externalInput');
            this.createButton = element.find('.eaTM-ProductWidget-createButton');
            this.editButton = element.find('.eaTM-ProductWidget-editButton');
            this.refreshButton = element.find('.eaTM-ProductWidget-refreshButton');
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
        },

        getRefreshButton: function () {
            return this.refreshButton;
        }

    });

});
