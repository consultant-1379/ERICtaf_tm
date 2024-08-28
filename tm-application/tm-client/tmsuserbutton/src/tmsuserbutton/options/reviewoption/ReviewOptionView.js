define([
    'jscore/core',
    'text!./ReviewOption.html',
    'styles!./ReviewOption.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.reviewLink = element.find('.eaTmsUserButton-reviewOption-anchor');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getReviewLink: function () {
            return this.reviewLink;
        }

    });

});
