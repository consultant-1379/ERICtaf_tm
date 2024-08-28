package com.ericsson.cifwk.tm.fun.common.testexecution;

import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.TestCaseExecutions;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestExecutionResultType;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by egergle on 04/01/2017.
 */
public class TestExecutionTestHelper {

    private TestExecutionTestHelper () {
        // empty
    }

    public static void assertPrepopulatedTestExecutionInfo(TestExecutionInfo expectedTestExecutionInfo, TestExecutionInfo actualTestExecutionInfo) {
        assertEquals(expectedTestExecutionInfo.getResult(), actualTestExecutionInfo.getResult());
        assertEquals(expectedTestExecutionInfo.getComment(), actualTestExecutionInfo.getComment());
        assertEquals(expectedTestExecutionInfo.getDefectIds().size(), actualTestExecutionInfo.getDefectIds().size());

        assertTestStepExecutions(expectedTestExecutionInfo.getTestStepExecutions(), actualTestExecutionInfo.getTestStepExecutions());
        assertVerifyStepExecutions(expectedTestExecutionInfo.getVerifyStepExecutions(), actualTestExecutionInfo.getVerifyStepExecutions());
    }

    public static void assertTestStepExecutions(List<TestStepExecutionInfo> expectedTestStepExecutions, List<TestStepExecutionInfo> actualTestStepExecutions) {
        assertEquals(expectedTestStepExecutions.size(), actualTestStepExecutions.size());

        for (final TestStepExecutionInfo expectedTestStepExecution : expectedTestStepExecutions) {
            Optional<TestStepExecutionInfo> testStepExecutionOptional = Iterables.tryFind(actualTestStepExecutions, new Predicate<TestStepExecutionInfo>() {
                @Override
                public boolean apply(TestStepExecutionInfo testStepExecution) {
                    return expectedTestStepExecution.getTestStep().equals(testStepExecution.getTestStep());
                }
            });
            assertTrue(testStepExecutionOptional.isPresent());
            assertEquals(expectedTestStepExecution.getResult(), testStepExecutionOptional.get().getResult());
        }
    }

    public static void assertVerifyStepExecutions(List<VerifyStepExecutionInfo> expectedVerifyStepExecutions, List<VerifyStepExecutionInfo> actualVerifyStepExecutions) {
        assertEquals(expectedVerifyStepExecutions.size(), actualVerifyStepExecutions.size());

        for (final VerifyStepExecutionInfo expectedVerifyStepExecution : expectedVerifyStepExecutions) {
            Optional<VerifyStepExecutionInfo> verifyStepExecutionOptional = Iterables.tryFind(actualVerifyStepExecutions, new Predicate<VerifyStepExecutionInfo>() {
                @Override
                public boolean apply(VerifyStepExecutionInfo verifyStepExecution) {
                    return expectedVerifyStepExecution.getTestStep().equals(verifyStepExecution.getTestStep())
                            && expectedVerifyStepExecution.getVerifyStep().equals(verifyStepExecution.getVerifyStep());
                }
            });
            assertTrue(verifyStepExecutionOptional.isPresent());
            assertEquals(expectedVerifyStepExecution.getActualResult(), verifyStepExecutionOptional.get().getActualResult());
        }
    }

    public static TestExecutionInfo createTestExecutionInfo(TestExecutionResultType result, String comment, Set<String> defectIds, Set<String> requirementIds, String executionTime) {
        TestExecutionInfo testExecutionInfo = new TestExecutionInfo();
        testExecutionInfo.setResult(TestExecutionResultType.getEnum(result.getTitle()));
        testExecutionInfo.setComment(comment);
        testExecutionInfo.getDefectIds().addAll(defectIds);
        testExecutionInfo.setExecutionTime(executionTime);
        testExecutionInfo.getRequirementIds().addAll(requirementIds);
        return testExecutionInfo;
    }

    public static void assertTestExecution(TestCampaignItemInfo actualAssignmentInfo, TestCaseExecutions testCaseExecutions) {
        List<TestExecutionInfo> testExecutions = testCaseExecutions.getTestExecutions();

        if (actualAssignmentInfo.getResult() == null) {
            assertEquals(true, testExecutions.isEmpty());
            return;
        } else if (testExecutions.isEmpty()) {
            assertEquals(TestExecutionResultType.NOT_STARTED.getTitle(), actualAssignmentInfo.getResult().getTitle());
            return;
        }

        TestExecutionInfo testExecutionInfo = testExecutions.get(0);
        assertNotNull(testExecutionInfo.getResult());
        assertEquals(testExecutionInfo.getResult().getTitle(), actualAssignmentInfo.getResult().getTitle());
    }
}
