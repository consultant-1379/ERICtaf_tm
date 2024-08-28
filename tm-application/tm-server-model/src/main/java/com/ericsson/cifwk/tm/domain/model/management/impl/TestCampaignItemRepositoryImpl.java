/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.management.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;

@Repository
public class TestCampaignItemRepositoryImpl
        extends BaseRepositoryImpl<TestCampaignItem, Long>
        implements TestCampaignItemRepository {

    @Override
    public TestCampaignItem findByTestPlanAndTestCase(long testPlanId, String testCaseId) {
        Search search = new Search(TestCampaignItem.class);
        search.addFilterEqual("testCampaign.id", testPlanId);
        filterByAnyTestCaseId(testCaseId, search);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    private void filterByAnyTestCaseId(String testCaseId, Search search) {
        try {
            long id = Long.parseLong(testCaseId);
            search.addFilterEqual("testCaseVersion.testCase.id", id);
        } catch (NumberFormatException e) {
            search.addFilterEqual("testCaseVersion.testCase.testCaseId", testCaseId);
        }
    }
}
