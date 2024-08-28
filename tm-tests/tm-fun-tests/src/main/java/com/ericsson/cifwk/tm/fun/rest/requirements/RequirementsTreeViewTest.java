package com.ericsson.cifwk.tm.fun.rest.requirements;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class RequirementsTreeViewTest extends BaseFuncTest {

    @Test
    @TestId(id="DURACI-2968_Func_1", title="Show Test Cases by Requirement")
    public void showTestCasesByRequirement() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        TestCasesResult result = tmRestOperator.getTestCasesByRequirement("Cip-4960");
        CustomAsserts.checkTestStep(result);

        assertNull(result.getErrorMessage());
        assertNotNull(result.getTestCases());
        assertFalse(result.getTestCases().isEmpty());
        assertThat(result.getTestCases(), hasSize(8));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-2968_Func_2", title="Show a Requirement without Test Cases")
    public void showByRequirementWithoutTestCases() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        TestCasesResult result = tmRestOperator.getTestCasesByRequirement("CIP-4183");
        CustomAsserts.checkTestStep(result);
        assertNotNull(result.getTestCases());
        assertTrue(result.getTestCases().isEmpty());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-2968_Func_3", title="Filter Requirements Tree by nonexistent requirement")
    public void tryToSelectNotListedRequirement() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        TestCasesResult result = tmRestOperator.getTestCasesByRequirement("CIP-NotFound");
        CustomAsserts.checkTestStepFailed(result);
        assertNull(result.getTestCases());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }
}
