/*global define*/
define([
    '../../ext/mvpCollection',
    './FileModel'
], function (Collection, ReferenceModel) {
    'use strict';

    return Collection.extend({

        Model: ReferenceModel,

        init: function () {
            this.product = '';
            this.category = '';
            this.id = '';
        },

        url: function () {
            return '/tm-server/api/files/' + this.product + '/' + this.category + '/' + this.id;
        },

        getProduct: function () {
            return this.product;
        },

        setProduct: function (product) {
            this.product = product;
        },

        getCategory: function () {
            return this.category;
        },

        setCategory: function (category) {
            this.category = category;
        },

        getId: function () {
            return this.id;
        },

        setId: function (value) {
            this.id = value;
        }

    });

});
