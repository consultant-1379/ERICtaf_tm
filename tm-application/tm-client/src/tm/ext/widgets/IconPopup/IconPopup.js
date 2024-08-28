define([
    'widgets/InfoPopup'
], function (IconPopup) {
    'use strict';

    return IconPopup.extend({

        init: function (options) {
            IconPopup.prototype.init.apply(this, options);
            this._iconName = options.icon !== undefined ? options.icon : 'info';
        },

        onViewReady: function () {
            IconPopup.prototype.onViewReady.apply(this);
            this.setIcon(this._iconName);

        },

        setIcon: function (iconName) {
            this.view.getInfoIcon().removeModifier(this._iconName);
            this.view.getInfoIcon().setModifier(iconName);
            this._iconName = iconName;
        },

        setTitle: function (title) {
            this.view.getInfoIcon().setAttribute('title', title);
        },

        getVisible: function () {
            return this._isVisible;
        }

    });

});
