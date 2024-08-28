/*global navigator*/
define([
    'jscore/base/jquery'
], function ($) {
    'use strict';

    return {

        BROWSER_WEBKIT: 'WebKit',
        BROWSER_FIREFOX: 'Firefox',
        BROWSER_MSIE: 'MSIE',

        currentBrowser: null,

        getBrowserName: function () {
            if (this.currentBrowser !== null) {
                return this.currentBrowser;
            }

            if (navigator.userAgent.indexOf('Firefox') !== -1 && parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('Firefox') + 8)) >= 3.6) { //Firefox
                this.currentBrowser = this.BROWSER_FIREFOX;
            } else if (navigator.userAgent.indexOf('MSIE') !== -1 && parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf('MSIE') + 8)) >= 3.6) { //IE
                this.currentBrowser = this.BROWSER_MSIE;
            } else {
                this.currentBrowser = this.BROWSER_WEBKIT;
            }
            return this.currentBrowser;
        },

        getAnimationEvent: function () {
            var animationEventNames = {};
            animationEventNames[this.BROWSER_WEBKIT] = 'webkitAnimationEnd';
            animationEventNames[this.BROWSER_FIREFOX] = 'animationend';
            animationEventNames[this.BROWSER_MSIE] = 'MSAnimationEnd';

            return animationEventNames[this.getBrowserName()];
        },

        textareaAutoAdjust: function (element) {
            element.setStyle('height', '26');
            var scrollHeight = element._getHTMLElement().scrollHeight;
            element.setStyle('height', 2 + scrollHeight);
        },

        offset: function (element) {
            return $(element._getHTMLElement()).offset();
        },

        deepCopy: function (destination, source) {
            $.extend(true, destination, source);
            return destination;
        }

    };

});
