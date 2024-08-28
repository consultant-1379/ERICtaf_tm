/*global define*/
define([
    'jscore/ext/mvp',
    'jscore/ext/utils/base/underscore',
    '../common/ObjectHelper'
], function (mvp, _, ObjectHelper) {
    'use strict';

    return mvp.Model.extend({

        clear: function () {
            var model = this._model;
            return model.clear.apply(model, arguments);
        },

        /**
         * Looks for attributes containing specific string or match regular
         * expression. If second argument is provided, it will specify which attributes should be looked up for matches.
         * Returns an array containing attribute names.
         *
         * @method search
         * @param {String|RegExp} pattern
         * @param {Array} attributeNames
         * @return {Array} [attributeNames]
         * @example
         *  model.search('James', ['My Test Case', 'testCase.title']);
         *  model.search('James', ['Nikolay', 'user.lastName']);
         *  model.search('James', ['firstName', 'lastName']);
         */
        search: function (pattern, attributeNames) {
            var model = this._model;
            var attributes = model.attributes;
            var isRegExp = _.isRegExp(pattern);

            return _.filter(_.keys(attributes), function (key) {
                var attributeName;
                if (_.isArray(attributeNames)) {
                    attributeName = _.find(attributeNames, function (attrName) {
                        attrName = _.first(attrName.split('.'));
                        return attrName === key;
                    }, key);
                }

                if (attributeNames && _.isUndefined(attributeName)) {
                    return false;
                }

                var value = ObjectHelper.findValue(attributes, attributeName);
                value = _.isNumber(value) ? value.toString() : value;
                if (!_.isString(value)) {
                    return false;
                }

                if (isRegExp) {
                    return pattern.test(value.toString());
                } else {
                    return value.indexOf(pattern) !== -1;
                }
            });
        }
    });

});
