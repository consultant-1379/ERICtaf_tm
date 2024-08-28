package com.ericsson.cifwk.tm.domain.model.trs.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecordRepository;
import com.googlecode.genericdao.search.Search;

import java.util.List;
import java.util.Optional;


public class TrsResultRecordRepositoryImpl extends BaseRepositoryImpl<TrsResultRecord, Long> implements TrsResultRecordRepository {

    @Override
    public List<TrsResultRecord> findResultsBySession(String sessionId) {
        Search search = new Search(TrsResultRecord.class);
        search.addFilterEqual("session.executionId", sessionId);
        return this.search(search);
    }

    @Override
    public Optional<TrsResultRecord> findResultBySessionAndTestCase(String sessionId, Long testCaseId) {
        Search search = new Search(TrsResultRecord.class);
        search.addFilterEqual("session.executionId", sessionId);
        search.addFilterEqual("testCase.id", testCaseId);
        return Optional.ofNullable(this.searchUnique(search));
    }
}
