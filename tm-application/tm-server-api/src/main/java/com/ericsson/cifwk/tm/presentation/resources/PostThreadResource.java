package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.PostInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("post_threads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PostThreadResource {
    /**
     * Request all screen(=objectName) comments for entity(=objectId) and for each fields of screen (screen = object).
     *
     * @param objectId The object id
     * @param objectKey The objects key
     *
     * @return Array with PostThreads objects for each commented area.
     */
    @GET
    Response getPostThreads(@QueryParam("objectId") Long objectId, @QueryParam("objectKey") String objectKey);


    /**
     * Request comments for entity+screen+field(=objectId+objectKey+fieldKey).
     *
     * @param objectId The object id
     * @param objectKey The objects key
     * @param fieldKey The field key
     *
     * @return Array with PostThreads objects for each commented area.
     */
    @GET
    @Path("findcreate")
    Response findOrCreatePostThread(
            @QueryParam("objectId") Long objectId,
            @QueryParam("objectKey") String objectKey,
            @QueryParam("field") String fieldKey);

    /**
     * Create post (comment).
     *
     * @param objectId     test case id or test plan id etc
     * @param postThreadId thread id
     * @param postInfo     new comment
     * @return Response with request status.
     */
    @POST
    @Path("{objectId}/thread/{threadId}/posts")
    Response createPost(
            @PathParam("objectId") Long objectId,
            @PathParam("threadId") Long postThreadId,
            PostInfo postInfo);

    /**
     * Request to mark as deleted
     *
     * @param postId id of deleted comment
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    Response deletePost(@PathParam("id") Long postId);


}
