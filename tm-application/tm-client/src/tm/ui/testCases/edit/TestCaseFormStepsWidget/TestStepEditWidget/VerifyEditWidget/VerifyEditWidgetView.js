/*global define*/
define([
    'jscore/core',
    'text!./VerifyEditWidget.html',
    'styles!./VerifyEditWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.holder = element.find('.eaTM-VerifyEdit-holder');
            this.orderNr = element.find('.eaTM-VerifyEdit-orderNr');
            this.title = element.find('.eaTM-VerifyEdit-title');
            this.iconsBlock = element.find('.eaTM-VerifyEdit-iconsBlock');
            this.draggableArea = element.find('.eaTM-VerifyEdit-draggableArea');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getHolder: function () {
            return this.holder;
        },

        getTitle: function () {
            return this.title;
        },

        getOrderNr: function () {
            return this.orderNr;
        },

        getIconsBlock: function () {
            return this.iconsBlock;
        },

        getDraggableArea: function () {
            return this.draggableArea;
        }

    });

});
