define([
    'jscore/core',
    'text!./AdminOption.html',
    'styles!./AdminOption.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.adminLink = element.find('.eaTmsUserButton-adminOption-anchor');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getAdminLink: function () {
            return this.adminLink;
        }
    });

});
