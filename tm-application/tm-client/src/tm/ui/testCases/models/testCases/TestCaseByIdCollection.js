define([
    '../../../../ext/mvpCollection',
    './TestCaseModel'
], function (Collection, TestCaseModel) {
    'use strict';

    return Collection.extend({

        Model: TestCaseModel,

        init: function () {
            this.ids = [];
        },

        url: function () {
            var ids = this.ids.join(',');
            return '/tm-server/api/test-cases?id=' + ids;
        },

        getIds: function () {
            return this.ids;
        },

        setIds: function (ids) {
            this.ids = [].concat(ids);
        }

    });

});
