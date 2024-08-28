define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    'use strict';

    var ValidationHelper = {};

       ValidationHelper.getValidationErrors = function (message) {
                var result = '';
                try {
                    var data = JSON.parse(message.responseText);
                    var index = 1;
                    if (_.isArray(data)) {
                        _.each(data, function (error) {
                            result += index + ') ' + error.message + '\n';
                            index++;
                        });
                    } else {
                        result = data.message;
                    }

                } catch (e) {
                    result = message.responseText;
                }
                return result;
            };

    return ValidationHelper;
});
