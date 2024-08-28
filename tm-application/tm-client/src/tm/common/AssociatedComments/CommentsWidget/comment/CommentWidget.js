define([
    'jscore/core',
    'jscore/ext/binding',
    './CommentWidgetView'
], function (core, binding, View) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function (options) {
            this.post = options.post;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.populatePost(this.post.getMessage());
        },

        populatePost: function (message) {
            if (message) {
                this.view.getMessageText().setText(message);
            }
        }

    });

});
