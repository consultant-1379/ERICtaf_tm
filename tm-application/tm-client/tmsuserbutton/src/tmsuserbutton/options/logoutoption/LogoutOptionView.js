define([
    'jscore/core',
    'text!./LogoutOption.html',
    'styles!./LogoutOption.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.logoutLink = element.find('.eaTmsUserButton-logout-anchor');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getLogoutLink: function () {
            return this.logoutLink;
        }

    });

});