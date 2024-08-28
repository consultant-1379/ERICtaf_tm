define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    'use strict';

    var CLOSED_JIRA_STATUSES = [
        'CLOSED',
        'RESOLVED',
        'DONE'
    ];
    var OPENED_JIRA_STATUSES = [
        'NEW',
        'OPEN'
    ];

    var RequirementCommonHelper = function () {
    };

    RequirementCommonHelper.updateColumnWidth = function (lsNamespace, column) {
        var ls = JSON.parse(localStorage[lsNamespace] || '{}');
        ls[column.attribute] = column.width;
        localStorage[lsNamespace] = JSON.stringify(ls);
    };

    RequirementCommonHelper.iconNameByStatus = function (status) {
        if (!status) {
            return 'silverCircle';
        }
        if (_.indexOf(CLOSED_JIRA_STATUSES, status.toUpperCase()) > -1) {
            return 'greenCircle';
        } else if (_.indexOf(OPENED_JIRA_STATUSES, status.toUpperCase()) > -1) {
            return 'blueCircle';
        }
        return 'yellowCircle';
    };

    RequirementCommonHelper.composeTooltipString = function (statusName, title, externalId, url, deliveredIn) {
        var pattern =
            '<p>Status: <b>@STATUS@</b></p>' +
            '<div>' +
            '<p><i>@SUBJECT@</i></p>' +
            '<div>' +
            'Details: <a target="_blank" href=@URL_HREF@>@URL_TEXT@</a>' +
            '<br>Delivered In: <b>@DELIVEREDIN@<b>' +
            '</div>' +
            '</div>';

        pattern = pattern.replace('@STATUS@', statusName ? statusName : 'N/A');
        pattern = pattern.replace('@SUBJECT@', title ? title : '');
        pattern = pattern.replace('@URL_HREF@', url);
        pattern = pattern.replace('@URL_TEXT@', externalId ? externalId : url);
        pattern = pattern.replace('@DELIVEREDIN@', deliveredIn ? deliveredIn : '');

        return pattern;
    };

    return RequirementCommonHelper;

});
