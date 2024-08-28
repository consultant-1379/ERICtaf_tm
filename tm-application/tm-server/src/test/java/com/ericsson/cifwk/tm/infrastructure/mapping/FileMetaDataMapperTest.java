package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.ericsson.cifwk.tm.presentation.dto.FileDataInfo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by egergle on 13/08/2015.
 */
public class FileMetaDataMapperTest {

    private FileMetaDataMapper fileMetaDataMapper;

    private FileData fileData;

    private FileDataInfo fileDataInfo;

    @Before
    public void setUp() {
        fileMetaDataMapper = new FileMetaDataMapper();
        fileDataInfo = new FileDataInfo();

        fileData = new FileData();
        fileData.setId(1l);
        fileData.setFilename("test.txt");
        fileData.setAuthor("tafuser");
        fileData.setLocation("/usr/test");

        fileDataInfo.setEntityId(1l);
        fileDataInfo.setAuthor("tafuser");
        fileDataInfo.setFileName("test.txt");
        fileDataInfo.setUrl("/usr/test");
        fileDataInfo.setFileCategory("test-cases");

    }

    @Test
    public void testEmptyMapEntity() throws Exception {
        FileMetaDataInfo fileInfo = fileMetaDataMapper.mapEntity(null, FileMetaDataInfo.class);
        assertNull(fileInfo);
    }

    @Test
    public void testMapFileDataEntity() throws Exception {
        FileMetaDataInfo fileInfo = fileMetaDataMapper.mapEntity(fileData, FileMetaDataInfo.class);
        assertThat(fileInfo.getFileName(), equalTo("test.txt"));
        assertThat(fileInfo.getAuthor(), equalTo("tafuser"));
        assertThat(fileInfo.getUrl(), equalTo("/usr/test"));
        assertThat(fileInfo.getId(), equalTo(1l));
    }

    @Test
    public void testMapDtoForFileData() throws Exception {
        FileData mapped = fileMetaDataMapper.mapDto(fileDataInfo, fileData);
        assertThat(mapped.getFilename(), equalTo("test.txt"));
        assertThat(mapped.getFileCategory(), equalTo(FileCategory.TEST_CASE_FILE));
        assertThat(mapped.getAuthor(), equalTo("tafuser"));
        assertThat(mapped.getLocation(), equalTo("/usr/test"));
        assertThat(mapped.getEntityId(), equalTo(1l));
        assertNotNull(mapped.getCreated());

    }

    @Test
    public void testMapDtoOfNull() throws Exception {
        FileData fileDataInfo = fileMetaDataMapper.mapDto(null, fileData);
        assertNull(fileDataInfo);
    }
}