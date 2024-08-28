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


import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

public class SearchResult implements HasResult {

    private final Paginated<TestCaseInfo> testCases;
    private final boolean success;
    private final String errorMessage;

    private SearchResult(boolean success, Paginated<TestCaseInfo> testCases, String errorMessage) {
        this.success = success;
        this.testCases = testCases;
        this.errorMessage = errorMessage;
    }

    public static SearchResult success(Paginated<TestCaseInfo> testCases) {
        return new SearchResult(true, testCases, null);
    }

    public static SearchResult error(Paginated<TestCaseInfo> testCases, String errorMessage) {
        return new SearchResult(false, testCases, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public Paginated<TestCaseInfo> getTestCases() {
        return testCases;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
