/*global define*/
define([
    'jscore/core',
    './RequirementsWidgetView'
], function (core, View) {
    'use strict';

    var Requirements = core.Widget.extend({

        view: function () {
            return new View({
                template: {
                    requirements: this.options.requirements,
                    noRequirementsMessage: Requirements.NO_REQUIREMENTS_MESSAGE
                }
            });
        }

    }, {
        NO_REQUIREMENTS_MESSAGE: 'No requirements are added here.'
    });

    return Requirements;

});
