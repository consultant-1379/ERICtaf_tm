package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;
import com.ericsson.cifwk.tm.presentation.validation.HasId;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

/**
 *
 */
@Path("drops")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DropResource {

    /**
     * Request drop by query<br>
     * Returns drop reference data if such exists based on query.
     *
     * @param q         Search query string. Example format: name=17.1<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return drops data.
     */
    @GET
    Response getDrops(@QueryParam("q") String q,
                      @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                      @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                      @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                      @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request drop by ID.<br>
     * Returns drop reference data if such exists for specified ID.
     *
     * @param id drop ID.
     * @return drop Data for specified drop ID.
     */
    @GET
    @Path("{id}")
    Response getDrop(@PathParam("id") Long id);


    /**
     * Request to create drop with specified ID.<br>
     *
     * @param dropInfo for the feature to create.
     * @return Created drop Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid DropInfo dropInfo);

    /**
     * Request to update drop with specified ID.<br>
     * If drop entity with specified ID doesn't exist, the it's created.
     *
     * @param id    drop Id.
     * @param drop DopInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @Valid @HasId DropInfo drop);

    /**
     * Request to delete drop with specified ID.<br>
     * If drop entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id drop ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id );
}
