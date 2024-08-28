define([
    'jscore/core',
    './HolderWidgetView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({

        view: function () {
            return new View(this.options);
        }

    });
});
