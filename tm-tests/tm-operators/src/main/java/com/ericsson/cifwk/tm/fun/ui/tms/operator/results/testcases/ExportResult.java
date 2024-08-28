/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

public class ExportResult implements HasResult {

    private final boolean success;
    private final String errorMessage;

    private ExportResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static ExportResult success() {
        return new ExportResult(true, null);
    }

    public static ExportResult error(String errorMessage) {
        return new ExportResult(false, errorMessage);
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
