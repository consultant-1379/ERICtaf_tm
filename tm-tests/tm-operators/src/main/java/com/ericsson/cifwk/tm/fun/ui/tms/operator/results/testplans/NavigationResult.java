package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

public class NavigationResult implements HasResult {

    private final boolean success;
    private final String errorMessage;

    private NavigationResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static NavigationResult success() {
        return new NavigationResult(true, null);
    }

    public static NavigationResult error(String errorMessage) {
        return new NavigationResult(false, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
