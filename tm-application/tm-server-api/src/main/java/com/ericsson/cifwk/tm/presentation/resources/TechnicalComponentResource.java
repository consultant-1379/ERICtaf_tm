package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
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
@Path("components")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TechnicalComponentResource {

    /**
     * Request Technical Components by query.<br>
     * Returns Technical Components reference data if such exists based on query.
     *
     * @param q         Search query string. Example format: name=CM_IMPORT<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return Technical Components data.
     */
    @GET
    Response getTechnicalComponents(@QueryParam("q") String q,
                                    @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                                    @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                                    @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                                    @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request technical component by ID.<br>
     * Returns technical component reference data if such exists for specified ID.
     *
     * @param id technical component ID.
     * @return technical component Reference Data for specified technical component ID.
     */
    @GET
    @Path("{id}")
    Response getTechnicalComponent(@PathParam("id") Long id);

    /**
     * Request to create technical component with specified ID.<br>
     * If technical component with such ID exists, checks to see if technical component was deleted.
     * If technical component with specified ID exists and is deleted, it is re-created.
     * If technical component with specified ID exists and is not deleted, exception is thrown.
     *
     * @param technicalComponentInfo for the technical component to create.
     * @return Created technical component Reference Data Item.
     */
    @POST
    Response create(@Authorized @Valid TechnicalComponentInfo technicalComponentInfo);

    /**
     * Request to update Technical Component with specified ID.<br>
     * If technical component entity with specified ID doesn't exist, then it's created.
     *
     * @param id                     Technical Component ID.
     * @param technicalComponentInfo TechnicalComponentInfo technicalComponentInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @Authorized @HasId @Valid TechnicalComponentInfo technicalComponentInfo);

    /**
     * Request to delete Technical Component with specified ID.<br>
     * If technical component entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id Technical Component ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long id);

}
