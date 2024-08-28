package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
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
@Path("features")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductFeatureResource {

    /**
     * Request feature by query<br>
     * Returns feature reference data if such exists based on query.
     * @param q         Search query string. Example format: name=PM<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return feature data.
     */
    @GET
    Response getFeatures(@QueryParam("q") String q,
                         @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                         @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                         @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                         @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request feature by ID.<br>
     * Returns feature reference data if such exists for specified ID.
     *
     * @param id feature ID.
     * @return feature Data for specified feature ID.
     */
    @GET
    @Path("{id}")
    Response getFeature(@PathParam("id") Long id);

    /**
     * Request to create feature with specified ID.<br>
     * If feature with such ID exists, checks to see if feature was deleted.
     * If feature with specified ID exists and is feature, it is re-created.
     * If feature with specified ID exists and is not feature, exception is thrown.
     *
     * @param featureInfo for the feature to create.
     * @return Created feature Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid FeatureInfo featureInfo);

    /**
     * Request to update feature with specified ID.<br>
     * If feature entity with specified ID doesn't exist, then it's created.
     *
     * @param id          feature ID.
     * @param featureInfo FeatureInfo featureInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @HasId @Valid FeatureInfo featureInfo);

    /**
     * Request to delete feature with specified ID.<br>
     * If feature entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id feature ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
