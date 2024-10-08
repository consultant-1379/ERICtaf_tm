package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("")
public interface RootResource {

    /**
     * Request for API info.
     *
     * @return Response with API version.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response info();

}
