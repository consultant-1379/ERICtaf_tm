/*global define*/
define([
    'jscore/core',
    './InfotipView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function () {
            this.tooltipText = this.options.tooltip;
        },

        onViewReady: function () {
            this.view.afterRender();
            this.view.getText().setText(this.tooltipText);
        },

        setPosition: function () {
            var height = this.view.getText().getProperty('clientHeight');
            if (height > 20) {
                var bottom = height / 50;
                this.view.getArrow().setStyle('margin-top', bottom + 'rem');
            }
        },

        displayInfotip: function (width) {
            if (this.tooltipText) {
                this.getElement().setModifier('show');
                this.setPosition(width);
            }
        },

        hideInfotip: function () {
            if (this.tooltipText) {
                this.getElement().removeModifier('show');
            }
        }
    });

});
