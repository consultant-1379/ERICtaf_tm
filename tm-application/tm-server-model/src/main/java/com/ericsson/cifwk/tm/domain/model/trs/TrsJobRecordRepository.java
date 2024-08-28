package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.trs.impl.TrsJobRecordRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Optional;

@ImplementedBy(TrsJobRecordRepositoryImpl.class)
public interface TrsJobRecordRepository extends BaseRepository<TrsJobRecord, Long> {

    Optional<TrsJobRecord> findJobByName(String name);
}
