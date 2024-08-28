define([
    '../Plugin',
    '../../../ObjectHelper'
], function (Plugin, ObjectHelper) {
    'use strict';

    return Plugin.extend({
        /*jshint validthis:true */

        injections: {

            after: {
                addRow: addRow
            }

        }

    });

    function addRow (object, index) {
        var table = this.getTable(),
            rowIdentifier = index !== undefined ? index : table.getRows().length - 1,
            row = table.getRows()[rowIdentifier];

        if (this.options.rowIdentifier) {
            rowIdentifier = ObjectHelper.findValue(object, this.options.rowIdentifier);
        }
        row.getElement().setAttribute('data-id', rowIdentifier);
    }

});
