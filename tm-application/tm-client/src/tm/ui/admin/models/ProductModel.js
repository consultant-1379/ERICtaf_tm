define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/products/';
        },

        getName: function () {
            return this.getAttribute('name');
        },

        setName: function (name) {
            this.setAttribute('name', name);
        },

        setProduct: function (product) {
            this.setAttribute('product', product);
        },

        setExternalId: function (externalId) {
            this.setAttribute('externalId', externalId);
        }
    });

});
