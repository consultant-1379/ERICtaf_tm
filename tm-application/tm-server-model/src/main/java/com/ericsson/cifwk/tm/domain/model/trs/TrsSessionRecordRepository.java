package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.trs.impl.TrsSessionRecordRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Optional;

@ImplementedBy(TrsSessionRecordRepositoryImpl.class)
public interface TrsSessionRecordRepository extends BaseRepository<TrsSessionRecord, Long> {

    Optional<TrsSessionRecord> findSessionByJobAndIso(String jobId, Long isoId);

}
