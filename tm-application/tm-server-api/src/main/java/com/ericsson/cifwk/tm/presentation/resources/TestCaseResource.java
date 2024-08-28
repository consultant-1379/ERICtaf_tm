package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseSubscriptionInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;
import com.ericsson.cifwk.tm.presentation.validation.CorrectlyFormattedTestCaseId;
import com.ericsson.cifwk.tm.presentation.validation.Editable;
import com.ericsson.cifwk.tm.presentation.validation.HasId;
import com.ericsson.cifwk.tm.presentation.validation.UniqueTestCaseId;
import com.ericsson.cifwk.tm.presentation.validation.ValidTestCaseComponent;
import com.ericsson.cifwk.tm.presentation.validation.XLSXFileType;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
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
@Path("test-cases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestCaseResource {

    /**
     * Returns information whether Test Case with specified ID exists.<br>
     * If Test Case exists, then Response with status code 200 is returned,<br>
     * otherwise status code 404 (Not Found) is returned.
     *
     * @param testCaseId Test Case ID string.
     * @return Response with status code
     */
    @HEAD
    @Path("{testCaseId}")
    Response hasTest(@PathParam("testCaseId") String testCaseId);

    /**
     * Returns Test Case by ID.<br>
     * If Test Case doesn't exist then Response with status code 404 is returned.
     *
     * @param testCaseId Test Case ID string.
     * @param version    Version number.
     * @param view       JSON View Type.
     * @return TestCase in JSON format
     */
    @GET
    @Path("{testCaseId}")
    Response getTestCase(@PathParam("testCaseId") String testCaseId,
                         @QueryParam("version") String version,
                         @QueryParam("view") String view);

    /**
     * Returns list with Test Cases.
     * <br>
     * There are two ways to use this method:
     * <ol>
     * <li>Search by <code>id</code> query parameter with a list of Test Case IDs.</li>
     * <li>Search by query string using <code>q</code> query parameter. Returned results are paginated.</li>
     * </ol>
     *
     * @param id        Comma-separated list of Test Case IDs.
     * @param q         Search query string.<br>
     *                  Example format: {@literal requirement~1&type=Functional}<br>
     *                  '~' - contains predicate, '=' - equals predicate, '!=' - not equals predicate.
     * @param page      Page number.
     * @param perPage   Number of Test Cases per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     * @param view      JSON View Type.
     * @return List with Test Cases
     */
    @GET
    Response getTestCases(@QueryParam("id") String id,
                          @QueryParam("q") String q,
                          @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                          @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                          @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                          @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode,
                          @QueryParam("view") String view);

    /**
     * Checks if Test Cases with passed IDs exist and returns those that do.
     *
     * @param testCaseIds Comma-separated list of Test Case IDs.
     * @return List with Test Case IDs
     */
    @GET
    @Path("ids")
    Response getTestCaseIds(@QueryParam("id") @NotEmpty String testCaseIds);

    /**
     * Returns list with Test Case IDs containing search string in their Test Case ID.<br>
     * Results are limited with limit parameter with a default value 20.
     *
     * @param productId The ID of the Product that the Test Campaign belongs to.
     * @param featureIds List of Feature IDs of the Feature that the Test Campaign belongs to.
     * @param components List of Technical Component IDs that the Test Campaign relates to.
     * @param search Search string containing Test Case ID or part of it.
     * @param limit  Results limitation param.
     * @return List with Test Case IDs
     */
    @GET
    @Path("completion")
    Response getCompletion(@QueryParam("product") @NotNull Long productId,
                           @QueryParam("feature") @NotNull List<Long> featureIds,
                           @QueryParam("component") List<Long> components,
                           @QueryParam("search") @NotNull String search,
                           @QueryParam("limit") @Min(1) @DefaultValue("20") int limit);


    /**
     * Request to create new Test Case.<br>
     * New Test Case should have unique Test Case ID.
     *
     * @param testCaseInfo Test Case Info object.
     * @return Response with request status.
     */
    @POST
    Response create(@Valid
                    @ValidTestCaseComponent
                    @UniqueTestCaseId
                    @CorrectlyFormattedTestCaseId TestCaseInfo testCaseInfo);

    /**
     * Request to update Test Case by ID.<br>
     * Id in request body should match ID in Test Case Info object.
     *
     * @param id           Existing Test Case ID.
     * @param testCaseInfo Test Case Info object with same ID.
     * @return Response with request status and updated test case information.
     */
    @PUT
    @Path("{id}")
    Response update(@PathParam("id") Long id,
                    @Valid
                    @ValidTestCaseComponent
                    @HasId
                    @UniqueTestCaseId
                    @CorrectlyFormattedTestCaseId
                    @Editable TestCaseInfo testCaseInfo);

    /**
     * Request to review Test Case by ID.<br>
     * Id in request body should match ID in Test Case Info object.
     *
     * @param id           Existing Test Case Version ID.
     * @param status       query parameter give the status that needs to be updated i.e. Approved, Rejected
     * @param type         query parameter to express the type of review i.e. major, minor
     * @param reviewGroupId id of the review group to be attached (must have either review group or review user)
     * @param reviewUserId userId of the reviewer to be attached (must have either review group or review user)
     *
     * @return Response with request status and updated test case information.
     */
    @PUT
    @Path("{id}/review")
    Response review(@PathParam("id") Long id, @QueryParam("status") String status, @QueryParam("type") String type,
                    @QueryParam("reviewGroup") long reviewGroupId, @QueryParam("reviewUser") String reviewUserId);

    /**
     * Request to delete Test Case by ID.<br>
     * Id in request body should exist and Test Case shouldn't be deleted.
     *
     * @param testCaseId Existing non deleted Test Case ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response delete(@Authorized @PathParam("id") Long testCaseId);

    @Path("{id}/comments")
    PostResource comments(@PathParam("id") Long testCaseId);

    /**
     * Export Test Cases to format for re-import (excel).
     *
     * @param query String with Query.<br>
     *              Query example: {@literal referenceId=priority&referenceId=context
     *              &referenceId=group&referenceId=type}
     * @return Report file.
     */
    @GET
    @Path("export")
    Response getReport(@QueryParam("q") String query);

    /**
     * Import Test Cases from .xslx file.
     *
     * @param formDataMultiPart .xlsx files to be read
     * @return Response.
     */
    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response updateTestCasesFromReport(@XLSXFileType FormDataMultiPart formDataMultiPart);

    @POST
    @Path("/subscription")
    Response subscribe(TestCaseSubscriptionInfo subscriptionInfo);

    @DELETE
    @Path("/subscription")
    Response unsubscribe(TestCaseSubscriptionInfo subscriptionInfo);

    @GET
    @Path("/subscription")
    Response isSubscribed(@QueryParam("testCaseId") String testCaseId, @QueryParam("userId") String userId);
}
