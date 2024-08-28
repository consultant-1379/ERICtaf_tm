/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.responses;

import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class PaginationHelper {

    private PaginationHelper() {
    }

    public static <T> Response page(Paginated<T> paginated,
                                    UriInfo uriInfo) {
        return page(
                paginated,
                uriInfo,
                Functions.<T>identity(),
                Collections.<String, Object>emptyMap(),
                null);
    }

    public static <T, U> Response page(Paginated<T> paginated,
                                       UriInfo uriInfo,
                                       Function<T, U> transformer) {
        return page(
                paginated,
                uriInfo,
                transformer,
                Collections.<String, Object>emptyMap(),
                null);
    }

    public static <T, U> Response page(Paginated<T> paginated,
                                       UriInfo uriInfo,
                                       Function<T, U> transformer,
                                       Class<? extends DtoView<U>> view) {
        return page(
                paginated,
                uriInfo,
                transformer,
                Collections.<String, Object>emptyMap(),
                view);
    }

    public static <T, U> Response page(Paginated<T> paginated,
                                       UriInfo uriInfo,
                                       Function<T, U> transformer,
                                       Map<String, ?> meta) {
        return page(paginated, uriInfo, transformer, meta, null);
    }

    public static <T, U> Response page(Paginated<T> paginated,
                                       UriInfo uriInfo,
                                       Function<T, U> transformer,
                                       Map<String, ?> meta,
                                       Class<? extends DtoView<U>> view) {
        int page = paginated.getPage();
        int perPage = paginated.getPerPage();
        long totalCount = paginated.getTotal();
        int lastPage = (int) Math.ceil(totalCount * 1.0 / perPage);
        if ((page > lastPage && totalCount != 0) || (page != Pagination.FIRST_PAGE && totalCount == 0)) {
            String message = String.format("Page %d (with %d items per page) doesn't exist", page, perPage);
            return Responses.badRequest(message);
        }
        List<U> items = Lists.transform(paginated.getResults(), transformer);

        PageWrapper<U> wrapper = new PageWrapper<>();
        wrapper.setTotalCount(totalCount);
        wrapper.setItems(items);
        wrapper.setMeta(meta);

        Response.ResponseBuilder responseBuilder;
        if (view != null) {
            responseBuilder = Response.ok().entity(wrapper, Responses.getJsonView(view));
        } else {
            responseBuilder = Response.ok().entity(wrapper);
        }
        if (page > Pagination.FIRST_PAGE) {
            responseBuilder.link(pageLink(uriInfo, Pagination.FIRST_PAGE), "first");
            responseBuilder.link(pageLink(uriInfo, page - 1), "prev");
        }
        if (page < lastPage) {
            responseBuilder.link(pageLink(uriInfo, page + 1), "next");
            responseBuilder.link(pageLink(uriInfo, lastPage), "last");
        }
        return responseBuilder.build();
    }

    private static URI pageLink(UriInfo uriInfo, int page) {
        return uriInfo.getAbsolutePathBuilder()
                .replaceQueryParam(Pagination.PAGE_PARAM, page)
                .build();
    }

}
