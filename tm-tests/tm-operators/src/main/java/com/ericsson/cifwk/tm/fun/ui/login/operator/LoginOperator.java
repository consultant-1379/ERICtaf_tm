/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.login.operator;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.tm.fun.ui.login.results.AuthenticationResult;

public interface LoginOperator {

    void start(Host host);

    AuthenticationResult loginWithUser(User user);

    AuthenticationResult loginWithDefaultUser();

    AuthenticationResult login();

    AuthenticationResult loginAsAdmin();

    AuthenticationResult logout();

}
