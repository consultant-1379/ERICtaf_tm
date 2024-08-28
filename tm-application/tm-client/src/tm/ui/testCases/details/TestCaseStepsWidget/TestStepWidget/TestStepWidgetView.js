/*global define*/
define([
    'jscore/core',
    'text!./TestStepWidget.html',
    'styles!./TestStepWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.orderNr = element.find('.eaTM-TestStep-orderNr');
            this.title = element.find('.eaTM-TestStep-title');
            this.verifiesList = element.find('.eaTM-TestStep-verifiesList');
            this.dataField = element.find('.eaTM-TestStep-dataField');
            this.dataFieldHolder = element.find('.eaTM-TestStep-dataFieldHolder');
            this.result = element.find('.eaTM-TestStep-result');
            this.titleCompare = element.find('.eaTM-TestStep-titleCompare');
            this.dataCompare = element.find('.eaTM-TestStep-dataFieldCompare');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getOrderNr: function () {
            return this.orderNr;
        },

        getTitle: function () {
            return this.title;
        },

        getVerifiesList: function () {
            return this.verifiesList;
        },

        getDataField: function () {
            return this.dataField;
        },

        getDataFieldHolder: function () {
            return this.dataFieldHolder;
        },

        getResult: function () {
            return this.result;
        },

        getTitleCompare: function () {
            return this.titleCompare;
        },

        getDataFieldCompare: function () {
            return this.dataCompare;
        }

    });

});
