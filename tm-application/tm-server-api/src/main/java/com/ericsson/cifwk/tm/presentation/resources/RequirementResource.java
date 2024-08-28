package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.validation.ExistingProject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
@Path("requirements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RequirementResource {

    /**
     * Request Requirement for Tree View by Search.
     *
     * @param requirementId Requirement ID.
     * @param view          (Optional) JSON View Type simple, detailed, tree, reverseTree.
     * @param projectId     (Optional) add filter based on project
     * @return Requirement with specified ID.
     */
    @GET
    @Path("{requirementId}")
    Response getRequirement(@PathParam("requirementId") @NotNull String requirementId,
                            @QueryParam("projectId") @ExistingProject String projectId,
                            @QueryParam("view") String view);

    /**
     * Get Requirements for Tree View, optionally filtering by project.
     *
     * @param projectId Existing project ID.
     * @param view      JSON View Type.
     * @return Requirement list (or tree) for the specified project.
     */
    @GET
    Response getRequirements(@QueryParam("projectId") @NotNull @ExistingProject String projectId,
                             @QueryParam("view") String view);

    /**
     * Request list of requirements matching specified search criteria.
     *
     * @param search Contains Requirement ID predicate.
     * @param type String value of Requirement type to filter. i.e. Story, Epic, MR
     * @param limit  Size of list to return.
     * @return List with requirements matching specified criteria.
     */
    @GET
    @Path("completion")
    CompletionInfo getCompletion(@QueryParam("search") @NotNull String search,
                                 @QueryParam("type") List<String> type,
                                 @QueryParam("limit") @Min(1) @DefaultValue("20") int limit);

}
