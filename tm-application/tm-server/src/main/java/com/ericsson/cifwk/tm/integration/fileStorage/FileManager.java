package com.ericsson.cifwk.tm.integration.fileStorage;

import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.integration.fileStorage.impl.FileManagerImpl;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.google.inject.ImplementedBy;

import java.io.File;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;

@ImplementedBy(FileManagerImpl.class)
public interface FileManager {

    File search(String product, FileCategory fileCategory, Long entityId, String filename);

    List<FileMetaDataInfo> getFileMetaData(String product, FileCategory fileCategory, Long entityId);

    String getExension(File file);

    String getName(File file);

    File save(String product, FileCategory fileCategory, Long entityId, String filename, InputStream inputStream);

    boolean delete(String product, FileCategory fileCategory, Long entityId, String filename);

    Map<String, MappedByteBuffer> findFileBufferForEntity(String product, FileCategory fileCategory, Long entityId);

}
