define([
    'jscore/ext/utils/base/underscore',
    'widgets/Dialog'
], function (_, Dialog) {
    'use strict';

    var DeleteDialog = function (options) {
        this.options = options;
        this.dialog = new Dialog({
            header: 'Remove ' + options.item,
            type: 'warning',
            content: 'Are you sure you want to delete this ' + options.item.toLowerCase() + '?',
            optionalContent: options.optionalContent,
            buttons: [
                {
                    caption: 'Remove',
                    color: 'red',
                    action: options.action
                },
                {
                    caption: 'Cancel',
                    action: function () {
                        this.hide();
                    }.bind(this)
                }
            ]
        });
    };

    DeleteDialog.prototype.show = function () {
        this.dialog.show();
    };

    DeleteDialog.prototype.hide = function () {
        this.dialog.hide();
    };

    return DeleteDialog;

});
