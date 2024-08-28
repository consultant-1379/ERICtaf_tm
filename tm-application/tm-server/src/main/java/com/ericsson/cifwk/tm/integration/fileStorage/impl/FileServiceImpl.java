package com.ericsson.cifwk.tm.integration.fileStorage.impl;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileDataRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.infrastructure.mapping.FileMetaDataMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;
import com.ericsson.cifwk.tm.integration.fileStorage.FileManager;
import com.ericsson.cifwk.tm.integration.fileStorage.FileService;
import com.ericsson.cifwk.tm.integration.fileStorage.FileStreaming;
import com.ericsson.cifwk.tm.presentation.dto.FileDataInfo;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class FileServiceImpl implements FileService {

    @Inject
    private FileManager fileManager;

    @Inject
    private FileDataRepository fileDataRepository;

    @Inject
    private FileMetaDataMapper fileMetaDataMapper;

    @Inject
    private FileStreaming fileStreaming;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Override
    public Response getFileMetaData(String product, String category, Long entityId) {
        validateProduct(product);
        FileCategory fileCategory = FileCategory.get(category);
        validateEntity(fileCategory, entityId);

        List<FileMetaDataInfo> files = fileManager.getFileMetaData(product, fileCategory, entityId);
        if (files == null || files.isEmpty()) {
            return Responses.noContent();
        }

        files.forEach(f -> {
                FileData filedata = fileDataRepository.findByFileNameAndTestCaseId(f.getFileName(), entityId);
                fileMetaDataMapper.mapEntity(filedata, f);
            });

        return Responses.ok(files);
    }

    @Override
    public boolean getFilesAttached(Long testExecutionId, FileCategory fileCategory) {
        boolean areFilesAttached = false;
        Optional<List<FileData>> files = Optional.ofNullable(
                fileDataRepository.findByTestExecutionIdAndFileCategory(testExecutionId, fileCategory));
        if (files.isPresent() && !files.get().isEmpty()) {
            areFilesAttached = true;
        }
        return areFilesAttached;
    }

    @Override
    public Response getFile(String product, String category, Long entityId, String filename) {
        validateProduct(product);
        FileCategory fileCategory = FileCategory.get(category);
        validateEntity(fileCategory, entityId);
        File file = fileManager.search(product, fileCategory, entityId, filename);

        if (file == null) {
            return Responses.badRequest("File " + filename + " not found", "File does not exist");
        }

        return Responses.file(fileStreaming.stream(file), fileManager.getName(file), "." + fileManager.getExension(file));
    }

    @Override
    @Transactional
    public Response save(String product, String category, Long entityId, FormDataMultiPart formDataMultiPart) {
        validateProduct(product);
        FileCategory fileCategory = FileCategory.get(category);
        validateEntity(fileCategory, entityId);

        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");
        FormDataBodyPart author = formDataMultiPart.getField("author");

        List<FileData> files = Lists.newArrayList();

        parts.forEach(part -> {
                String filename = part.getContentDisposition().getFileName();
                InputStream inputStream = part.getValueAs(InputStream.class);
                File savedFile = fileManager.save(product, fileCategory, entityId, filename, inputStream);

                FileDataInfo input = getDto(product, category, entityId, filename, author.getValue(), savedFile);

                FileData fileData = fileMetaDataMapper.mapDto(input, FileData.class);
                FileData entity = fileDataRepository.findByFileNameAndTestCaseId(input.getFileName(), input.getEntityId());

                if (entity != null) {
                    fileData.setId(entity.getId());
                    fileDataRepository.save(fileData);
                } else {
                    fileDataRepository.persist(fileData);
                }
                files.add(fileData);
            });

        return Responses.created(files);
    }

    @Override
    @Transactional
    public Response delete(String product, String category, Long entityId, String filenames) {
        validateProduct(product);
        FileCategory fileCategory = FileCategory.get(category);
        validateEntity(fileCategory, entityId);

        Iterable<String> names = Sanitization.splitCommaSeparated(filenames);
        List<String> nameList = Lists.newArrayList(names);

        nameList.forEach(filename -> {
                boolean result = fileManager.delete(product, fileCategory, entityId, filename);

                if (!result) {
                    throw new BadRequestException(Responses.badRequest("File " + filename + " could not be deleted"));
                }
                FileData fileData = fileDataRepository.findByFileNameAndTestCaseId(filename, entityId);

                if (fileData != null) {
                    fileData.delete();
                    fileDataRepository.save(fileData);
                }
            });

        return Responses.noContent();
    }

    private void validateProduct(String productName) {
        Product product = productRepository.findByName(productName);
        if (product == null) {
            throw new BadRequestException(Responses.badRequest("Product " + productName + " not found"));
        }
    }

    private void validateEntity(FileCategory fileCategory, Long entityId) {
        if (fileCategory.equals(FileCategory.TEST_CASE_FILE)) {
            validateEntity(testCaseRepository, entityId);
        } else if (fileCategory.equals(FileCategory.TEST_EXECUTION_FILE)) {
            validateEntity(testExecutionRepository, entityId);
        }
    }

    private <T extends BaseRepository> void validateEntity(T repository, Long entityId) {
        if (repository.find(entityId) == null) {
            throw new BadRequestException(Responses.badRequest("Entity with id " + entityId + " not found"));
        }
    }

    private FileDataInfo getDto(String product, String category, Long entityId, String filename, String author, File file) {
        FileDataInfo fileDataInfo = new FileDataInfo(product, category, entityId, filename);
        fileDataInfo.setAuthor(author);
        fileDataInfo.setUrl(file.getAbsolutePath());
        return fileDataInfo;
    }

}
