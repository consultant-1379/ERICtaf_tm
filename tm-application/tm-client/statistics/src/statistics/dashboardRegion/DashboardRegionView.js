define([
    'jscore/core',
    'template!./DashboardRegion.html'
], function (core, template) {

    return core.View.extend({

        getTemplate: function () {
            return template(this.options);
        }

    });

});
