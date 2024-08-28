define([
    'jscore/core',
    'text!./WelcomeContentRegion.html',
    'styles!./WelcomeContentRegion.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.title = element.find('.eaTM-title');
            this.text = element.find('.eaTM-text');
            this.content = element.find('.eaTM-content');
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

        getText: function () {
            return this.text;
        },

        getContent: function () {
            return this.content;
        }

    });

});
