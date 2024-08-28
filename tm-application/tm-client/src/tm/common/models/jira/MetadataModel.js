/*global define*/
define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        getId: function () {
            return this.getAttribute('id');
        },

        getItems: function () {
            return this.getAttribute('items');
        },

        setItems: function (items) {
            return this.setAttribute('items', items);
        },

        getType: function () {
            return this.getAttribute('type');
        },

        getLabel: function () {
            return this.getAttribute('label');
        },

        getRequired: function () {
            return this.getAttribute('required');
        },

        getFieldName: function () {
            return this.getAttribute('fieldName');
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
