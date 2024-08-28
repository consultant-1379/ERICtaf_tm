/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.perf.testcases;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.LoginOperator;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.ericsson.cifwk.tm.perf.common.CustomAsserts.checkTestStep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class AuthenticationTest extends TafTestBase {

    private Host host;

    @BeforeClass
    public void setUp() {
        host = DataHandler.getHostByName("tm_test");
    }

    @Test
    public void successLogin() {
        LoginOperator loginOperator = new RestLoginOperator();
        loginOperator.start(host);

        RestTestManagementOperator tmOperator = new RestTestManagementOperator();
        tmOperator.start(host);

        checkTestStep(loginOperator.login());

        TestCasesResult result = tmOperator.getTestCasesByRequirement("CIP-4960");

        checkTestStep(result);
        assertEquals("CIP-4960", result.getRequirementId());
        assertNull(result.getErrorMessage());

        checkTestStep(loginOperator.logout());
    }
}
