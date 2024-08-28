define([
    'jscore/core',
    'widgets/Tooltip',
    '../../Constants',
    './TableProgressBarView',
    '../../TestExecutionHelper',
    'widgets/ProgressBar'
], function (core, Tooltip, Constants, View, TestExecutionHelper, ProgressBar) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function (options) {
            this.testCampaignItems = options.value || [];
            this.title = '';
        },

        onViewReady: function () {
            this.testExecutionHelper = new TestExecutionHelper();
            var resultMap = this.testExecutionHelper.calculateTestExecutions(this.testCampaignItems);
            var resultValue = 0;
            var count = 0;
            var total = this.testCampaignItems.length;

            var color = 'green';
            if (resultMap.Pass) {
                resultValue += resultMap.Pass.value;
                count += resultMap.Pass.count;
            }
            if (resultMap['Passed with exception']) {
                resultValue += resultMap['Passed with exception'].value;
                count += resultMap['Passed with exception'].count;
            }
            if (resultMap['N/A']) {
                resultValue += resultMap['N/A'].value;
                count += resultMap['N/A'].count;
            }

            if (resultValue < 100) {
                color = 'red';
            }

            this.title = '(' + count + '/' + total + ') ' + resultValue + '%';

            var progressBar = new ProgressBar({
                value: parseFloat(resultValue.toFixed(1)),
                color: color
            });

            progressBar.attachTo(this.getElement());

        },

        getTooltip: function () {
            return this.title;
        }

    });
})
;
