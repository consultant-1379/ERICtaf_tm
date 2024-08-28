/*global define*/
define([
    'jscore/core',
    'text!./ContextSearchMenu.html',
    'styles!./ContextSearchMenu.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.productHolder = element.find('.eaTM-ContextSearchMenu-selectProductBox');
            this.featureHolder = element.find('.eaTM-ContextSearchMenu-selectFeatureBox');
            this.componentHolder = element.find('.eaTM-ContextSearchMenu-selectComponentBox');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getProductSelectBoxHolder: function () {
            return this.productHolder;
        },

        getFeatureSelectBoxHolder: function () {
            return this.featureHolder;
        },

        getComponentSelectBoxHolder: function () {
            return this.componentHolder;
        }
    });

});
