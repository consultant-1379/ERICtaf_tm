package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobConfigurationBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TrsClientStub implements TrsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrsClientStub.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JobConfigurationBean getJobConfiguration(String jobName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tvs-data/jobConfigurationBean.json");
        JobConfigurationBean jobConfigurationBean = null;
        try {
            jobConfigurationBean = objectMapper.readValue(inputStream, JobConfigurationBean.class);
            jobConfigurationBean.setJobName(jobName);
        } catch (IOException e) {
            LOGGER.error("Could not read job config data from file", e);
        }
        return jobConfigurationBean;
    }

    @Override
    public List<JobBean> queryForJobByName(String jobName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tvs-data/jobBean.json");
        JobBean jobBean = null;
        try {
            jobBean = objectMapper.readValue(inputStream, JobBean.class);
            jobBean.setId(generateUuid());
            jobBean.setName(jobName);
            jobBean.setContext(generateUuid());
        } catch (IOException e) {
            LOGGER.error("Could not read job bean data from file", e);
        }
        return Lists.newArrayList(jobBean);
    }

    @Override
    public JobBean createOrUpdateJob(String contextId, String jobName, JobBean jobBean) {
        jobBean.setId(generateUuid());
        jobBean.setContext(contextId);
        jobBean.setName(jobName);
        return jobBean;
    }

    @Override
    public TestSessionBean createOrUpdateSession(String contextId, String jobName, String executionId, TestSessionBean testSessionBean) {
        return testSessionBean;
    }

    @Override
    public TestCaseResultBean createOrUpdateTestCase(String contextId, String jobName, String testSessionId, String suiteName,
                                                     TestCaseResultBean testCaseResultBean) {

        testCaseResultBean.setId(generateUuid());
        testCaseResultBean.setCreatedDate(new Date(System.currentTimeMillis() - 10000));
        testCaseResultBean.setModifiedDate(new Date(System.currentTimeMillis()));
        return testCaseResultBean;
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
