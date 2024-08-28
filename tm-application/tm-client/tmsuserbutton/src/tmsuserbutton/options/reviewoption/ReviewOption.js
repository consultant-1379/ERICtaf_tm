define([
    'jscore/core',
    './ReviewOptionView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this.reviewUrl = options.reviewUrl;
        },

        onViewReady: function () {
            this.view.getReviewLink().setText('My Reviews');

            if (this.reviewUrl) {
                this.view.getReviewLink().setAttribute('href', this.reviewUrl);
            }
        }
    });
});
