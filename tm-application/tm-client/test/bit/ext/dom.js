define([
    'jscore/base/jquery',
    'jscore/core'
], function ($, core) {
    'use strict';

    return {

        find: function (selector, contextElement) {
            var result = [];

            if (!contextElement) {
                return result;
            }

            var newElements = $(contextElement._getHTMLElement()).find(selector);
            if (newElements !== undefined) {
                newElements.each(function (index, newEl) {
                    result.push(core.Element.wrap(newEl));
                });
            }

            return result;
        },

        is: function (selector, element) {
            if (!element) {
                return false;
            }
            return $(element._getHTMLElement()).is(selector);
        }

    };

});
