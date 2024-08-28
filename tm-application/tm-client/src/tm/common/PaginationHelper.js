define([
    'widgets/Pagination',
    './ModelHelper'
], function (Pagination, ModelHelper) {
    'use strict';

    var PaginationHelper = function () {
    };

    PaginationHelper.prototype.createPagination = function (collection, parent) {
               if (this.pagination != null) {
                   this.pagination.destroy();
                   this.pagination = null;
               }

               var perPage = collection.getPerPage();
               if (collection.getTotalCount() > perPage) {
                   var pages = Math.ceil(collection.getTotalCount() / perPage);
                   this.pagination = new Pagination({
                       pages: pages,
                       selectedPage: collection.getPage(),
                       onPageChange: function (pageNumber) {
                           if (collection.getPage() !== pageNumber) {
                               collection.setPage(pageNumber);

                               collection.fetch({
                                   reset: true,
                                   statusCode: ModelHelper.statusCodeHandler(this.eventBus)
                               });

                           }
                       }.bind(this)
                   });
                   this.pagination.attachTo(parent);
               }
               return this.pagination;
           };

    return PaginationHelper;

});
