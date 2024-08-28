define([
    'jscore/ext/utils/base/underscore',
    'text!./TestCampaignLockMessage.html',
    'text!./TestCampaignUnlockMessage.html',
    'widgets/Dialog',
    '../../../../common/HolderWidget/HolderWidget'
], function (_, lockMessage, unlockMessage, Dialog, HolderWidget) {
    'use strict';

    var TYPES = {
        lock: {
            title: 'Lock',
            message: lockMessage
        },
        unlock: {
            title: 'Unlock',
            message: unlockMessage
        }
    };

    var TestPlanLockUnlockDialog = function (options) {
        var type = TYPES[options.type];
        this.dialog = new Dialog({
            header: type.title + ' Test Campaign',
            type: 'confirmation',
            content: 'Are you sure you want to ' + type.title.toLowerCase() + ' this Test Campaign?',
            optionalContent: new HolderWidget({
                content: type.message
            }),
            buttons: [
                {
                    caption: type.title,
                    color: 'blue',
                    action: function () {
                        options.action();
                        this.hide();
                    }.bind(this)
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

    TestPlanLockUnlockDialog.prototype.show = function () {
        this.dialog.show();
    };

    TestPlanLockUnlockDialog.prototype.hide = function () {
        this.dialog.hide();
    };

    return TestPlanLockUnlockDialog;

});
