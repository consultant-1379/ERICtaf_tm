package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;
import com.ericsson.cifwk.tm.presentation.validation.Authorized;

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
@Path("projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProjectResource {

    /**
     * Request all projects.
     * @param q         Search query string. Example format: name=deployment<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return Array with project objects.
     */
    @GET
    Response getProjects(@QueryParam("q") String q,
                         @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                         @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                         @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                         @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Request project by specified ID.<br>
     * If Project doesn't exist then Response with status code 404 (Not Found) is returned.
     *
     * @param projectId Existing project ID.
     * @return Project object.
     */
    @GET
    @Path("{projectId}")
    Response getProject(@PathParam("projectId") String projectId);


    /**
     * Request to create Project.<br>
     * Specified Project Id should be unique, otherwise project won't be created.
     * Specifying a Product Id will make the request join it to that product. otherwise it will be set to default value
     * If Project with such ID exists, checks to see if product was deleted.
     * If Project with specified ID exists and is deleted, it is re-created.
     * If Project with specified ID exists and is not deleted, exception is thrown.
     *
     * @param projectInfo Project Info with unique project ID.
     * @return Created Project Info.
     */
    @POST
    Response createProject(@Authorized @Valid ProjectInfo projectInfo);

    /**
     * Request to update Project info.<br>
     *
     * @param projectId Project ID
     * @param projectInfo Project Info with unique project ID.
     * @return Updated Entity
     */
    @PUT
    @Path("{id}")
    Response updateProject(@PathParam("id") Long projectId, @Authorized @Valid ProjectInfo projectInfo);

    /**
     * Request to delete a Project.<br>
     *
     * @param projectId Project ID with unique project ID.
     * @return Reponse.
     */

    @DELETE
    @Path("{projectId}")
    Response deleteProject(@Authorized @PathParam("projectId") Long projectId);

}
