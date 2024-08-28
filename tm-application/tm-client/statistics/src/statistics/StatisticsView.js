define([
    'jscore/core',
    'template!./statistics.html',
    'styles!./statistics.less',
    'styles!./externalCSS/nv.d3.less'
], function (core, template, style, nvd3) {
    'use strict';

    return core.View.extend({

        getTemplate: function() {
            return template(this.options);
        },

        getStyle: function() {
            return nvd3 +  style;
        }

    });

});
