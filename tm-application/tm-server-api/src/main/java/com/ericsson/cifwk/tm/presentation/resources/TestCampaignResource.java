package com.ericsson.cifwk.tm.presentation.resources;


import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestInfoList;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.ContextsMatch;
import com.ericsson.cifwk.tm.presentation.validation.HasDefectWhenFailed;
import com.ericsson.cifwk.tm.presentation.validation.HasDefectWhenFailedMultiple;
import com.ericsson.cifwk.tm.presentation.validation.HasId;
import com.ericsson.cifwk.tm.presentation.validation.HasIsoIfRequired;
import com.ericsson.cifwk.tm.presentation.validation.HaveIsosIfRequired;
import com.ericsson.cifwk.tm.presentation.validation.TestCampaignNotAlreadyInDrop;
import com.ericsson.cifwk.tm.presentation.validation.ValidCopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.validation.ValidFeatureAndComponents;

import javax.validation.Valid;
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
@Path("test-campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestCampaignResource {

    /**
     * Requests Test Campaign for specified test Campaign ID.<br>
     * If Test Campaign is deleted or not exists, Response 404 (Not found) is returned.
     *
     * @param testCampaignId String with Test Campaign ID.
     * @param view           JSON View Type.
     * @return Test Campaign with specified ID and fields corresponding to view type.
     * @see com.ericsson.cifwk.tm.presentation.dto.view.DtoView
     */
    @GET
    @Path("{testCampaignId}")
    Response getTestPlan(@PathParam("testCampaignId") long testCampaignId,
                         @QueryParam("view") String view);

    /**
     * @param q          Search query string.<br>
     * @param productId  Product that the Test campaign relates to.
     * @param dropId     Drop that the Test Campaign relates to (optional)
     * @param featureId  Product feature that the Test Campaign relates to (optional)
     * @param components Components that the Test Campaign relates to (optional)
     * @param page       Page number.
     * @param perPage    Number of Test Campaigns per page.
     * @param orderBy    Field name for sorting.
     * @param orderMode  Sorting type.
     * @param view       JSON View Type.
     * @return List of Test Campaigns.
     * @see com.ericsson.cifwk.tm.presentation.dto.view.DtoView
     */
    @GET
    Response getTestCampaigns(@QueryParam("q") String q,
                              @QueryParam("product") Long productId,
                              @QueryParam("drop") Long dropId,
                              @QueryParam("feature") List<Long> featureId,
                              @QueryParam(value = "component") List<Long> components,
                              @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                              @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                              @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                              @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode,
                              @QueryParam("view") String view);

    /**
     * Get Test Campaigns for completion. Search is done by specified project ID.<br>
     * Locked Test Campaigns are excluded.<br>
     * If project ID is not specified, then all test campaigns are returned.
     *
     * @param search String to query.
     * @param limit  Limit the number of values displayed default 50.
     * @return Completion Info with item count and items array.
     */
    @GET
    @Path("completion")
    Response getCompletion(@QueryParam("search") String search,
                           @DefaultValue("50") @QueryParam("limit") int limit);

    /**
     * Request to create Test Campaign.<br>
     * Specified Test Campaign Id should be unique, otherwise test Campaign won't be created.
     *
     * @param testCampaignInfo Test Campaign Info object to create.
     * @return Created Test Campaign Info object.
     */
    @POST
    Response create(@Valid @ValidFeatureAndComponents
                    @TestCampaignNotAlreadyInDrop @ContextsMatch TestCampaignInfo testCampaignInfo);

    /**
     * Request to update Test Campaign by ID.<br>
     * Id in request body should match ID in Test Campaign Info object.
     *
     * @param testCampaignId   Existing Test Campaign ID.
     * @param testCampaignInfo Test Campaign Info object with updated data.
     * @return Response with request status.
     */
    @PUT
    @Path("{testCampaignId}")
    Response update(@PathParam("testCampaignId") long testCampaignId,
                    @HasId @Valid @ValidFeatureAndComponents
                    @TestCampaignNotAlreadyInDrop @ContextsMatch TestCampaignInfo testCampaignInfo);

    /**
     * Request to delete Test Campaign by ID.<br>
     * If Test Campaign with such ID is deleted or not exists - Response 404 (Not Found) is returned.
     *
     * @param testCampaignId Existing non deleted Test Campaign ID.
     * @return Response with request status.
     */
    @DELETE
    @Path("{testCampaignId}")
    Response delete(@PathParam("testCampaignId") long testCampaignId);

    @POST
    @Path("copy")
    Response copyTestCampaigns(@Valid @ValidCopyTestCampaignsRequest CopyTestCampaignsRequest request);

    /**
     * Returns a list of Test Cases in a Test Campaign with latest Test Execution result.
     *
     * @param testCampaignId Existing Test Campaign ID.
     * @param q              Query String.
     * @return List of Test Cases with latest Test Execution result.
     */
    @GET
    @Path("{testCampaignId}/test-cases")
    Response getTestCases(@PathParam("testCampaignId") long testCampaignId,
                          @QueryParam("q") String q);

    /**
     * Request to attach test cases to Test Campaign.
     *
     * @param testCampaignId Existing Test Campaign ID.
     * @param testInfoList   Test Case Info objects list.
     * @return Response with request status.
     */
    @POST
    @Path("{testCampaignId}/test-cases")
    Response attachTestCases(@PathParam("testCampaignId") long testCampaignId,
                             @NotNull TestInfoList testInfoList);

    /**
     * Updates Test Case version to latest in a Test Campaign.
     *
     * @param testCampaignId Updated Test Campaign ID.
     * @param testCaseId     Test Case ID to update.
     * @return Response
     */
    @PUT
    @Path("{testCampaignId}/test-cases/{testCaseId}")
    Response updateTestCaseVersion(@PathParam("testCampaignId") long testCampaignId,
                                   @PathParam("testCaseId") long testCaseId);

    /**
     * Returns Test Case version in a Test Campaign.
     *
     * @param testCampaignId    Test Campaign ID.
     * @param testCaseId        Test Case ID.
     * @param view              The view to be returned i.e. simple, detailed
     *
     * @return Response
     */
    @GET
    @Path("{testCampaignId}/test-cases/{testCaseId}")
    Response getTestCaseVersion(@PathParam("testCampaignId") long testCampaignId,
                                @PathParam("testCaseId") String testCaseId,
                                @QueryParam("view") String view);

    /**
     * Request to lock or unlock test Campaign.<br>
     * If Test Campaign was not modified during Request then Response 200 is returned.<br>
     * If Test Campaign with specified ID exist then Response 404 (Not Found) is returned.
     *
     * @param testCampaignId    The test  campaign id.
     * @param testCampaignInfo  Existing Test Campaign Info with 'locked' param set.
     * @return Response with request status.
     */
    @PUT
    @Path("{testCampaignId}/status")
    Response lockTestPlan(@PathParam("testCampaignId") long testCampaignId,
                          @NotNull TestCampaignInfo testCampaignInfo);

    /**
     * Returns a list of Test Executions for a Test Case in a Test Campaign.
     *
     * @param testCampaignId Existing Test Campaign ID.
     * @param testCaseId     Existing Test Case ID.
     * @param page           Page number.
     * @param perPage        Number of Test Campaigns per page.
     * @param view           The view to be returned i.e. simple, detailed
     * @return List of Test Executions.
     */
    @GET
    @Path("{testCampaignId}/test-cases/{testCaseId}/executions")
    Response getTestExecutions(@PathParam("testCampaignId") long testCampaignId,
                               @PathParam("testCaseId") String testCaseId,
                               @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                               @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                               @QueryParam("view") String view);

    /**
     * Returns a list of Test Cases Executions.
     *
     * @param testCampaignId    Existing Test Campaign ID.
     * @param testExecutionInfo list of Test Executions
     * @return List of Test Cases.
     */
    @POST
    @Path("{testCampaignId}/test-cases/executions")
    Response createMultipleTestExecutions(@PathParam("testCampaignId") long testCampaignId,
                                          @Valid @HaveIsosIfRequired @HasDefectWhenFailedMultiple
                                                  List<TestExecutionInfo> testExecutionInfo);

    /**
     * Returns a list of the latest Test Executions for each test case.
     *
     * @param testCampaignId Existing Test Campaign ID.
     * @param view           The view to be returned i.e. simple, detailed
     * @return List of Test Cases.
     */
    @GET
    @Path("{testCampaignId}/test-cases/executions")
    Response getLatestTestExecutions(@PathParam("testCampaignId") long testCampaignId,
                                     @QueryParam("view") String view);

    /**
     * Request to record new Test Execution for a Test Case in a Test Campaign.
     *
     * @param testCampaignId    Existing Test Campaign ID.
     * @param testCaseId        Existing Test Case ID.
     * @param testExecutionInfo Test Execution object to create.
     * @return Created Test Execution object.
     */
    @POST
    @Path("{testCampaignId}/test-cases/{testCaseId}/executions")
    Response createTestExecution(@PathParam("testCampaignId") long testCampaignId,
                                 @PathParam("testCaseId") String testCaseId,
                                 @Valid @HasIsoIfRequired @HasDefectWhenFailed TestExecutionInfo testExecutionInfo);


    /**
     * Returns a list of Test Cases in a Test Campaign in CSV format.
     *
     * @param testCampaignId Existing Test Campaign ID.
     * @param filter         Filters the test cases to be returned.
     * @return List of Test Cases with latest Test Execution result.
     */
    @GET
    @Path("{testCampaignId}/test-cases/csv")
    @Produces("text/csv")
    Response getTestCaseCSV(@PathParam("testCampaignId") long testCampaignId,
                            @QueryParam("filter") String filter);



    @GET
    @Path("{testCampaignId}/test-campaigns.{ext}")
    Response getTestCaseGAT(@PathParam("testCampaignId") long testCampaignId,
                     @QueryParam("q") String query, @PathParam("ext") String ext,
                     @QueryParam("view") String view,
                     @QueryParam("reportType") String reportType);


}
