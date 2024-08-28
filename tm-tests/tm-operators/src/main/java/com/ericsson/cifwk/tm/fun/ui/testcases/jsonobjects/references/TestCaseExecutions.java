package com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import java.util.List;

public class TestCaseExecutions {
    private String testCaseId;
    private List<TestExecutionInfo> testExecutions = Lists.newArrayList();

    public TestCaseExecutions (String id, List<TestExecutionInfo> testExecutionInfos) {
        this.testCaseId = id;
        this.testExecutions = testExecutionInfos;
    }
    public List<TestExecutionInfo> getTestExecutions() {
        return testExecutions;
    }

    public String getTestCaseId() {
        return testCaseId;
    }
}
