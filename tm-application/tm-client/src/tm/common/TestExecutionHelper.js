define([
    'jscore/ext/utils/base/underscore',
    './Constants'
], function (_, Constants) {
    'use strict';

    var TestExecutionHelper = function () {

    };

    TestExecutionHelper.prototype.calculateTestExecutions = function (model) {
        var resultMap = {};
        if (model) {
            var total = model.length;

            _.each(model, function (item) {
                if (item.result == null) {
                    item.result = {
                        group: 'Not started',
                        title: 'Not started',
                        id: ''
                    };
                }

                if (resultMap[item.result.title]) {
                    resultMap[item.result.title].count += 1;
                    resultMap[item.result.title].value = Math.floor((resultMap[item.result.title].count / total) * 100);
                    resultMap[item.result.title].title =
                        item.result.title + ' (' + resultMap[item.result.title].count + '/' + total + ') ' +
                        resultMap[item.result.title].value.toFixed(1) + '%';
                    return;
                }

                var value = parseFloat((1 / total).toFixed(5) * 100);
                resultMap[item.result.title] = {
                    count: 1,
                    value: value,
                    color: Constants.executionStatusMap[item.result.id.toString()].color,
                    title: item.result.title + ' (' + 1 + '/' + total + ') ' + value.toFixed(1) + '%',
                    group: item.result.title
                };

                if (item.result.title === 'Pass') {
                    resultMap[item.result.title].report = true;
                }
            });
        }
        return resultMap;
    };

    TestExecutionHelper.prototype.convertToList = function (resultMap) {
        var resultList = [];
        _.each(resultMap, function (item) {
            resultList.push(item);
        });

        return resultList;

    };

    TestExecutionHelper.prototype.calculateFeaturePerType = function (items) {
        var resultMap = {};
        _.each(items, function (item) {
            var label = item.testCase.feature.name;
            var type = item.testCase.type.title;

            if (resultMap[label]) {
                if (resultMap[label].type[type]) {
                    resultMap[label].type[type].value += 1;
                } else {
                    resultMap[label].type[type] = {label: type, value: 1};
                }
            } else {
                var typeMap = {};
                typeMap[type] = {label: type, value: 1};
                resultMap[label] = {label: label, type: typeMap};
            }
        }.bind(this));

        _.each(resultMap, function (item) {
            item.data = TestExecutionHelper.prototype.convertToList(item.type);
        }.bind(this));

        return resultMap;
    };

    return TestExecutionHelper;

});
