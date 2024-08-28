package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestTeamRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Optional;


@ImplementedBy(TestTeamRepositoryImpl.class)
public interface TestTeamRepository extends BaseRepository<TestTeam, Long> {
    Optional<TestTeam> findByNameAndFeatureId(String name, Long featureId);
}
