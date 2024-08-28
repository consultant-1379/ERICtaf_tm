define([
    'jscore/core',
    'jscore/ext/net',
    './LogoutOptionView',
    'widgets/Dialog'
], function (core, net, View, Dialog) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        init: function (options) {
            this.logoutUrl = options.logoutUrl;
            this.restLogoutUrl = options.restLogoutUrl;
        },

        onViewReady: function (parent) {
            this.view.getLogoutLink().setText('Logout');
            this.configureLogout();
        },

        configureLogout: function () {
            // Get the translation for the logout button
            this.setDefaultLocale();

            // Set the URL based on the property defined in container config
            if (this.logoutUrl) {
                this.view.getLogoutLink().setAttribute('href', this.logoutUrl);
            }

            // Show dialog on click. This still allows middle clicking to log out.
            this.view.getLogoutLink().addEventHandler('click', function(e) {
                e.preventDefault();
                this.dialog.show();
            }.bind(this));
        },

        setDefaultLocale: function() {
            this.view.getLogoutLink().setText('Logout');
            this.createDialog({
                title: 'Logout',
                message: 'Are you sure you want to log out?',
                ok: 'OK',
                cancel: 'Cancel'
            });
        },

        createDialog: function(dictionary) {
            this.dialog = new Dialog({
                header: dictionary.title,
                content: dictionary.message,
                buttons: [
                    {
                        caption: dictionary.ok,
                        color: 'blue',
                        action: function () {
                            continueLogout.call(this);
                        }.bind(this)
                    },
                    {
                        caption: dictionary.cancel,
                        action: function () {
                            this.dialog.hide();
                        }.bind(this)
                    }
                ]
            });
        }
    });

    function continueLogout() {
        if (this.restLogoutUrl) {
            net.ajax({
                url: this.restLogoutUrl,
                type: 'DELETE',
                async: false,
                success: function () {
                    closeLogoutAndRedirect.call(this);
                }.bind(this),
                error: function () {
                    closeLogoutAndRedirect.call(this);
                }
            });
        } else {
            closeLogoutAndRedirect.call(this);
        }
    }

    function closeLogoutAndRedirect () {
        this.dialog.hide();

        // redirect to login page
        window.location.href = this.view.getLogoutLink().getAttribute('href');
    }
})