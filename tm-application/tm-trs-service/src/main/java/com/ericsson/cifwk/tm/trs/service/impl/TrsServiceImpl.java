package com.ericsson.cifwk.tm.trs.service.impl;


import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.trs.service.TrsRecordHelper;
import com.ericsson.cifwk.tm.trs.service.TrsRestClient;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.ericsson.cifwk.tm.trs.service.exceptions.DuplicateJobException;
import com.ericsson.cifwk.tm.trs.service.exceptions.UnresolvedContextException;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.persist.Transactional;
import com.netflix.governator.annotations.Configuration;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TrsServiceImpl implements TrsService {

    @Configuration("default.contextId")
    private String defaultContextId;

    @Inject
    private TrsRecordHelper recordHelper;

    @Inject
    private TrsRestClient restClient;

    @Inject
    private DtoHelper dtoHelper;

    @Override
    @Transactional
    public Optional<TrsRecord> recordExecutions(TestCampaign testCampaign, TestExecution ... testExecutions) {
        String contextId = resolveContext(testCampaign.getName());

        Optional<TrsJobRecord> optionalJobRecord = recordHelper.findJobByName(testCampaign.getName());

        if (optionalJobRecord.isPresent()) {
            TrsJobRecord jobRecord = optionalJobRecord.get();
            recordHelper.updateJobRecordWithNewTestCampaign(jobRecord, testCampaign);

            ISO iso = testExecutions[0].getIso();
            Optional<TrsSessionRecord> optionalSessionRecord = recordHelper.findSessionByJobAndIso(jobRecord.getJobId(), iso.getId());

            if (optionalSessionRecord.isPresent()) {
                TrsSessionRecord sessionRecord = optionalSessionRecord.get();

                Map<String, TestExecution> resultMapping = Maps.newHashMap();

                for (TestExecution testExecution : testExecutions) {
                    Long testCaseId = testExecution.getTestCaseVersion().getTestCase().getId();
                    Optional<TrsResultRecord> optionalResultRecord = recordHelper.findResultBySessionAndTestCase(sessionRecord.getExecutionId(), testCaseId);

                    if (optionalResultRecord.isPresent()) {
                        TrsResultRecord resultRecord = optionalResultRecord.get();
                        String trsResultId = resultRecord.getTrsResultId();
                        resultMapping.put(trsResultId, testExecution);
                    } else {
                        resultMapping.put(generateNewUuid(), testExecution);
                    }
                }
                return updateSession(contextId, sessionRecord, resultMapping);
            } else {
                return createNewSession(contextId, testCampaign, Lists.newArrayList(testExecutions), jobRecord);
            }
        } else {
            return createNewJob(contextId, testCampaign, Lists.newArrayList(testExecutions));
        }
    }

    private String generateNewUuid() {
        return UUID.randomUUID().toString();
    }

    private String resolveContext(String testCampaignName) {
        List<JobBean> jobs = restClient.queryForJobByName(testCampaignName);
        checkForDuplicateJobs(jobs, testCampaignName);

        String contextId;
        if (jobs.isEmpty()) {
            contextId = defaultContextId;
        } else {
            contextId = jobs.get(0).getContext();
        }
        if (Strings.isNullOrEmpty(contextId)) {
            throw new UnresolvedContextException(testCampaignName);
        }
        return contextId;
    }

    private void checkForDuplicateJobs(List<JobBean> jobs, String testCampaignName) {
        if (jobs.size() > 1) {
            List<String> contextIds = jobs.stream()
                    .map(j -> j.getContext())
                    .collect(Collectors.toList());

            String contexts = Joiner.on(", ").join(contextIds);
            throw new DuplicateJobException(testCampaignName, contexts);
        }
    }

    private Optional<TrsRecord> createNewJob(String contextId, TestCampaign testCampaign, List<TestExecution> testExecutions) {
        JobBean jobBean = dtoHelper.createJobBean(contextId, testCampaign, testExecutions);
        JobBean created = restClient.createOrUpdateJob(contextId, testCampaign.getName(), jobBean);
        Optional<TrsRecord> record = Optional.empty();
        if (created != null) {
            ISO iso = testExecutions.get(0).getIso();
            record = Optional.of(recordHelper.createJobRecord(testCampaign, iso, created));
        }
        return record;
    }

    private Optional<TrsRecord> createNewSession(String contextId, TestCampaign testCampaign, List<TestExecution> testExecutions, TrsJobRecord jobRecord) {
        TestSessionBean sessionBean = dtoHelper.createSessionBean(testCampaign, testExecutions);
        TestSessionBean created = restClient.createOrUpdateSession(contextId, testCampaign.getName(), sessionBean.getExecutionId(), sessionBean);
        Optional<TrsRecord> record = Optional.empty();
        if (created != null) {
            ISO iso = testExecutions.get(0).getIso();
            record = Optional.of(recordHelper.createOrUpdateSessionRecord(iso, sessionBean, jobRecord));
        }
        return record;
    }

    private Optional<TrsRecord> updateSession(String contextId, TrsSessionRecord sessionRecord, Map<String, TestExecution> resultMapping) {
        TestSessionBean sessionBean = dtoHelper.createSessionBeanForUpdate(sessionRecord, resultMapping);
        String jobName = sessionRecord.getJob().getJobName();
        TestSessionBean created = restClient.createOrUpdateSession(contextId, jobName, sessionBean.getExecutionId(), sessionBean);
        Optional<TrsRecord> record = Optional.empty();
        if (created != null) {
            ISO iso = resultMapping.entrySet().iterator().next().getValue().getIso();
            TrsJobRecord jobRecord = sessionRecord.getJob();
            record = Optional.of(recordHelper.createOrUpdateSessionRecord(iso, sessionBean, jobRecord));
        }
        return record;
    }

}
