define(function () {
    'use strict';

    /**
     * Wrapper around localStorage that saves and loads JSON.
     */
    var LocalStorage = {};

    LocalStorage.get = function (key, defaultValue) {
        var value = window.localStorage.getItem(key);
        if (value == null) {
            return defaultValue || null;
        }
        try {
            return JSON.parse(value);
        } catch (e) {
            return defaultValue || null;
        }
    };

    LocalStorage.set = function (key, value) {
        window.localStorage.setItem(key, JSON.stringify(value));
    };

    return LocalStorage;

});
