/*global define*/
define([
    '../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getId: function () {
            return this.getAttribute('id');
        },

        /**
         * Returns Array of objects with "id" and "title".
         *
         * @returns {string|Array}
         */
        getItems: function () {
            return this.getAttribute('items');
        },

        setItems: function (items) {
            return this.setAttribute('items', items);
        },

        parse: function (data) {
            data.items = data.items.map(function (item) {
                return {
                    itemObj: item,
                    name: item.title,
                    value: item.id,
                    title: item.title
                };
            });
            return data;
        }

    });

});
