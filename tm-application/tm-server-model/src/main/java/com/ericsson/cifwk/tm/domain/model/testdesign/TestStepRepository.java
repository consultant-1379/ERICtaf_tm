package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestStepRepositoryImpl;
import com.google.inject.ImplementedBy;


@ImplementedBy(TestStepRepositoryImpl.class)
public interface TestStepRepository extends BaseRepository<TestStep, Long> {

}
