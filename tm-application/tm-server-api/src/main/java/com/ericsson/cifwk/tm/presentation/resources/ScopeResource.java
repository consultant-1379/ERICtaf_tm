package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
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
@Path("scopes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ScopeResource {

    /**
     * Request group by query<br>
     * Returns group reference data if such exists based on query.
     * @param q         Search query string. Example format: name=Regression<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return groups data.
     */
    @GET
    Response getGroups(@QueryParam("q") String q,
                       @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                       @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                       @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                       @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request Scope by ID.<br>
     * Returns scope reference data if such exists for specified ID.
     *
     * @param scopeId Scope ID.
     * @return Scope Reference Data for specified scope ID.
     */
    @GET
    @Path("{scopeId}")
    Response getScope(@PathParam("scopeId") Long scopeId);

    /**
     * Request to create scope with specified ID.<br>
     * If scope with such ID exists, checks to see if scope was deleted.
     * If scope with specified ID exists and is deleted, it is re-created.
     * If scope with specified ID exists and is not deleted, exception is thrown.
     *
     * @param scope GroupInfo for scope to create.
     * @return Created Scope Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid GroupInfo scope);

    /**
     * Request to update Scope with specified ID.<br>
     * If Scope entity with specified ID doesn't exist, the it's created.
     *
     * @param scopeId Scope ID.
     * @param scope   GroupInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long scopeId, @Authorized @HasId @Valid GroupInfo scope);

    /**
     * Request to delete Scope with specified ID.<br>
     * If scope entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param scopeId Scope ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long scopeId);

}
