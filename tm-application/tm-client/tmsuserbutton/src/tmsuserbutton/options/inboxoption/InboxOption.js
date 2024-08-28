define([
    'jscore/core',
    './InboxOptionView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this.inboxUrl = options.inboxUrl;
        },

        onViewReady: function () {
            this.view.getInboxLink().setText('Inbox');

            if (this.inboxUrl) {
                this.view.getInboxLink().setAttribute('href', this.inboxUrl);
            }
        }
    });
});
