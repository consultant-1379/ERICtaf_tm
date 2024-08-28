package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.trs.impl.TrsResultRecordRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Optional;

@ImplementedBy(TrsResultRecordRepositoryImpl.class)
public interface TrsResultRecordRepository extends BaseRepository<TrsResultRecord, Long> {

    List<TrsResultRecord> findResultsBySession(String sessionId);

    Optional<TrsResultRecord> findResultBySessionAndTestCase(String sessionId, Long testCaseId);

}
