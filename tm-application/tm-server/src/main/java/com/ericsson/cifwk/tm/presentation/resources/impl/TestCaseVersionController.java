/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateTestCaseVersionCommand;
import com.ericsson.cifwk.tm.application.queries.TestCaseVersionQuerySet;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.resources.TestCaseVersionResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class TestCaseVersionController implements TestCaseVersionResource {

    @Inject
    private TestCaseVersionQuerySet testCaseVersionQuerySet;

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTestCaseVersionCommand createTestCaseVersionCommand;

    @Override
    public Response getTestCaseVersions(String testCaseId) {
        return testCaseVersionQuerySet.getVersions(testCaseId, TestCaseView.Simple.class);
    }

    @Override
    public Response newVersion(String testCaseId) {
        return commandProcessor.process(createTestCaseVersionCommand, testCaseId);
    }

}
