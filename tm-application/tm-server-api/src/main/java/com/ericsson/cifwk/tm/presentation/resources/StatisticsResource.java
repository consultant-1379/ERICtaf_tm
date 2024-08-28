package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface StatisticsResource {

    /**
     * Request User statistics data.<br>
     * Returns team reference data if such exists for specified ID.
     *
     * @return User data.
     */
    @GET
    @Path("/users")
    Response getUsers();

    /**
     * Request No. Test Cases statistics data.<br>
     * Returns data needs to chart metrics.
     *
     * @return No. Test Cases.
     */
    @GET
    @Path("/test-cases")
    Response getTestCases();

}
