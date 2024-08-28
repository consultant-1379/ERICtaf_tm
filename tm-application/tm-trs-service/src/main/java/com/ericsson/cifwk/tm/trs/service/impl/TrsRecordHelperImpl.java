package com.ericsson.cifwk.tm.trs.service.impl;

import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecordRepository;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecordRepository;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecordRepository;
import com.ericsson.cifwk.tm.trs.service.TrsRecordHelper;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TrsRecordHelperImpl implements TrsRecordHelper {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TrsJobRecordRepository jobRecordRepository;

    @Inject
    private TrsSessionRecordRepository sessionRecordRepository;

    @Inject
    private TrsResultRecordRepository resultRecordRepository;

    @Override
    public Optional<TrsJobRecord> findJobByName(String name) {
        return jobRecordRepository.findJobByName(name);
    }

    @Override
    public Optional<TrsSessionRecord> findSessionByJobAndIso(String jobId, Long isoId) {
        return sessionRecordRepository.findSessionByJobAndIso(jobId, isoId);
    }

    @Override
    public Optional<TrsResultRecord> findResultBySessionAndTestCase(String sessionId, Long testCaseId) {
        return resultRecordRepository.findResultBySessionAndTestCase(sessionId, testCaseId);
    }

    @Override
    public TrsJobRecord createJobRecord(TestCampaign testCampaign, ISO iso, JobBean jobBean) {
        TrsJobRecord jobRecord = new TrsJobRecord();
        jobRecord.setJobName(jobBean.getName());
        jobRecord.setJobId(jobBean.getId());
        jobRecord.addTestCampaign(testCampaign);

        jobRecordRepository.save(jobRecord);

        List<TestSessionBean> sessions = jobBean.getTestSessions();
        List<TrsSessionRecord> sessionRecords = Lists.newArrayList();

        sessions.forEach(session -> {
                TrsSessionRecord sessionRecord = createOrUpdateSessionRecord(iso, session, jobRecord);
                sessionRecords.add(sessionRecord);
            });

        jobRecord.setSessions(sessionRecords);

        return jobRecordRepository.save(jobRecord);
    }

    @Override
    public void updateJobRecordWithNewTestCampaign(TrsJobRecord jobRecord, TestCampaign testCampaign) {
        List<TestCampaign> testCampaigns = jobRecord.getTestCampaigns();
        if (!testCampaigns.contains(testCampaign)) {
            jobRecord.addTestCampaign(testCampaign);
            jobRecordRepository.save(jobRecord);
        }
    }

    @Override
    public TrsSessionRecord createOrUpdateSessionRecord(ISO iso, TestSessionBean session, TrsJobRecord jobRecord) {
        TrsSessionRecord sessionRecord;
        Optional<TrsSessionRecord> optionalSessionRecord = sessionRecordRepository.findSessionByJobAndIso(jobRecord.getJobId(), iso.getId());

        if (optionalSessionRecord.isPresent()) {
            sessionRecord = optionalSessionRecord.get();
        } else {
            sessionRecord = new TrsSessionRecord(session.getExecutionId(), iso, jobRecord);
            sessionRecordRepository.save(sessionRecord);
        }

        // only 1 suite for manual testing
        List<TestCaseResultBean> results = session.getTestSuites().get(0).getTestCaseResults();
        Set<TrsResultRecord> resultRecords = Sets.newHashSet();

        for (TestCaseResultBean result : results) {
            Optional<TrsResultRecord> optionalResultRecord = createResultRecordIfNotExists(result, sessionRecord);
            // add new results
            if (optionalResultRecord.isPresent()) {
                resultRecords.add(optionalResultRecord.get());
            }
        }

        sessionRecord.addResults(resultRecords);

        return sessionRecordRepository.save(sessionRecord);
    }

    @Override
    public Optional<TrsResultRecord> createResultRecordIfNotExists(TestCaseResultBean resultBean, TrsSessionRecord sessionRecord) {
        String tvsResultId = resultBean.getId();
        TestCase testCase = testCaseRepository.findByAnyId(resultBean.getName());

        Optional<TrsResultRecord> optionalResultRecord =
                resultRecordRepository.findResultBySessionAndTestCase(sessionRecord.getExecutionId(), testCase.getId());

        if (!optionalResultRecord.isPresent()) {
            TrsResultRecord resultRecord = new TrsResultRecord(tvsResultId, testCase, sessionRecord);
            return Optional.of(resultRecordRepository.save(resultRecord));
        }
        return Optional.empty();
    }
}
