package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.integration.fileStorage.FileService;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.resources.FileResource;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class FileController implements FileResource {

    @Inject
    private FileService fileService;

    @Override
    public Response getFileMetaData(String product, String category, Long entityId) {
        return fileService.getFileMetaData(product, category, entityId);
    }

    @Override
    public Response getFile(String product, String category, Long entityId, String filename) {
        return fileService.getFile(product, category, entityId, filename);
    }

    @Override
    public Response deleteFile(String product, String category, Long entityId, String fileNames) {
        return fileService.delete(product, category, entityId, fileNames);
    }

    @Override
    public Response saveFiles(String product, String category, Long entityId,
                              FormDataMultiPart formDataMultiPart) {

        return fileService.save(product, category, entityId, formDataMultiPart);
    }
}
