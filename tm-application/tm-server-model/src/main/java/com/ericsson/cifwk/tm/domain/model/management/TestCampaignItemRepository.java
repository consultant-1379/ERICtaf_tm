/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.management;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.management.impl.TestCampaignItemRepositoryImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(TestCampaignItemRepositoryImpl.class)
public interface TestCampaignItemRepository extends BaseRepository<TestCampaignItem, Long> {

    TestCampaignItem findByTestPlanAndTestCase(long testPlanId, String testCaseId);

}
