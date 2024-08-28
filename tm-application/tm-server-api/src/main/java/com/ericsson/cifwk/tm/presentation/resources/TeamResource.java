package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
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
@Path("team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TeamResource {

    /**
     * Request team by ID.<br>
     * Returns team reference data if such exists for specified ID.
     *
     * @param q         Search query string. Example format: name=sovereign<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return Teams data.
     */
    @GET
    Response getTeams(@QueryParam("q") String q,
                      @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                      @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                      @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                      @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);


    /**
     * Request to create team.<br>
     * If team with such ID exists, checks to see if product was deleted.
     * If team with specified ID exists and is deleted, it is re-created.
     * If team with specified ID exists and is not deleted, exception is thrown.
     *
     * @param team TeamInfo for team to be created.
     * @return created team.
     */
    @POST
    Response create(@Authorized @Valid TeamInfo team);

    /**
     * Request to update team with specified ID.<br>
     * If team entity with specified ID doesn't exist, the it's created.
     *
     * @param id   team Id.
     * @param team TeamInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Valid @Authorized @HasId TeamInfo team);

    /**
     * Request to delete team with specified ID.<br>
     * If team entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id team ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
