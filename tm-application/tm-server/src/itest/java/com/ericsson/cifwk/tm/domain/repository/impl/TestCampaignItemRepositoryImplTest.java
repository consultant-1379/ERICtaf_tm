/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.repository.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class TestCampaignItemRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    TestCampaignItemRepository testCampaignItemRepository;

    private TestCase testCase;
    private TestCampaign testCampaign;
    private TestCampaignItem testCampaignItem;

    @Before
    public void setUp() {
        testCase = fixture().persistTestCase("TC1");
        testCampaign = fixture().persistTestCampaignWithFeature("ENM", "PM");

        testCampaignItem = new TestCampaignItem();
        testCampaignItem.setTestCaseVersion(testCase.getCurrentVersion());
        testCampaignItem.setTestCampaign(testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem);

        persistence.persistInTransaction(testCampaignItem, testCase, testCampaign);
    }

    @Test
    public void testFindByTestCaseAndTestPlan() {
        TestCampaignItem foundEntity =
                testCampaignItemRepository.findByTestPlanAndTestCase(testCampaign.getId(), Long.toString(testCase.getId()));
        assertThat(foundEntity, notNullValue());
        assertThat(foundEntity.getId(), is(testCampaignItem.getId()));
    }
}
