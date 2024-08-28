package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.VerifyStepRepositoryImpl;
import com.google.inject.ImplementedBy;


@ImplementedBy(VerifyStepRepositoryImpl.class)
public interface VerifyStepRepository extends BaseRepository<VerifyStep, Long> {

}
