package com.ericsson.cifwk.tm.presentation.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 */
@Path("references")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReferenceResource {

    /**
     * Request references by reference ID.<br>
     * Example query = {@literal references?referenceId=priority&referenceId=context}
     *
     * @param referenceIds List of reference ID.
     * @return Arrays of objects with Reference Id and reference data items.
     */
    @GET
    Response getReferences(@QueryParam("referenceId") List<String> referenceIds);

}
