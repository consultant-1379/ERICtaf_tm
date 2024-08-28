/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.results;

public final class Result<T> implements HasResult {

    private final boolean success;
    private final T value;
    private final String errorMessage;

    private Result(boolean success, T value, String errorMessage) {
        this.success = success;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(true, value, null);
    }

    public static <T> Result<T> error(String errorMessage) {
        return new Result<>(false, null, errorMessage);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

}
