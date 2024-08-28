define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    'use strict';

    var LocationHelper = {};

    /**
     * Extracts URL parts from the given URL.
     *
     * @param {string} url
     * @returns {Object} URL parts
     */
    LocationHelper.parse = function (url) {
        var parser = document.createElement('a');
        parser.href = url;
        return {
            protocol: parser.protocol, // => "http:"
            hostname: parser.hostname, // => "example.com"
            port: parser.port,         // => "3000"
            pathname: parser.pathname, // => "/pathname/"
            search: parser.search,     // => "?search=test"
            hash: parser.hash,         // => "#hash"
            host: parser.host          // => "example.com:3000"
        };
    };

    /**
     * Transforms a query string into a key-value map.
     * Handles key-value collections like "key=value&key=value".
     *
     * @param query
     * @returns {Object} map of query string keys to value lists.
     */
    LocationHelper.parseQuery = function (query) {
        if (!_.isString(query)) {
            return {};
        }
        query = query.trim();
        if (query[0] === '?') {
            query = query.substring(1);
        }
        var map = {};
        query.split('&').forEach(function (pair) {
            var equalsIndex = pair.indexOf('=');
            var key = normalizeKey(decode(pair.slice(0, equalsIndex)));
            var value = decode(pair.slice(equalsIndex + 1));
            if (key !== '') {
                addKeyValue(map, key, value);
            }
        });
        return map;
    };

     /**
         * Transforms a query string into a key-value map with operator.
         * Handles key-value collections like "key=value&key~value".
         *
         * @param query
         * @returns {operator: '=', value: 'example'} map of query string keys to value lists.
         */
        LocationHelper.parseQueryWithOperator = function (query) {
            if (!_.isString(query)) {
                return {};
            }
            query = query.trim();
            if (query[0] === '?') {
                query = query.substring(1);
            }
            var map = {};
            query.split('&').forEach(function (pair) {
                var operatorIndex;
                var operator;
                if (pair.indexOf('=') > 0) {
                    operatorIndex = pair.indexOf('=');
                    operator = '=';
                } else if (pair.indexOf('~') > 0) {
                    operatorIndex = pair.indexOf('~');
                    operator = '~';
                } else {
                    return;
                }
                var key = normalizeKey(decode(pair.slice(0, operatorIndex)));
                var value = decode(pair.slice(operatorIndex + 1));
                if (key !== '') {
                     map[key] = {value: value, operator: operator};
                }
            });
            return map;
        };

    /**
     * Decodes the URI component and additionally replaces "+" with " ".
     *
     * @param component
     * @returns {string} decoded URI component
     */
    var decode = function (component) {
        return decodeURIComponent(component.replace(/\+/g, '%20'));
    };

    /**
     * Removes special symbols and whitespace from query string key name.
     */
    var normalizeKey = function (key) {
        key = key.trim();
        if (key.slice(-2) === '[]') {
            key = key.slice(0, -2);
        }
        return key;
    };

    /**
     * Adds the key-value pair to a map of keys to lists,
     * possibly adding to a list of values for an existing key.
     */
    var addKeyValue = function (map, key, value) {
        var currentValue = map[key];
        if (currentValue != null) {
            map[key] = [].concat(currentValue, value);
        } else {
            map[key] = [value];
        }
    };

    /**
     * Splits URL paths into parts.
     *
     * @param path
     * @returns {Array} list of non-empty path parts
     */
    LocationHelper.splitPath = function (path) {
        return _.chain(path.split('/'))
            .map(function (part) {
                return part.trim();
            })
            .compact()
            .value();
    };

    return LocationHelper;
});
