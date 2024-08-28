package com.ericsson.cifwk.tm.integration.fileStorage;

import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.integration.fileStorage.impl.FileServiceImpl;
import com.google.inject.ImplementedBy;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.core.Response;

@ImplementedBy(FileServiceImpl.class)
public interface FileService {

    Response getFileMetaData(String product, String category, Long entityId);

    boolean getFilesAttached(Long testExecutionId, FileCategory fileCategory);

    Response getFile(String product, String category, Long entityId, String filename);

    Response save(String product, String category, Long entityId, FormDataMultiPart formDataMultiPart);

    Response delete(String product, String category, Long entityId, String filenames);
}
