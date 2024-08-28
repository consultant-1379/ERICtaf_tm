define([
    'jscore/ext/mvp',
    './UserModel'
], function (mvp, UserModel) {
    'use strict';

    return mvp.Collection.extend({

        Model: UserModel,

        url: '/tm-server/api/statistics/users'

    });
});
