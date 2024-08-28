define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/drops/';
        },

        getName: function () {
            return this.getAttribute('name');
        },

        getProduct: function () {
            return this.getAttribute('product');
        },

        setName: function (name) {
            this.setAttribute('name', name);
        },

        setProduct: function (product) {
            this.setAttribute('product', product);
        }
    });

});
