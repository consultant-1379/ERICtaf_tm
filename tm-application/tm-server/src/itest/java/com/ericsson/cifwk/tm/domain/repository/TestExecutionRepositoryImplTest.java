/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.googlecode.genericdao.search.Search;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TestExecutionRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Test
    public void testDAO() {
        Product product = fixture().persistProduct();
        ProductFeature feature = fixture().persistFeature("feature", product);
        TestType testType = fixture().persistTestType("testType", product);
        TechnicalComponent comp = fixture().persistTechnicalComponent("comp", feature);
        TestCase testCase = fixture().persistTestCase("id123", comp, feature, testType);
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature(feature);
        TestExecution testExecution = fixture().constructTestExecution(testCampaign, testCaseVersion);
        Project project = fixture().persistProject("idProject", "testProject", product);
        Defect defect = fixture().persistDefect(project);

        persistence.persistInTransaction(testCampaign, project, testExecution);
        testExecution.addDefect(defect);
        persistence.persistInTransaction(testExecution);

        Search search = new Search();
        search.addFilterEqual("testCaseVersion.testCase.testCaseId", testCase.getTestCaseId());
        TestExecution execution = testExecutionRepository.searchUnique(search);

        assertThat(execution.getDefects().size(), equalTo(1));
        assertThat(testExecution.getTestCaseVersion().getTestCase().getTestCaseId(),
                equalTo(testCase.getTestCaseId()));
    }

}
