package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;

import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User assignment HTTP resource of test plan items
 */
@Path("assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AssignmentResource {

    /**
     * Returns list with user and items of test plans.
     *
     * @param userId    User id.
     * @param q         Search query string.<br>
     *                  Example format: {@literal name~Test&environment=Test}<br>
     *                  '~' - contains predicate, '=' - equals predicate, '!=' - not equals predicate.
     * @param page      Page number.
     * @param perPage   Number of Test Cases per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     * @param view The json view to be returned i.e. simple, detailed
     * @return List with Test Cases
     * @see com.ericsson.cifwk.tm.presentation.dto.view.DtoView
     */
    @GET
    Response getAssignments(@QueryParam("userId") String userId,
                            @QueryParam("q") String q,
                            @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                            @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                            @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                            @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode,
                            @QueryParam("view") String view);

}
