package com.ericsson.cifwk.tm.trs;

import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobConfigurationBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;

import java.util.List;

public interface TrsClient {

    JobConfigurationBean getJobConfiguration(String jobName);

    List<JobBean> queryForJobByName(String jobName);

    JobBean createOrUpdateJob(String contextId, String jobName, JobBean jobBean);

    TestSessionBean createOrUpdateSession(String contextId, String jobName, String executionId, TestSessionBean testSessionBean);

    TestCaseResultBean createOrUpdateTestCase(String contextId, String jobName, String testSessionId,
                                              String suiteName, TestCaseResultBean testCaseResultBean);

}
