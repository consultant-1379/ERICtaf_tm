/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestCaseVersionControllerTest extends BaseControllerLevelTest {

    public static final String TEST_CASES_URL = "/tm-server/api/test-cases/";

    @Test
    public void testGetTestCaseVersion() throws Exception {
        Requirement requirement = createRequirement();
        ProductFeature productFeature = fixture().persistProductFeature();
        TestType testType = fixture().persistTestType("Functional", productFeature.getProduct());

        TestCase testCase = createTestCase(requirement, productFeature, testType);

        Response response = app.client()
                .path(TEST_CASES_URL + testCase.getId())
                .queryParam("version", 0.1)
                .request().get();
        assertStatus(response, Response.Status.OK);

        TestCaseInfo info = response.readEntity(TestCaseInfo.class);
        assertThat(info.getId(), equalTo(testCase.getId()));
        assertThat(0L, equalTo(testCase.getCurrentVersion().getMajorVersion()));
        assertThat(1L, equalTo(testCase.getCurrentVersion().getMinorVersion()));
    }

    private Requirement createRequirement() {
        Requirement requirement = buildRequirement().build();
        app.persistence().persistInTransaction(requirement);
        return requirement;
    }

    private TestCase createTestCase(Requirement requirement, ProductFeature feature, TestType testType) {
        TestCase testCase = buildTestCase()
                .addRequirement(requirement)
                .withProductFeature(feature)
                .withType(testType)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();
        app.persistence().persistInTransaction(testCase);
        return testCase;
    }
}
