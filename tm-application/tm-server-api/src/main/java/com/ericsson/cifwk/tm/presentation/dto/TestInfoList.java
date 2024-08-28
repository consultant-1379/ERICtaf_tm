package com.ericsson.cifwk.tm.presentation.dto;

import java.util.List;

/**
 *
 */
public class TestInfoList {

    private List<SimpleTestCaseInfo> testCases;

    public List<SimpleTestCaseInfo> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<SimpleTestCaseInfo> testCases) {
        this.testCases = testCases;
    }

}
