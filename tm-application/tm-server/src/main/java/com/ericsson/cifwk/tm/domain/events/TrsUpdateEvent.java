/*
 * COPYRIGHT Ericsson (c) 2017.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.domain.events;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;

import java.util.List;

public class TrsUpdateEvent {
    private final TestCampaign testCampaign;

    private final List<TestExecution> testExecution;

    public TrsUpdateEvent(TestCampaign testCampaign, List<TestExecution> testExecution) {
        this.testCampaign = testCampaign;
        this.testExecution = testExecution;
    }

    public TestCampaign getTestCampaign() {
        return testCampaign;
    }

    public List<TestExecution> getTestExecutions() {
        return testExecution;
    }
}
