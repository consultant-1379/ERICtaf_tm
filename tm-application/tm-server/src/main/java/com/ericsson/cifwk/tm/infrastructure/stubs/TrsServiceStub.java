package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.trs.service.TrsRecordHelper;
import com.ericsson.cifwk.tm.trs.service.TrsRestClient;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.ericsson.cifwk.tm.trs.service.impl.DtoHelper;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TrsServiceStub implements TrsService {

    @Inject
    private TrsRecordHelper recordHelper;

    @Inject
    private TrsRestClient restClient;

    @Inject
    private DtoHelper dtoHelper;

    @Override
    @Transactional
    public Optional<TrsRecord> recordExecutions(TestCampaign testCampaign, TestExecution... testExecutions) {
        return createNewJob(UUID.randomUUID().toString(), testCampaign, Lists.newArrayList(testExecutions));
    }

    private Optional<TrsRecord> createNewJob(String contextId, TestCampaign testCampaign, List<TestExecution> testExecutions) {
        JobBean jobBean = dtoHelper.createJobBean(contextId, testCampaign, testExecutions);
        JobBean created = restClient.createOrUpdateJob(contextId, testCampaign.getName(), jobBean);
        Optional<TrsRecord> optionalRecord = Optional.empty();
        if (created != null) {
            ISO iso = testExecutions.get(0).getIso();
            optionalRecord = Optional.ofNullable(recordHelper.createJobRecord(testCampaign, iso, created));
        }
        return optionalRecord;
    }

}
