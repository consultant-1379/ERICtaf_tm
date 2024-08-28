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

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.net.HttpHeaders;

import java.io.InputStream;
import java.util.UUID;

@Operator()
public class TestCaseFixturesOperatorImpl extends RestLoginCommonOperator implements TestCaseFixturesOperator {

    public static final String TEST_CASES_URL = "/tm-server/api/test-cases";

    @Override
    @TestStep(id = "start", description = "Start Test case fixtures operator on host {0}")
    public void start(Host host) {
        super.start(host);
    }

    @Override
    @TestStep(id = "createTestCase", description = "Creates Test Case with JSON InputStream")
    public CreateEditTestCaseResult createTestCase(InputStream testCaseJson, String description) {
        String requestUrl = TEST_CASES_URL;
        TestCaseInfo testCaseToCreate = setTestCaseId(testCaseJson, UUID.randomUUID().toString(), description);
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .body(JsonHelper.fromTestCaseInfo(testCaseToCreate))
                .contentType(ContentType.APPLICATION_JSON)
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return CreateEditTestCaseResult.error(e.getMessage());
        }

        TestCaseInfo testCaseInfo = json.fromJson(response.getBody(), TestCaseInfo.class);
        return CreateEditTestCaseResult.success(testCaseInfo);
    }

    private TestCaseInfo setTestCaseId(InputStream json, String testCaseId, String uniqueDescription) {
        TestCaseInfo info = JsonHelper.toTestCaseInfo(json);
        if (info.getTestCaseId() != null) {
            info.setTestCaseId(testCaseId);
        }
        if (uniqueDescription != null) {
            info.setDescription(uniqueDescription);
        }
        return info;
    }

}
