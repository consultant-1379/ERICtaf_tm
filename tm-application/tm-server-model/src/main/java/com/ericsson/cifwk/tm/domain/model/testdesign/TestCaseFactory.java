package com.ericsson.cifwk.tm.domain.model.testdesign;

/**
 *
 */
public final class TestCaseFactory {

    private TestCaseFactory() {
    }

    public static TestCase createTestCase() {
        TestCase testCase = new TestCase();
        testCase.addNewMinorVersion();
        return testCase;
    }

}
