package com.ericsson.cifwk.tm.trs.service;

import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.trs.service.impl.TrsRecordHelperImpl;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.google.inject.ImplementedBy;

import java.util.Optional;

@ImplementedBy(TrsRecordHelperImpl.class)
public interface TrsRecordHelper {

    Optional<TrsJobRecord> findJobByName(String name);

    Optional<TrsSessionRecord> findSessionByJobAndIso(String jobId, Long isoId);

    Optional<TrsResultRecord> findResultBySessionAndTestCase(String sessionId, Long testCaseId);

    TrsJobRecord createJobRecord(TestCampaign testCampaign, ISO iso, JobBean jobBean);

    void updateJobRecordWithNewTestCampaign(TrsJobRecord jobRecord, TestCampaign testCampaign);

    TrsSessionRecord createOrUpdateSessionRecord(ISO iso, TestSessionBean sessionBean, TrsJobRecord jobRecord);

    Optional<TrsResultRecord> createResultRecordIfNotExists(TestCaseResultBean resultBean, TrsSessionRecord sessionRecord);
}
