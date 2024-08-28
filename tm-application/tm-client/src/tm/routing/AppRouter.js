/*global define, window*/
define([
    'router/Router'
], function (Router) {
    'use strict';

    var AppRouter = function (namespace, pages, eventBus) {
        this.namespace = namespace;
        this.pages = pages;
        this.eventBus = eventBus;

        this.router = new Router();
    };

    function onHashChange () {
        /*jshint validthis:true */
        var match = window.location.href.match(/#(.*)$/);
        this.router.trigger(match ? match[1] : '');
    }

    AppRouter.prototype.start = function (routes) {
        this.router.match(function (match) {
            match(this.namespace, routes);
        }.bind(this));
        this.router.start();

        // listen to hash change, support history
        window.addEventListener('hashchange', onHashChange.bind(this), false);
        onHashChange.call(this);
    };

    AppRouter.prototype.stop = function () {
        this.router.stop();
    };

    AppRouter.prototype.getParameterFromSearchQuery = function (name, searchQuery) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]{0,1}' + name + '[=~]([^&#]*)');
        var results = regex.exec(searchQuery);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    /* Code for TM.js
     routeMatching: function (match) {
     match('(/)', function (match) {
     match('').to(function (builder) {
     console.log('Default url ', builder.getLocation());
     });

     match('/').to(function (builder) {
     console.log('Quick search: ', builder.getQuery());
     });

     match('/search/').to(function (builder) {
     console.log('Advanced search: ', builder.getQuery());
     });
     });

     match('/viewTC/:id').to(function (id, builder) {
     console.log(id, builder.getLocation());
     });

     },
     */

    return AppRouter;

});
