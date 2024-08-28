/*global define*/
define([
    'jscore/core',
    'text!./TestStepEditWidget.html',
    'styles!./TestStepEditWidget.less',
    'widgets/utils/domUtils'
], function (core, template, styles, domUtils) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.orderNr = element.find('.eaTM-TestStepEdit-orderNr');
            this.titleHolder = element.find('.eaTM-TestStepEdit-titleHolder');
            this.title = element.find('.eaTM-TestStepEdit-title');
            this.iconsBlock = element.find('.eaTM-TestStepEdit-iconsBlock');
            this.verifiesList = element.find('.eaTM-TestStepEdit-verifiesList');
            this.addLink = element.find('.eaTM-TestStepEdit-addLink');
            this.draggableArea = element.find('.eaTM-TestStepEdit-draggableArea');
            this.dataField = element.find('.eaTM-TestStepEdit-dataField');
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

        getTitleHolder: function () {
            return this.titleHolder;
        },

        getTitle: function () {
            return this.title;
        },

        getIconsBlock: function () {
            return this.iconsBlock;
        },

        getVerifiesList: function () {
            return this.verifiesList;
        },

        getAddLink: function () {
            return this.addLink;
        },

        getDraggableArea: function () {
            return this.draggableArea;
        },

        getDataField: function () {
            return this.dataField;
        },

        getTitleValue: function () {
            return this.title.getValue();
        },

        getDataFieldValue: function () {
            return this.dataField.getValue();
        },

        getVerifiesData: function () {
            var verifiesElements = domUtils.findAll('.eaTM-VerifyEdit-title', this.getElement());
            var verifiesData = [];
            verifiesElements.forEach(function (el) {
                verifiesData.push({
                    name: el.getValue()
                });
            });
            return verifiesData;
        },

        markAsCopy: function () {
            this.getElement().setModifier('highlight');
        },

        removeHighlight: function () {
            this.getElement().removeModifier('highlight');
        }

    });

});
