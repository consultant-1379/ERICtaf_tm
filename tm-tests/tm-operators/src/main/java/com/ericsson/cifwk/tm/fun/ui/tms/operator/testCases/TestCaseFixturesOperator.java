/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.results.AuthenticationResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;

import java.io.InputStream;

public interface TestCaseFixturesOperator {

    void start(Host host);

    CreateEditTestCaseResult createTestCase(InputStream testCaseJson, String description);

    AuthenticationResult login();

    AuthenticationResult logout();

}
