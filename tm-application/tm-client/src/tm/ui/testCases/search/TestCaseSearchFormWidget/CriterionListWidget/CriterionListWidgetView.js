/*global define*/
define([
    'jscore/core',
    'text!./CriterionListWidget.html',
    'styles!./CriterionListWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.listHolder = element.find('.eaTM-Criterions-list');
            this.addBlock = element.find('.eaTM-Criterions-addBlock');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getRoot: function () {
            return this.getElement();
        },

        getListHolder: function () {
            return this.listHolder;
        },

        getAddBlock: function () {
            return this.addBlock;
        }

    });

});
