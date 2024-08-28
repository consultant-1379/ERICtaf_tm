package com.ericsson.cifwk.tm.integration.fileStorage;

import com.google.common.base.Preconditions;
import com.netflix.governator.annotations.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by egergle on 20/07/2015.
 */
public class StorageConfiguration {

    @Configuration("storage.directory")
    private String storageDirectory;

    @Configuration("storage.testCases")
    private String testCases;

    @Configuration("storage.testExecutions")
    private String testExecutions;


    @PostConstruct
    public void validate() {
        Preconditions.checkNotNull(storageDirectory);
        Preconditions.checkNotNull(testCases);
        Preconditions.checkNotNull(testExecutions);
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public String getTestCases() {
        return testCases;
    }

    public String getTestExecutions() {
        return testExecutions;
    }
}
