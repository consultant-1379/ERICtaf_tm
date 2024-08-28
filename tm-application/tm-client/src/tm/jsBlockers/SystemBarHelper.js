define([
], function () {
    'use strict';

    return {
        /*jshint validthis:true */
        createSystemBarLink: function () {
            this.headerText = window.document.getElementsByClassName('eaContainer-SystemBar-name');

            this.headerText[0].onclick = function () {
                window.location.href = '#tm';
            };
        }
    };
});
