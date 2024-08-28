package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileDataRepository;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Repository
public class FileDataRepositoryImpl extends BaseRepositoryImpl<FileData, Long> implements FileDataRepository {

    public static final String FILE_NAME = "filename";
    public static final String ENTITY_ID = "entityId";
    public static final String FILE_CATEGORY = "fileCategory";

    @Override
    public FileData findByFileNameAndTestCaseId(String filename, Long testCaseId) {
        Search search = new Search(FileData.class);
        search.addFilterEqual(FILE_NAME, filename);
        search.addFilterEqual(ENTITY_ID, testCaseId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<FileData> findByTestExecutionIdAndFileCategory(Long testExecutionId, FileCategory fileCategory){
        Search search = new Search(FileData.class);
        search.addFilterEqual(ENTITY_ID, testExecutionId);
        search.addFilterEqual(FILE_CATEGORY, fileCategory);
        try {
            return this.search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
