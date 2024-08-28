package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

import java.util.List;

public class CopyTestStepResult implements HasResult {

    private boolean success;

    private String errorMessage;

    private Integer testStepsBeforeCopy;

    private Integer testStepsAfterCopy;

    private List<String> testStepTitles;

    private List<String> copyTitles;

    private List<Integer> copyIndices;

    private CopyTestStepResult(boolean success, Integer testStepsBeforeCopy,
                               Integer testStepsAfterCopy, List<String> testStepTitles,
                               List<String> copyTitles, List<Integer> copyIndices) {

        this.success = success;
        this.testStepsBeforeCopy = testStepsBeforeCopy;
        this.testStepsAfterCopy = testStepsAfterCopy;
        this.testStepTitles = testStepTitles;
        this.copyTitles = copyTitles;
        this.copyIndices = copyIndices;
    }

    private CopyTestStepResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static CopyTestStepResult success(Integer testStepsBeforeCopy, Integer testStepsAfterCopy,
                                             List<String> testStepTitles, List<String> copyTitles,
                                             List<Integer> copyIndices) {

        return new CopyTestStepResult(true, testStepsBeforeCopy, testStepsAfterCopy, testStepTitles, copyTitles, copyIndices);
    }

    public static CopyTestStepResult error(String errorMessage) {
        return new CopyTestStepResult(false, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getTestStepsBeforeCopy() {
        return testStepsBeforeCopy;
    }

    public Integer getTestStepsAfterCopy() {
        return testStepsAfterCopy;
    }

    public List<String> getTestStepTitles() {
        return testStepTitles;
    }

    public List<String> getCopyTitles() {
        return copyTitles;
    }

    public List<Integer> getCopyIndices() {
        return copyIndices;
    }
}
