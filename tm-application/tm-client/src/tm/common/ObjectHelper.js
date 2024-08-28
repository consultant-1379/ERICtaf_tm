define(function () {
    'use strict';

    var ObjectHelper = {};
    var LEADING_DOT = /^\./;
    var INDEXING_DOT = /\[(\w+)\]/g;
    var INDEX_TO_PROPERTIES = '.$1';

    /**
     * Select n-levels deep into an object given a dot/bracket-notation query.
     * If partially applied, returns a function accepting the second argument.
     *
     * ### Examples:
     *
     *      findValue(contact, 'name.first');
     *
     *      findValue(contact, 'addresses[0].street');
     *
     *      findValue(contacts, 'addresses.street');
     *
     * @param  {array|string} query - dot/bracket-notation query string
     *
     * @param  {Object|array} object - object to access
     *
     * @return {Object} value at given reference or undefined if it does not exist
     */
    ObjectHelper.findValue = function (object, query) {
        query = query.replace(INDEXING_DOT, INDEX_TO_PROPERTIES); // convert indexes to properties
        query = query.replace(LEADING_DOT, '');           // strip a leading dot
        var properties = query.split('.');
        var len = properties.length;

        for (var i = 0, n = len; i < n; ++i) {
            var property = properties[i];
            if (object instanceof Array) {
                for (var j = 0; j < object.length; j++) {
                    if (property in object[j]) {
                        object = object[j][property];
                        break;
                    }
                }
            } else if (object == null) {
                return null;
            } else if (object.hasOwnProperty(property)) {
                object = object[property];
            } else {
                throw new Error('Property ' + property + ' for ' + query + ' is not found in the object');
            }
        }
        return object;
    };

    /**
     * Select n-levels deep into a collection given a dot/bracket-notation query.
     * If partially applied, returns a function accepting the second argument.
     *
     * ### Examples:
     *
     *      findAll(collection, 'name.first');
     *
     * @param  {array|string} query - dot/bracket-notation query string
     *
     *
     * @return {Collection} all values at given reference
     */
    ObjectHelper.findAll = function (collection, query) {
        query = query.replace(INDEXING_DOT, INDEX_TO_PROPERTIES); // convert indexes to properties
        query = query.replace(LEADING_DOT, '');           // strip a leading dot
        var properties = query.split('.');
        var len = properties.length;

        for (var i = 0, n = len; i < n; ++i) {
            var property = properties[i];
            if (collection instanceof Array) {
                var arrayObject = [];
                for (var j = 0; j < collection.length; j++) {
                    if (property in collection[j]) {
                        arrayObject[j] = collection[j][property];
                    }
                }
                collection = arrayObject;
            } else if (collection == null) {
                    return null;
            } else {
                throw new Error('Property ' + property + ' for ' + query + ' is not found in the object');
            }
        }
        return collection;
    };
    return ObjectHelper;
});
