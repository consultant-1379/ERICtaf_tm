package com.ericsson.cifwk.tm.integration.fileStorage.impl;

import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.infrastructure.Environment;
import com.ericsson.cifwk.tm.integration.fileStorage.FileManager;
import com.ericsson.cifwk.tm.integration.fileStorage.StorageConfiguration;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileManagerImpl implements FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagerImpl.class);

    private String env;

    private String baseDir;

    private String testCasesDir;

    private String testExecutionsDir;

    @Inject
    public FileManagerImpl(StorageConfiguration configuration) {
        env = new Environment().getModuleType();
        baseDir = configuration.getStorageDirectory();
        testCasesDir = configuration.getTestCases();
        testExecutionsDir = configuration.getTestExecutions();
    }

    @Override
    public File search(String product, FileCategory fileCategory, Long entityId, String filename) {
        String path = generatePath(product, fileCategory, entityId);
        File file = new File(path);
        List<File> files = Lists.newArrayList(FileUtils.listFiles(file, null, false));

        return files.stream()
                .filter(f -> filename.equals(f.getName()))
                .findFirst()
                .get();
    }

    private List<File> findAll(String product, FileCategory fileCategory, Long entityId) {
        String path = generatePath(product, fileCategory, entityId);
        File file = new File(path);
        return Lists.newArrayList(FileUtils.listFiles(file, null, false));
    }

    @Override
    public List<FileMetaDataInfo> getFileMetaData(String product, FileCategory fileCategory, Long entityId) {
        String path = generatePath(product, fileCategory, entityId);

        File file = new File(path);
        if (!file.exists()) {
            return Lists.newArrayList();
        }
        List<File> files = findAll(product, fileCategory, entityId);

        List<FileMetaDataInfo> dtos = files.stream().map(f -> {
                FileMetaDataInfo fileData = new FileMetaDataInfo();
                fileData.setFileName(f.getName());
                fileData.setType(getExension(f));
                fileData.setUrl(f.getAbsolutePath());
                return fileData;
            }).collect(Collectors.toList());

        return dtos;
    }

    @Override
    public String getExension(File file) {
        return FilenameUtils.getExtension(file.getName());
    }

    @Override
    public String getName(File file) {
        return FilenameUtils.getBaseName(file.getName());
    }

    @Override
    public File save(String product, FileCategory fileCategory, Long entityId, String filename, InputStream inputStream) {
        String path = generatePath(product, fileCategory, entityId) + "/" + filename;
        File file = new File(path);
        file.getParentFile().mkdirs();
        try {
            OutputStream out = new FileOutputStream(file);
            IOUtils.copy(inputStream, out);
            IOUtils.closeQuietly(inputStream);
            out.close();
        } catch (IOException e) {
            LOGGER.error("Problem writing file to file system", e);
        }

        return file;
    }

    @Override
    public boolean delete(String product, FileCategory fileCategory, Long entityId, String filename) {
        String path = generatePath(product, fileCategory, entityId) + "/" + filename;
        File file = new File(path);
        return file.delete();
    }

    @Override
    public Map<String, MappedByteBuffer> findFileBufferForEntity(String product, FileCategory fileCategory, Long entityId) {
        String path = generatePath(product, fileCategory, entityId);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<File> allFiles = findAll(product, fileCategory, entityId);

        Map<String, MappedByteBuffer> fileBuffers = new HashMap();

        allFiles.forEach(f -> {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(f, "r");
                    FileChannel fileChannel = randomAccessFile.getChannel();
                    MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                    fileBuffers.put(f.getName(), buffer);
                    randomAccessFile.close();
                } catch (IOException e) {
                    LOGGER.error("Problem getting file buffers for entity with id " + entityId, e);
                }
            });

        return fileBuffers;
    }

    public String generatePath(String product, FileCategory fileCategory, Long entityId) {
        String dir = getDirectory(fileCategory);
        return baseDir + env + MessageFormat.format("/{0}/{1}/{2}", product, dir, Long.toString(entityId));
    }

    private String getDirectory(FileCategory fileCategory) {
        if (fileCategory.equals(FileCategory.TEST_CASE_FILE)) {
            return testCasesDir;
        } else {
            return testExecutionsDir;
        }
    }

}
