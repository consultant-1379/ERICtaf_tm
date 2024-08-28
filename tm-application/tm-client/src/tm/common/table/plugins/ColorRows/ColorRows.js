define([
    '../Plugin'
], function (Plugin) {
    'use strict';

    return Plugin.extend({
        /*jshint validthis:true */

        injections: {

            after: {
                addRow: addRow
            }

        }
    });

    function addRow (obj, index) {
        var table = this.getTable(),
            rowIdentifier = index !== undefined ? index : table.getRows().length - 1,
            row = table.getRows()[rowIdentifier];

        if (obj.rowColor) {
            row.getElement().setStyle('background', obj.rowColor);
        }

    }

});
