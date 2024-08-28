define([
    'jscore/ext/mvp',
    './TestCaseModel'
], function (mvp, TestCaseModel) {
    'use strict';

    return mvp.Collection.extend({

        Model: TestCaseModel,

        url: '/tm-server/api/statistics/test-cases'

    });
});
