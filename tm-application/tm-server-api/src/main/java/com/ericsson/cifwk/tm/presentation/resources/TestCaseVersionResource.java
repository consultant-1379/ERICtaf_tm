/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("test-cases/{testCaseId}/versions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TestCaseVersionResource {


    /**
     * Returns Test Case version history from newest to oldest.
     *
     * @param testCaseId Test Case ID string.
     * @return List of Test Case versions.
     */
    @GET
    Response getTestCaseVersions(@PathParam("testCaseId") String testCaseId);

    /**
     * Creates new minor version of a Test Case from provided object by ID.
     *
     * @param testCaseId Test Case ID string
     * @return Response with request status.
     */
    @POST
    Response newVersion(@PathParam("testCaseId") String testCaseId);

}
