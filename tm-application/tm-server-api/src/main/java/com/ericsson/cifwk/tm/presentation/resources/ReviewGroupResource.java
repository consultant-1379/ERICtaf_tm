package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 */
@Path("review-group")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReviewGroupResource {

    /**
     * Returns list with Review Groups.
     * <br>
     * There are two ways to use this method:
     * <ol>
     * <li>Search by <code>id</code> query parameter with a list of Review Group IDs.</li>
     * <li>Search by query string using <code>q</code> query parameter. Returned results are paginated.</li>
     * </ol>
     *
     * @param q         Search query string.<br>
     *                  Example format: {@literal requirement~1&type=Functional}<br>
     *                  '~' - contains predicate, '=' - equals predicate, '!=' - not equals predicate.
     * @param page      Page number.
     * @param perPage   Number of Review Groups per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     * @return List with Review Groups
     */
    @GET
    Response getReviewGroups(@QueryParam("q") String q,
                             @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                             @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                             @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                             @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);


    /**
     * Request to delete a Review Group by ID.<br>
     * Id in request body should exist and Review Group should be deleted.
     *
     * @param id Existing non deleted Review Group ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") Long id);


    /**
     * Request to update Review Group by ID.<br>
     * Id in request body should match ID in Review Group Info object.
     *
     * @param id              Existing Review Group ID.
     * @param reviewGroupInfo Review Group Info object with same ID.
     * @return Response with request status and updated review group information.
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id,
                    @Valid ReviewGroupInfo reviewGroupInfo);


    /**
     * Request to create new Review Group.<br>
     * New Review Group should have unique Review Group ID.
     * If Review Group with such ID exists, checks to see if product was deleted.
     * If Review Group with specified ID exists and is deleted, it is re-created.
     * If Review Group with specified ID exists and is not deleted, exception is thrown.
     *
     * @param reviewGroupInfo Review Group Info object.
     * @return Response with request status.
     */
    @POST
    Response create(@Authorized @Valid ReviewGroupInfo reviewGroupInfo);

    /**
     * Returns list with Review Group IDs containing search string in their Review Group ID.<br>
     * Results are limited with limit parameter with a default value 20.
     *
     * @param search Search string containing Review Group ID or part of it.
     * @param limit  Results limitation param.
     * @return List with Review Group IDs
     */
    @GET
    @Path("completion")
    Response getCompletion(@QueryParam("search") @NotNull String search,
                           @QueryParam("limit") @Min(1) @DefaultValue("20") int limit);

}
