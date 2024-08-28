/*global define*/
define([
    '../../../ext/mvpCollection',
    './ProductModel'
], function (Collection, ProjectModel) {
    'use strict';

    return Collection.extend({

        Model: ProjectModel,

        url: '/tm-server/api/products'

    });

});
