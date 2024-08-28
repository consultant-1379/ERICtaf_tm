package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.responses.Pagination;
import com.ericsson.cifwk.tm.presentation.responses.Sorting;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UserProfileResource {

    /**
     * Returns User Profile for specified user ID.
     * If no User Profile exist with specified user ID then new User Profile will be created.
     *
     * @param userId String with User ID.
     * @return User Profile for specified user ID.
     */
    @GET
    @Path("{userId}")
    Response getUserProfile(@NotNull @PathParam("userId") String userId);

    /**
     * Request User Profiles.<br>
     * Returns User Profiles based on query.
     *
     * @param q         Search query string.<br>
     * @param page      Page number.
     * @param perPage   Number of items per page.
     * @param orderBy   Field name for sorting.
     * @param orderMode Sorting type.
     *
     * @return User Profiles data.
     */
    @GET
    Response getUserProfile(@QueryParam("q") String q,
                       @DefaultValue("1") @QueryParam(Pagination.PAGE_PARAM) @Min(1) int page,
                       @DefaultValue("20") @QueryParam(Pagination.PER_PAGE_PARAM) int perPage,
                       @QueryParam(Sorting.ORDER_BY_PARAM) String orderBy,
                       @QueryParam(Sorting.ORDER_MODE_PARAM) String orderMode);

    /**
     * Requests to update User Profile for specified user ID.
     * If no User Profile exist with specified user ID then new User Profile will be created
     * and updated with specified data.
     *
     * @param userId      String with User ID.
     * @param userProfile User Profile with updated data.
     * @return Response with request status.
     */
    @PUT
    @Path("{userId}")
    Response updateUserProfile(@NotNull @PathParam("userId") String userId,
                               @Valid UserProfileInfo userProfile);

    /**
     * Requests to delete a saved searches for specified profile ID.
     *
     * @param id            String saved search Id.
     * @param userId        String user Id.
     * @return Response with request status.
     */
    @DELETE
    @Path("saved-search/{userId}/{id}")
    Response deleteSavedSearch(@NotNull @PathParam("userId") String userId,
                               @NotNull @PathParam("id") Long id);

    /**
     * Returns list with User IDs containing search string in their User ID.<br>
     * Results are limited with limit parameter with default value 20.
     *
     * @param search Search string containing User ID or part of it.
     * @param limit  Results limitation param.
     * @return List with User IDs
     */
    @GET
    @Path("completion")
    Response getCompletion(@QueryParam("search") @NotNull String search,
                           @QueryParam("limit") @Min(1) @DefaultValue("20") int limit);

    @PUT
    @Path("{userId}/administrator/{admin}")
    Response updatePermissions(@NotNull @PathParam("userId") String userId, @PathParam("admin") boolean admin);

}
