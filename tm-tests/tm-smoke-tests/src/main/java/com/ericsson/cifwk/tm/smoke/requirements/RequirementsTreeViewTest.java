package com.ericsson.cifwk.tm.smoke.requirements;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.smoke.common.CustomAsserts.checkTestStep;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class RequirementsTreeViewTest extends TafTestBase{

    private Host host;

    @Inject
    RestTestManagementOperator tmOperator;

    @Inject
    RestLoginOperator loginOperator;

    @BeforeClass(alwaysRun = true)
    public final void setUp() {
        host = DataHandler.getHostByName("tm");
        loginOperator.start(host);
        tmOperator.start(host);
    }

    @Test
    @TestId(id="DURACI-2968_Smoke_1", title="Show Test Cases by Requirement")
    public void showTestCasesByRequirement() {

        TestCasesResult result = tmOperator.getTestCasesByRequirement("Cip-4960");
        checkTestStep(result);

        assertNull(result.getErrorMessage());
        assertNotNull(result.getTestCases());

    }
}
