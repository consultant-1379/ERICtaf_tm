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
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import java.util.List;
import java.util.Optional;

@Repository
public class TestCampaignRepositoryImpl extends BaseRepositoryImpl<TestCampaign, Long>
        implements TestCampaignRepository {

    @Override
    public List<TestCampaign> findByTestCaseVersionId(long testCaseVersionId) {
        Search search = searchByTestCaseVersionId(testCaseVersionId);
        return this.search(search);
    }

    @Override
    public List<TestCampaign> findByDrop(long dropId) {
        Search search = new Search(TestCampaign.class);
        search.addFilterEqual("drop.id", dropId);
        return this.search(search);
    }

    @Override
    public List<TestCampaign> findByName(String name) {
        Search search = new Search(TestCampaign.class);
        search.addFilterEqual("name", name);
        return this.search(search);
    }

    @Override
    public Optional<List<TestCampaign>> findByNameAndDrop(String name, Long dropId) {
        Search search = new Search(TestCampaign.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("drop.id", dropId);
        List<TestCampaign> testCampaigns = this.search(search);
        return Optional.ofNullable(testCampaigns);
    }

    private Search searchByTestCaseVersionId(long testCaseVersionId) {
        Search search = new Search(TestCampaign.class);
        search.addFilterEqual("testCampaignItems.testCaseVersion.id", testCaseVersionId);
        return search;
    }
}
