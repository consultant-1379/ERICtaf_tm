package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("test-cases.{ext}")
public interface TestCaseReportingResource {

    /**
     * Export Test Cases in file of specified format.
     *
     * @param query         String with Query.<br>
     *                      Query example: {@literal referenceId=priority&referenceId=context
     *                          &referenceId=group&referenceId=type}
     * @param ext           Report file format. Examples: docx, html, pdf.
     * @param view          Specify view ie. view=detailed.
     * @param reportType    Specifies what report type to generate i.e TestExecution or TestCase
     * @return Report file.
     */
    @GET
    Response getTest(@QueryParam("q") String query, @PathParam("ext") String ext,
                     @QueryParam("view") String view,
                     @QueryParam("reportType") String reportType);

    /**
     * Export Test Campaigns in file of specified format.
     *
     * @param testCampaignGroupId    Specifies the id value of selected Test Campaign Group
     * @return void.
     */
    @GET
    @Path("{id}")
    Response getTestCampaignGroupId(@PathParam("id") long testCampaignGroupId);


    @GET
    @Path("{id}/{testCampaignId}")
    Response getTestCampaignSubGroupId(@PathParam("id") long testCampaignGroupId, @PathParam("testCampaignId") String testCampaignSubGroupId);
    @GET

    @Path("{campaignId}/{id}/{testCampaignId}")
    Response getSovReport(@PathParam("campaignId") long campaignId,@PathParam("id") long id,@PathParam("testCampaignId") String testCampaignId);


    @GET
    @Path("{testCampaignGroupId}/{campaignId}/{id}/{testCampaignId}")
    Response getSovSubReport(@PathParam("testCampaignGroupId") long testCampaignGroupId, @PathParam("campaignId") long campaignId, @PathParam("id") long id, @PathParam("testCampaignId") String testCampaignId);
}

