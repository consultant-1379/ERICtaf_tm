define([
    'jscore/ext/utils/base/underscore',
    'widgets/Dialog'
], function (_, Dialog) {
    'use strict';

    var DialogFactory = {};

    DialogFactory.info = function (opts) {
        var dialog = new Dialog(_.extend({
            type: 'information',
            buttons: [{
                caption: 'OK',
                color: 'blue',
                action: function () {
                    dialog.hide();
                }
            }]
        }, opts));
        return dialog;
    };

    return DialogFactory;
});
