package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestSuiteRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Optional;


@ImplementedBy(TestSuiteRepositoryImpl.class)
public interface TestSuiteRepository extends BaseRepository<TestSuite, Long> {
    Optional<TestSuite> findByNameAndFeatureId(String name, Long featureId);
}
