/*global define*/
define([
    'jscore/core',
    'text!./DetailsLineWidget.html',
    'styles!./DetailsLineWidget.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.title = element.find('.eaTM-DetailsLine-title');
            this.type = element.find('.eaTM-DetailsLine-type');
            this.actions = element.find('.eaTM-DetailsLine-actions');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTitle: function () {
            return this.title;
        },

        getType: function () {
            return this.type;
        },

        getActions: function () {
            return this.actions;
        }

    });

});
