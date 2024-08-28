define([
    '../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            return '/tm-server/api/jira/create-defect';
        },

        setFields: function (fieldsObj) {
            this.setAttribute('fields', fieldsObj);
        }

    });

});
