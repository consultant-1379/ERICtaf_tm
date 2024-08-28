define([
    '../../../../ext/mvpModel'
], function (Model) {
    'use strict';

    return Model.extend({

        url: function () {
            var url = '/tm-server/api/test-cases/' + this.testCaseId + '/review?status=' + this.status + '&type=' + this.type;
            if (this.reviewGroupId) {
                url += '&reviewGroup=' + this.reviewGroupId;
            }
            if (this.reviewUserId) {
                url += '&reviewUser=' + this.reviewUserId;
            }
            return url;
        },

        getId: function () {
            return this.testCaseId;
        },

        setTestCaseId: function (id) {
            this.testCaseId = id;
        },

        getStatus: function () {
            return this.status;
        },

        setStatus: function (value) {
            this.status = value;
        },

        setType: function (value) {
            this.type = value;
        },

        setReviewGroupId: function (id) {
            this.reviewGroupId = id;
        },

        setReviewUserId: function (id) {
            this.reviewUserId = id;
        }
    });

});
