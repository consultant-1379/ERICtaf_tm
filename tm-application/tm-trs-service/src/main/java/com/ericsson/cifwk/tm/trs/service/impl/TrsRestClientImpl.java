package com.ericsson.cifwk.tm.trs.service.impl;

import com.ericsson.cifwk.tm.tce.TceClient;
import com.ericsson.cifwk.tm.trs.service.TrsRestClient;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobConfigurationBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;

import javax.inject.Inject;
import java.util.List;

public class TrsRestClientImpl implements TrsRestClient {

    @Inject
    private TrsClient trsClient;

    @Inject
    private TceClient tceClient;

    @Override
    public String getContextIdByName(String contextName) {
        return tceClient.getContextIdByName(contextName);
    }

    @Override
    public JobConfigurationBean getJobConfiguration(String jobName) {
        return trsClient.getJobConfiguration(jobName);
    }

    @Override
    public List<JobBean> queryForJobByName(String jobName) {
        return trsClient.queryForJobByName(jobName);
    }

    @Override
    public JobBean createOrUpdateJob(String contextId, String jobName, JobBean jobBean) {
        return trsClient.createOrUpdateJob(contextId, jobName, jobBean);
    }

    @Override
    public TestSessionBean createOrUpdateSession(String contextId, String jobName, String executionId, TestSessionBean testSessionBean) {
        return trsClient.createOrUpdateSession(contextId, jobName, executionId, testSessionBean);
    }

    @Override
    public TestCaseResultBean createOrUpdateTestCase(String contextId, String jobName, String testSessionId,
                                                     String suiteName, TestCaseResultBean testCaseResultBean) {

        return trsClient.createOrUpdateTestCase(contextId, jobName, testSessionId, suiteName, testCaseResultBean);
    }
}
