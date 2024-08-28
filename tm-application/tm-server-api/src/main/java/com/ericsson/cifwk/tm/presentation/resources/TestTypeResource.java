package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
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
@Path("test-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestTypeResource {

    /**
     * Request test type by query<br>
     * Returns test type reference data if such exists based on query.
     *
     * @param q         Search query string.<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return test type data.
     */
    @GET
    Response getTestTypes(@QueryParam("q") String q,
                          @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                          @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                          @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                          @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request test type by ID.<br>
     * Returns test type reference data if such exists for specified ID.
     *
     * @param id test type ID.
     * @return test type Data for specified feature ID.
     */
    @GET
    @Path("{id}")
    Response getTestType(@PathParam("id") Long id);

    /**
     * Request to create test type with specified ID.<br>
     * If test type with such ID exists, checks to see if test type was deleted.
     * If test type with specified ID exists and is deleted, it is re-created.
     * If test type with specified ID exists and is not deleted, exception is thrown.
     *
     * @param testTypeInfo for the test type to create.
     * @return Created test type Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid TestTypeInfo testTypeInfo);

    /**
     * Request to update test type with specified ID.<br>
     * If test type entity with specified ID doesn't exist, then it's created.
     *
     * @param id           test type ID.
     * @param testTypeInfo TestTypeInfo testTypeInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @Valid @HasId TestTypeInfo testTypeInfo);

    /**
     * Request to delete test type with specified ID.<br>
     * If test type entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id test type ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
