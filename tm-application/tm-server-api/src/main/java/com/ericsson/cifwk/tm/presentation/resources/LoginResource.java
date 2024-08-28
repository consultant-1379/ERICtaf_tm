package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.UserCredentials;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface LoginResource {

    /**
     * Request authentication status.
     *
     * @return Response with Authentication status.
     */
    @GET
    Response status();

    /**
     * Login request.
     *
     * @param userCredentials User Credentials with username and password.
     * @return Response with request status.
     */
    @POST
    Response login(@Valid UserCredentials userCredentials);

    /**
     * Logout request.
     *
     * @return Response with Authentication status.
     */
    @DELETE
    Response logout();

}
