/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.security;

/**
 * Class is responsible for user authentication ONLY.
 */
public interface AuthenticationService {

    String login(String username, String credentials, boolean rememberMe);

    String login(String sessionId);

    boolean isLoggedIn();

    void logout();

}
