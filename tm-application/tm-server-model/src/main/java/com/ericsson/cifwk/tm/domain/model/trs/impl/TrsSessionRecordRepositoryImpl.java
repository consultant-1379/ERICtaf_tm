package com.ericsson.cifwk.tm.domain.model.trs.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecordRepository;
import com.googlecode.genericdao.search.Search;

import java.util.Optional;


public class TrsSessionRecordRepositoryImpl extends BaseRepositoryImpl<TrsSessionRecord, Long> implements TrsSessionRecordRepository {

    @Override
    public Optional<TrsSessionRecord> findSessionByJobAndIso(String jobId, Long isoId) {
        Search search = new Search(TrsSessionRecord.class);
        search.addFilterEqual("job.jobId", jobId);
        search.addFilterEqual("iso.id", isoId);
        TrsSessionRecord trsSessionRecord = this.searchUnique(search);
        return Optional.ofNullable(trsSessionRecord);
    }
}
