/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.params.TestCampaignCriteria;
import com.ericsson.cifwk.tm.application.services.impl.TestCampaignServiceImpl;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Map;

@ImplementedBy(TestCampaignServiceImpl.class)
public interface TestCampaignService {

    List<TestCampaign> findAll(String projectId);

    List<TestCampaign> findAllOpen(String search);

    Paginated<TestCampaign> customSearch(Query query, int page, int perPage);

    Paginated<TestCampaign> customSearch(TestCampaignCriteria criteria, int page, int perPage);

    List<TestCampaign> copyTestCampaigns(CopyTestCampaignsRequest request) throws ServiceValidationException;

    TestCampaign populate(TestCampaign entity, TestCampaignInfo input) throws ServiceValidationException;

    Map<String, List<String>>  getUsersToNotify(TestCampaign entity, TestCampaignInfo input);

    void notifyAboutAssignment(Map<String, List<String>> usersToNotify, Long testPlanId, String hostname);

}
