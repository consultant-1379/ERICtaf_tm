package com.ericsson.cifwk.tm.domain.repository;

/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.google.inject.Inject;
import org.junit.Test;

import java.util.List;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestCampaignRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    TestCampaignRepository testCampaignRepository;

    @Test
    public void testBasic() {
        final Project project = fixture().persistProject("CIP", "CIP");
        final Product product = fixture().persistProduct("Product", "Product", project);
        final ProductFeature feature = fixture().persistFeature("Feature", product);

        final TestCampaign testCampaign = buildTestPlan()
                .withName("testPlanName")
                .withDescription("testPlanDescription")
                .withEnvironment("testPlanEnvironment")
                .withProject(project)
                .withFeature(feature)
                .build();

        final TestCampaignItem testCampaignItem1 = constructTestPlanItem(1);
        final TestCampaignItem testCampaignItem2 = constructTestPlanItem(2);

        testCampaign.addTestCampaignItem(testCampaignItem1);
        testCampaign.addTestCampaignItem(testCampaignItem2);
        persistence.inTransaction(new Runnable() {
            @Override
            public void run() {
                testCampaignRepository.save(testCampaign);
            }
        });

        List<TestCampaign> testCampaigns = testCampaignRepository.findAll();

        assertThat(testCampaigns.size(), equalTo(1));
        assertThat(testCampaigns.get(0).getName(), equalTo("testPlanName"));
        assertThat(testCampaigns.get(0).getDescription(), equalTo("testPlanDescription"));
        assertThat(testCampaigns.get(0).getEnvironment(), equalTo("testPlanEnvironment"));
        assertThat(testCampaigns.get(0).getProject(), equalTo(project));
        assertThat(testCampaigns.get(0).getTestCampaignItems(), containsInAnyOrder(testCampaignItem1, testCampaignItem2));
    }


    private TestCampaignItem constructTestPlanItem(int i) {
        return buildTestPlanItem()
                .withUser(fixture().persistUser("user" + i))
                .withTestCaseVersion(fixture().persistTestCase("testCase" + i).getCurrentVersion())
                .build();
    }
}
