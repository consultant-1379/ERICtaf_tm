define([
    'jscore/core',
    'text!./ReviewInboxContentRegion.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            this.reviewInboxBlock = element.find('.eaTM-ReviewInbox-ReviewBlock');
        },

        getTemplate: function () {
            return template;
        },

        getReviewInboxBlock: function () {
            return this.reviewInboxBlock;
        }

    });
});
