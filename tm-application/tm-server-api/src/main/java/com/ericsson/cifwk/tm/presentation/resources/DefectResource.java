/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;

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

@Path("defects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DefectResource {

    @GET
    @Path("completion")
    CompletionInfo getCompletion(@QueryParam("search") @NotNull String search,
                                 @QueryParam("limit") @Min(1) @DefaultValue("20") int limit);
    /**
     * Request defect information for a given id.
     *
     * @param defectId The defect id from requirments management tool e.g. (TORF-1234)
     * @param view The json view to be returned i.e. simple, detailed
     *
     * @return defect information retrieved from server
     */
    @GET
    @Path("{defectId}")
    Response getDefects(@PathParam("defectId") @NotNull String defectId,
                        @QueryParam("view") String view);

}
