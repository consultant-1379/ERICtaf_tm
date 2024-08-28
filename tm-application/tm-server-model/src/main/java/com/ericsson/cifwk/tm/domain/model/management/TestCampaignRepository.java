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

import com.ericsson.cifwk.tm.domain.model.management.impl.TestCampaignRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Optional;

@ImplementedBy(TestCampaignRepositoryImpl.class)
public interface TestCampaignRepository extends BaseRepository<TestCampaign, Long> {

    List<TestCampaign> findByTestCaseVersionId(long testCaseVersionId);

    List<TestCampaign> findByDrop(long dropId);

    List<TestCampaign> findByName(String name);

    Optional<List<TestCampaign>> findByNameAndDrop(String name, Long dropId);
}
