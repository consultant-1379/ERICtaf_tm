package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.domain.model.domain.impl.DropRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Optional;

@ImplementedBy(DropRepositoryImpl.class)
public interface DropRepository extends BaseRepository<Drop, Long> {
    List<Drop> findByProduct(Long productId);
    Drop findByProductIDAndDropName(Long productId, String name);
}
