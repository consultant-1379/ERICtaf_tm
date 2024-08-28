/*global encodeURIComponent*/
define([
    'jscore/ext/mvp',
    'jscore/ext/utils/base/underscore',
    '../common/ObjectHelper'
], function (mvp, _, ObjectHelper) {
    'use strict';

    return mvp.Collection.extend({

        reset: function () {
            var collection = this._collection;
            return collection.reset.apply(collection, arguments);
        },

        escape: function (string) {
            return encodeURIComponent(string);
        },

        getAtIndex: function (index) {
            return this._collection.at(index);
        },

        findBy: function (predicate) {
            return this._collection.find(predicate);
        },

        /**
         * Return the first model with matching attributes.
         *
         * @param attributes
         * @returns {*}
         */
        findWhere: function (attributes) {
            var collection = this._collection;
            var matched = false;

            collection.forEach(function (model) {
                var keyNames = _.keys(attributes);
                keyNames.forEach(function (key) {
                    var foundValue = ObjectHelper.findValue(model.attributes, key);
                    if (attributes[key] === foundValue) {
                        matched = true;
                    }
                });
            });
            return matched;
        }
    });

});
