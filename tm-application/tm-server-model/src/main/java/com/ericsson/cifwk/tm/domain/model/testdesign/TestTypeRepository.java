package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestTypeRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Optional;


@ImplementedBy(TestTypeRepositoryImpl.class)
public interface TestTypeRepository extends BaseRepository<TestType, Long> {
    Optional<TestType> findByNameAndProductId (String name, Long product_id);
}
