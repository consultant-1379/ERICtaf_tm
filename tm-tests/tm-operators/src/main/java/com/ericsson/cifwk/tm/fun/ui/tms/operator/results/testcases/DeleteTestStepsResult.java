package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

public class DeleteTestStepsResult implements HasResult {

    private boolean success;

    private String errorMessage;

    private Integer testStepsBeforeDelete;

    private Integer testStepsAfterDelete;

    private DeleteTestStepsResult(boolean success, String errorMessage,
                                  Integer testStepsBeforeDelete, Integer testStepsAfterDelete) {

        this.success = success;
        this.errorMessage = errorMessage;
        this.testStepsBeforeDelete = testStepsBeforeDelete;
        this.testStepsAfterDelete = testStepsAfterDelete;
    }

    public static DeleteTestStepsResult success(Integer testStepsBeforeDelete, Integer testStepsAfterDelete) {
        return new DeleteTestStepsResult(true, null,
                testStepsBeforeDelete,
                testStepsAfterDelete);
    }

    public static DeleteTestStepsResult error(String errorMessage) {
        return new DeleteTestStepsResult(false, errorMessage, null, null);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getTestStepsBeforeDelete() {
        return testStepsBeforeDelete;
    }

    public Integer getTestStepsAfterDelete() {
        return testStepsAfterDelete;
    }
}
