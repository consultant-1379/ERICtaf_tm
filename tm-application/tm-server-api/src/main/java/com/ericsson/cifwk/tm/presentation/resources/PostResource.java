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

public interface PostResource {
    /**
     * Request for comments.
     *
     * @param view The json view to be returned i.e. simple, detailed
     *
     * @return Comments stats and array with Posts (Comments) for test case with id = 'objectId' if 'view' = detail,
     * else: comments stats only
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response getPosts(@QueryParam("view") String view);

    /**
     * Create post (comment).
     *
     * @param postInfo The object used to create the post
     *
     * @return Response with request status.
     */

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response createPost(PostInfo postInfo);

    /**
     * Request to mark as deleted
     *
     * @param postId id of deleted comment
     * @return Response with request status.
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Response deletePost(@PathParam("id") Long postId);

}
