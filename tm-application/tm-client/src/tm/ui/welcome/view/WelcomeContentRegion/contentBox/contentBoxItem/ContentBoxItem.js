define([
    'jscore/core',
    './ContentBoxItemView',
    'jscore/ext/utils/base/underscore',
    '../../infotip/Infotip',
    'widgets/Dialog'
], function (core, View, _, Infotip, Dialog) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        onViewReady: function () {
            this.view.afterRender();
            var infotipText = this.view.getText();
            infotipText.setText(this.options.name);
            infotipText.setAttribute('href', this.options.link || '#');
            infotipText.setAttribute('tooltip', this.options.tooltip);
            this.tooltipText = this.options.tooltip;
            this.dialogContent = this.options.dialogContent;

            this.infotip = new Infotip({tooltip: this.tooltipText});
            this.infotip.attachTo(this.view.getInfotip());

            infotipText.addEventHandler('mouseover', function () {
                var width = this.view.getText().getProperty('clientWidth');
                this.view.getElement().setStyle('cursor', 'pointer');
                this.infotip.displayInfotip(width);
            }.bind(this));

            infotipText.addEventHandler('mouseout', function () {
                this.infotip.hideInfotip();
            }.bind(this));

            infotipText.addEventHandler('click', function (e) {
                if (this.dialogContent) {
                    e.preventDefault();
                    createDialogBox.call(this);
                    this.helpDialog.show();
                }
            }.bind(this));

        }
    });

    function createDialogBox () {
        this.helpDialog = new Dialog({
            type: 'information',
            header: this.dialogContent.title,
            content: this.dialogContent.content,
            buttons: [{
                caption: 'Close',
                action: function () {
                    this.helpDialog.hide();
                }.bind(this)
            }]
        });
    }
});
