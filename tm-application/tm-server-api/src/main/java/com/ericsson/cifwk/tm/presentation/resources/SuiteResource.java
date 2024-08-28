package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
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
@Path("suite")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SuiteResource {

    /**
     * Request suite by ID.<br>
     * Returns suite reference data if such exists for specified ID.
     * @param q         Search query string.<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return suites data.
     */
    @GET
    Response getSuites(@QueryParam("q") String q,
                       @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                       @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                       @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                       @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);


    /**
     * Request to create suite.<br>
     * If suite with such ID exists, checks to see if product was deleted.
     * If suite with specified ID exists and is deleted, it is re-created.
     * If suite with specified ID exists and is not deleted, exception is thrown.
     *
     * @param suite SuiteInfo for suite to be created.
     * @return created suite.
     */
    @POST
    Response create(@Authorized @Valid SuiteInfo suite);

    /**
     * Request to update suite with specified ID.<br>
     * If suite entity with specified ID doesn't exist, the it's created.
     *
     * @param id    suite Id.
     * @param suite SuiteInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @HasId @Valid SuiteInfo suite);

    /**
     * Request to delete suite with specified ID.<br>
     * If suite entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id suite ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
