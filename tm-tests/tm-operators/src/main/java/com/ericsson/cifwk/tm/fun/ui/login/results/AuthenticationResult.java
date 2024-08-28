/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.login.results;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

public final class AuthenticationResult implements HasResult {

    private final boolean success;
    private final boolean authenticated;
    private final String errorMessage;
    private final String username;

    private AuthenticationResult(boolean success, boolean authenticated, String errorMessage, String username) {
        this.success = success;
        this.authenticated = authenticated;
        this.errorMessage = errorMessage;
        this.username = username;
    }

    public static AuthenticationResult success(boolean authenticated, String username) {
        return new AuthenticationResult(true, authenticated, null, username);
    }

    public static AuthenticationResult error(String errorMessage, String username) {
        return new AuthenticationResult(false, false, errorMessage, username);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUsername() {
        return username;
    }
}
