/*global define*/
define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getId: function () {
            return this.getAttribute('id');
        },

        getType: function () {
            return this.getAttribute('type');
        },

        setType: function (type) {
            return this.setAttribute('type', type);
        },

        getText: function () {
            return this.getAttribute('text');
        },

        setText: function (text) {
            return this.setAttribute('text', text);
        },

        getStartDate: function () {
            return this.getAttribute('startDate');
        },

        setStartDate: function (startDate) {
            return this.setAttribute('startDate', startDate);
        },

        getEndDate: function () {
            return this.getAttribute('endDate');
        },

        setEndDate: function (endDate) {
            return this.setAttribute('endDate', endDate);
        },

        getAuthor: function () {
            return this.getAttribute('author');
        },

        setAuthor: function (author) {
            return this.setAttribute('author', author);
        }

    });

});
