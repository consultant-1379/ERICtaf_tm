package com.ericsson.cifwk.tm.trs.service;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;

import java.util.Optional;

public interface TrsService {

    Optional<TrsRecord> recordExecutions(TestCampaign testCampaign, TestExecution... testExecutions);

}
