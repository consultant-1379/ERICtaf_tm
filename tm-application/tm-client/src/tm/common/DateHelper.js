define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    'use strict';

    var DateHelper = {};

    /**
     * Checks if date object is valid.
     *
     * @param {Object} date
     * @returns {boolean}
     */
    DateHelper.isDate = function (date) {
        return _.isDate(date) && !isNaN(date.getTime());
    };

    /**
     * Returns date object or null from given parameter.
     * Parameter could be date or String.
     *
     * @param {Date|string} date
     * @returns {Date|null}
     */
    DateHelper.getDateObj = function (date) {
        if (date == null) {
            return null;
        }
        if (DateHelper.isDate(date)) {
            return date;
        }
        var dateObj = new Date(date);
        if (DateHelper.isDate(dateObj)) {
            return dateObj;
        }
        return null;
    };

    /**
     * Returns formatted datetime string according users locale PC settings.
     *
     * @param {Date|string} dateStr
     * @returns {string}
     */
    DateHelper.formatStringToDatetime = function (dateStr) {
        var dateObj = DateHelper.getDateObj(dateStr);
        if (dateObj === null) {
            return '';
        }
        return dateObj.toLocaleString();
    };

    /**
     * Returns formatted date string according users locale PC settings.
     *
     * @param {Date|String} dateStr
     * @returns {String}
     */
    DateHelper.formatStringToDate = function (dateStr) {
        var dateObj = DateHelper.getDateObj(dateStr);
        if (dateObj === null) {
            return '';
        }
        return dateObj.toLocaleDateString();
    };

    return DateHelper;

});
