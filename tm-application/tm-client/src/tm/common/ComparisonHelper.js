define(function () {
    'use strict';

    var ComparisonHelper = {};

    ComparisonHelper.compareTextAndAddText = function (text1, text2, element) {
        if (text1 !== text2) {
            if (text2 === null || text2.length === 0) {
                text2 = 'This field was empty!';
            }
            element.setText(text2);
            return element;
        }
        return null;
    };

    ComparisonHelper.compareBooleanAndAddText = function (text1, text2, element) {
        if (text1 !== text2) {
            if (text2) {
                text2 = 'Yes';
            } else {
                text2 = 'No';
            }
            element.setText(text2);
            return element;
        }
        return null;
    };

    return ComparisonHelper;
});
