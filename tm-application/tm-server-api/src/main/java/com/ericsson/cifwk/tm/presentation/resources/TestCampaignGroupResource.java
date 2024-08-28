package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;
import com.ericsson.cifwk.tm.presentation.validation.HasId;

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
@Path("testCampaignGroup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestCampaignGroupResource {

    /**
     * Request test campaign group by query.<br>
     * Returns test campaign groups if such exists based on query.
     *
     * @param q         Search query string.<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return test campaign groups data.
     */
    @GET
    Response getTestCampaignGroups(@QueryParam("q") String q,
                       @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                       @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                       @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                       @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request test campaign group by ID.<br>
     * Returns test campaign group if such exists for specified ID.
     *
     * @param id ID of test campaign group.
     * @return test campaign group for specified test campaign group ID.
     */
    @GET
    @Path("{id}")
    Response getTestCampaignGroup(@PathParam("id") Long id);

    /**
     * Request to create a test campaign group with specified ID.<br>
     * If test campaign group with such ID exists, exception is thrown.
     *
     * @param testCampaignGroupInfo TestCampaignGroupInfo for test campaign group to create.
     * @return Created test campaign group.
     */
    @POST
    Response create(TestCampaignGroupInfo testCampaignGroupInfo);

    /**
     * Request to update test campaign group with specified ID.<br>
     * If test campaign group entity with specified ID doesn't exist, the it's created.
     *
     * @param id test campaign group ID.
     * @param testCampaignGroupInfo   TestCampaignGroupInfo
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id, @HasId TestCampaignGroupInfo testCampaignGroupInfo);

    /**
     * Request to delete test campaign group with specified ID.<br>
     * If test campaign group entity doesn't exist or is deleted then response code 404 (Not Found) is returned.
     *
     * @param id test campaign group ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") Long id);

    /**
     * Request to create a csv of all test cases in a test campaign group.<br>
     *
     * @param id TestCampaignGroup ID.
     * @return Creates a csv report of all test cases.
     */
    @GET
    @Path("csv/{id}")
    @Produces("text/csv")
    Response getTestCampaignGroupCSV(@PathParam("id") Long id);
}
