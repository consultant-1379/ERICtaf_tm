package com.ericsson.cifwk.tm.presentation.resources;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("jira")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JiraDefectResource {

    /**
     * Request JIRA MetaData for a sepecific project.
     *
     * @param projectId the project to get meta data for e.g. (TORF)
     *
     * @return Response with meta data from JIRA based on JIRA project configuration
     */
    @GET
    @Path("defect-metadata")
    Response getDefectMetadata(@QueryParam("projectId") String projectId);

    /**
     * Request label search based on query parameter.
     *
     * @param query the query passed to search for labels
     *
     * @return Response list of labels from JIRA.
     */
    @GET
    @Path("labels")
    Response getLabels(@QueryParam("query") String query);

    /**
     * Request to create a JIRA BUG.
     *
     * @param defectInfo the defect information json for creation
     *
     * @return Response with Jira issue created.
     */
    @POST
    @Path("create-defect")
    Response createDefect(Object defectInfo);

    /**
     * Request to attach a file to Jira bug.
     *
     * @param issueId the JIRA issue id
     * @param multiPart the file to attach
     *
     * @return Response with Authentication status.
     */
    @POST
    @Path("attachments/{issueId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response uploadAttachments(@PathParam("issueId") String issueId, FormDataMultiPart multiPart);

}
