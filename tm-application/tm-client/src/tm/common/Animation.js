define([
    'jscore/ext/utils/base/underscore',
    '../ext/domUtils'
], function (_, domUtils) {
    'use strict';

    var Animation = function (el, eventBus) {
        this.el = el;
        this.eventBus = eventBus;
        this.showEvent = null;
        this.hideEvent = null;
        this.visible = null;
    };

    Animation.prototype.show = function () {
        if (this.visible) {
            return;
        }
        this.visible = true;

        this.el.setModifier('current', undefined, 'eb-animatedPage');
        this.el.setModifier('animate', 'fadeIn', 'eb');

        if (this.hideEvent != null) {
            this.el.removeEventHandler(this.hideEvent);
        }
        var animationEvent = domUtils.getAnimationEvent();
        this.showEvent = this.el.addEventHandler(animationEvent, function () {
            this.el.removeModifier('animate', 'eb');
            this.el.removeEventHandler(this.showEvent);
        }, this);
    };

    Animation.prototype.showOn = function (event, fn) {
        this.eventBus.subscribe(event, function () {
            this.show();
            if (fn != null) {
                fn.apply(null, arguments);
            }
        }, this);
    };

    Animation.prototype.hide = function () {
        if (this.visible != null && !this.visible) {
            return;
        }
        this.visible = false;

        this.el.setModifier('animate', 'fadeOut', 'eb');

        var animationEvent = domUtils.getAnimationEvent();
        this.hideEvent = this.el.addEventHandler(animationEvent, function () {
            this.el.removeModifier('current', 'eb-animatedPage');
            this.el.removeModifier('animate', 'eb');
            this.el.removeEventHandler(this.hideEvent);
        }, this);
    };

    Animation.prototype.hideOn = function (event, fn) {
        this.eventBus.subscribe(event, function () {
            this.hide();
            if (fn != null) {
                fn.apply(null, arguments);
            }
        }, this);
    };

    Animation.prototype.markCurrent = function () {
        this.el.setModifier('current', undefined, 'eb-animatedPage');
    };

    Animation.prototype.markCurrentOn = function (event, fn) {
        this.eventBus.subscribe(event, function () {
            this.markCurrent();
            if (fn != null) {
                fn.apply(null, arguments);
            }
        }, this);
    };

    return Animation;
});
