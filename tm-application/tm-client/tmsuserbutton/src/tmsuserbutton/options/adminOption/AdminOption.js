define([
    'jscore/core',
    './AdminOptionView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this.adminUrl = options.adminUrl;
        },

        onViewReady: function () {
            this.view.getAdminLink().setText('Admin');

            if (this.adminUrl) {
                this.view.getAdminLink().setAttribute('href', this.adminUrl);
            }
        }
    });
});
