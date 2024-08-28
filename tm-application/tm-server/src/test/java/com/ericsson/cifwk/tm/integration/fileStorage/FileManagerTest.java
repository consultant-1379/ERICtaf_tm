package com.ericsson.cifwk.tm.integration.fileStorage;


import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.integration.fileStorage.impl.FileManagerImpl;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by egergle on 13/08/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileManagerTest {

    @Mock
    private StorageConfiguration storageConfiguration;

    private FileManagerImpl fileManager;

    private static final String DIR = ".\\target\\tmp\\tms\\";

    @Before
    public void setUp() {
        when(storageConfiguration.getStorageDirectory()).thenReturn(DIR);
        when(storageConfiguration.getTestCases()).thenReturn("\\test_cases\\");
        fileManager = new FileManagerImpl(storageConfiguration);
    }

    @After
    public void tearDown() throws IOException {
        File dir = new File(DIR);
        FileUtils.deleteDirectory(dir);
    }

    @Test
    public void testGeneratePath() {
        String path = fileManager.generatePath("ENM", FileCategory.TEST_CASE_FILE, 13000L);
        assertFalse(path.contains(","));
    }

    @Test
    public void testFindFilesForEntity() throws Exception {
        String product = "ENM";
        Long entityId = 1L;
        List<String> filenames = Lists.newArrayList("test.txt", "test2.txt");

        InputStream input = new ByteArrayInputStream(new String("1234567890").getBytes());

        fileManager.save(product, FileCategory.TEST_CASE_FILE, entityId, filenames.get(0), input);
        fileManager.save(product, FileCategory.TEST_CASE_FILE, entityId, filenames.get(1), input);

        List<FileMetaDataInfo> retrievedFiles = fileManager.getFileMetaData(product, FileCategory.TEST_CASE_FILE, entityId);

        retrievedFiles.forEach(f -> assertTrue(filenames.contains(f.getFileName())));
    }

    @Test
    public void testDeleteFileForTestCase() throws Exception {
        String product = "ENM";
        Long entityId = 1L;
        String filename = "test.txt";

        InputStream input = new ByteArrayInputStream(new String("1234567890").getBytes());
        fileManager.save(product, FileCategory.TEST_CASE_FILE, entityId, filename, input);

        assertTrue(fileManager.delete(product, FileCategory.TEST_CASE_FILE, entityId, filename));

    }

    @Test
    public void testSaveFileForTestCase() throws Exception {
        String product = "ENM";
        Long entityId = 1L;
        String filename = "test.txt";

        InputStream input = new ByteArrayInputStream(new String("1234567890").getBytes());
        File savedFile = fileManager.save(product, FileCategory.TEST_CASE_FILE, entityId, filename, input);
        File retrievedFile = fileManager.search(product, FileCategory.TEST_CASE_FILE, entityId, filename);

        assertThat(retrievedFile.getName(), equalTo(savedFile.getName()));
    }

    @Test
    public void testGetExension() throws Exception {
        String filename = "test.txt";
        File file = new File(filename);
        assertThat(fileManager.getExension(file), equalTo("txt"));
    }

    @Test
    public void testGetName() throws Exception {
        String filename = "test.txt";
        File file = new File(filename);
        assertThat(fileManager.getName(file), equalTo("test"));
    }

}