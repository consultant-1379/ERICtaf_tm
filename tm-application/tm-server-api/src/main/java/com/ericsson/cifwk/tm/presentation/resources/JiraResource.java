package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("jira")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JiraResource {

    /**
     * Trigger sync of Requirements (User Stories/ Epics) from JIRA based on jql query.
     *
     * @param jql JIRA Query Language.
     * @param password used to allow access for this operation.
     * @return Response
     */
    @POST
    @Path("requirements")
    Response getRequirements(@QueryParam("jql") String jql, @QueryParam("password") String password);

    /**
     * Trigger sync of Defects from JIRA based on jql query.
     *
     * @param jql Existing project ID.
     * @param password used to allow access for this operation.
     * @return Response
     */
    @POST
    @Path("defects")
    Response getDefects(@QueryParam("jql") String jql, @QueryParam("password") String password);

}
