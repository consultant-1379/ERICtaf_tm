package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/version")
@Produces(MediaType.TEXT_PLAIN)
public interface VersionResource {


    /**
     * Requests application version
     *
     * @return String with current application version
     */

    @GET
    Response getVersion();

}
