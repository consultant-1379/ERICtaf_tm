package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.validation.FileName;
import com.ericsson.cifwk.tm.presentation.validation.FileSize10MB;
import com.ericsson.cifwk.tm.presentation.validation.FileType;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.validation.constraints.NotNull;
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

@Path("files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FileResource {

    @GET
    @Path("{product}/{file-category}/{id}")
    Response getFileMetaData(@PathParam("product") String product,
                             @PathParam("file-category") String category,
                             @PathParam("id") Long entityId);

    @GET
    @Path("{product}/{file-category}/{id}/{filename}")
    Response getFile(@PathParam("product") String product,
                     @PathParam("file-category") String category,
                     @PathParam("id") Long entityId,
                     @PathParam("filename") String filename);

    @DELETE
    @Path("{product}/{file-category}/{id}")
    @Consumes("text/plain")
    Response deleteFile(@PathParam("product") String product,
                        @PathParam("file-category") String category,
                        @PathParam("id") Long entityId,
                        @NotNull @QueryParam("filenames") String filenames);

    @POST
    @Path("{product}/{file-category}/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    Response saveFiles(@PathParam("product") String product,
                       @PathParam("file-category") String category,
                       @PathParam("id") Long entityId,
                       @FileName @FileType @FileSize10MB FormDataMultiPart formDataMultiPart);
}
