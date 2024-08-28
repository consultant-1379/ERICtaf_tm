package com.ericsson.cifwk.tm.integration.trs.client;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobConfigurationBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TrsClientITest extends BaseServiceLayerTest {

    private static final String CONTEXT_ID = "55d4b4d2-e9e1-11e5-9ce9-5e5517507c66";

    private static final String JOB_NAME = "TMS_JOB";

    private static final String TEST_SESSION_EXECUTION_ID = "1d1faae2-41f8-11e6-beb8-9e71128cae77";

    private static final String TEST_SUITE_NAME = "MANUAL_TEST_SUITE";

    @Inject
    private TrsClient trsClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getJobConfig() {
        JobConfigurationBean config = trsClient.getJobConfiguration(JOB_NAME);
        assertThat(config.getJobName(), equalTo(JOB_NAME));
        assertThat(config.getContextId(), equalTo(CONTEXT_ID));
    }

    @Test
    public void queryForJobByName() {
        List<JobBean> jobs = trsClient.queryForJobByName(JOB_NAME);
        assertThat(jobs.size(), equalTo(1));
        assertThat(jobs.get(0).getName(), equalTo(JOB_NAME));
        assertThat(jobs.get(0).getId(), notNullValue());
        assertThat(jobs.get(0).getContext(), notNullValue());
    }

    @Test
    public void createOrUpdateJob() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tvs-data/jobBean.json");
        JobBean jobBean = objectMapper.readValue(inputStream, JobBean.class);
        JobBean created = trsClient.createOrUpdateJob(CONTEXT_ID, JOB_NAME, jobBean);

        assertThat(created, notNullValue());
        assertThat(created.getId(), notNullValue());
        assertThat(created.getName(), equalTo(JOB_NAME));
        assertThat(created.getContext(), equalTo(CONTEXT_ID));
    }

    @Test
    public void createOrUpdateTestSession() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tvs-data/testSessionBean.json");
        TestSessionBean sessionBean = objectMapper.readValue(inputStream, TestSessionBean.class);
        TestSessionBean created = trsClient.createOrUpdateSession(CONTEXT_ID, JOB_NAME, sessionBean.getExecutionId(), sessionBean);

        assertThat(created, notNullValue());
        assertThat(created.getExecutionId(), equalTo(sessionBean.getExecutionId()));
    }

    @Test
    public void createOrUpdateTestCaseResult() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tvs-data/testCaseResultBean.json");
        TestCaseResultBean resultBean = objectMapper.readValue(inputStream, TestCaseResultBean.class);
        TestCaseResultBean created = trsClient.createOrUpdateTestCase(CONTEXT_ID, JOB_NAME, TEST_SESSION_EXECUTION_ID, TEST_SUITE_NAME, resultBean);

        assertThat(created, notNullValue());
        assertThat(created.getId(), notNullValue());
        assertThat(created.getCreatedDate(), notNullValue());
        assertThat(created.getModifiedDate(), notNullValue());
    }

}
