/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.FileDataRepositoryImpl;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(FileDataRepositoryImpl.class)
public interface FileDataRepository extends BaseRepository<FileData, Long> {

    FileData findByFileNameAndTestCaseId(String filename, Long testCaseId);

    List<FileData> findByTestExecutionIdAndFileCategory(Long testExecutionId, FileCategory fileCategory);
}
