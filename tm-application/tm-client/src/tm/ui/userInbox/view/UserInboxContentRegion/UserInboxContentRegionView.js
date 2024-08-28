define([
    'jscore/core',
    'text!./UserInboxContentRegion.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();

            this.userInboxAssignmentsBlock = element.find('.eaTM-UserInbox-AssignmentsBlock');
        },

        getTemplate: function () {
            return template;
        },

        getUserInboxAssignmentsBlock: function () {
            return this.userInboxAssignmentsBlock;
        }

    });
});
