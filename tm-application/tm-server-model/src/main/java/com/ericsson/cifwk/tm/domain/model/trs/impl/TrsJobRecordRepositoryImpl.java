package com.ericsson.cifwk.tm.domain.model.trs.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecordRepository;
import com.googlecode.genericdao.search.Search;

import java.util.Optional;

public class TrsJobRecordRepositoryImpl extends BaseRepositoryImpl<TrsJobRecord, Long> implements TrsJobRecordRepository {

    @Override
    public Optional<TrsJobRecord> findJobByName(String name) {
        Search search = new Search(TrsJobRecord.class);
        search.addFilterEqual("jobName", name);
        TrsJobRecord job = this.searchUnique(search);

        return Optional.ofNullable(job);
    }
}
