define([
    'jscore/core',
    'text!./InboxOption.html',
    'styles!./InboxOption.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({
        /*jshint validthis:true*/

        afterRender: function () {
            var element = this.getElement();
            this.inboxLink = element.find('.eaTmsUserButton-inboxOption-anchor');
        },

        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return styles;
        },

        getInboxLink: function () {
            return this.inboxLink;
        }

    });

});
