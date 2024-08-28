define([
    '../../../ext/mvpCollection',
    './IsoModel'
], function (Collection, IsoModel) {
    'use strict';

    return Collection.extend({

        Model: IsoModel,

        init: function () {
            this.dropId = null;
            this.baseUrl = '/tm-server/api/isos?dropId=';
        },

        url: function () {
            return this.baseUrl + this.dropId;
        },

        setDropId: function (dropId) {
            this.dropId = dropId;
        }

    });

});
