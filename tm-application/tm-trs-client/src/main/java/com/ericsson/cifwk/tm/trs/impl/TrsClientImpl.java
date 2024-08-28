package com.ericsson.cifwk.tm.trs.impl;

import com.ericsson.cifwk.tm.common.ClientFactory;
import com.ericsson.cifwk.tm.common.UrlHelper;
import com.ericsson.cifwk.tm.trs.TrsClient;
import com.ericsson.gic.tms.presentation.dto.jsonapi.DocumentList;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobConfigurationBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.ericsson.gic.tms.tvs.presentation.resources.JobConfigurationResource;
import com.ericsson.gic.tms.tvs.presentation.resources.JobInsertResource;
import com.ericsson.gic.tms.tvs.presentation.resources.TestCaseResultInsertResource;
import com.ericsson.gic.tms.tvs.presentation.resources.TestSessionInsertResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.glassfish.jersey.client.proxy.WebResourceFactory.newResource;

public class TrsClientImpl implements TrsClient {

    private static final String SOURCE = "ENM"; //hardcoded in TVS

    private WebTarget apiTarget;
    private JobInsertResource jobInsertResource;
    private TestCaseResultInsertResource resultInsertResource;
    private TestSessionInsertResource testSessionInsertResource;
    private JobConfigurationResource jobConfigurationResource;

    @Inject
    private UrlHelper urlHelper;

    @PostConstruct
    public void createResources() {
        apiTarget = ClientFactory.newWebtarget(urlHelper.getTvsApiUrl());
        jobInsertResource = newResource(JobInsertResource.class, apiTarget);
        resultInsertResource = newResource(TestCaseResultInsertResource.class, apiTarget);
        testSessionInsertResource = newResource(TestSessionInsertResource.class, apiTarget);
        jobConfigurationResource = newResource(JobConfigurationResource.class, apiTarget);
    }

    @Override
    public JobConfigurationBean getJobConfiguration(String jobName) {
        return jobConfigurationResource.get(jobName, SOURCE).unwrap();
    }

    @Override
    public List<JobBean> queryForJobByName(String jobName) {
        String url = urlHelper.buildJobQueryUrl(jobName);
        WebTarget target = ClientFactory.newWebtarget(url);

        Response response = target
                .request()
                .get();

        DocumentList<JobBean> wrappedJob = response.readEntity(new GenericType<DocumentList<JobBean>>() {});

        return wrappedJob.unwrap();
    }

    @Override
    public JobBean createOrUpdateJob(String contextId, String jobName, JobBean jobBean) {
        return jobInsertResource.updateJob(contextId, jobName, jobBean).unwrap();
    }

    @Override
    public TestSessionBean createOrUpdateSession(String contextId, String jobName, String executionId, TestSessionBean testSessionBean) {
        return testSessionInsertResource.updateTestSession(contextId, jobName, executionId, testSessionBean).unwrap();
    }

    @Override
    public TestCaseResultBean createOrUpdateTestCase(String contextId, String jobName, String testSessionId, String suiteName,
                                                     TestCaseResultBean testCaseResultBean) {

        return resultInsertResource.updateTestCaseResult(contextId, jobName, testSessionId, suiteName,
                        testCaseResultBean.getId(), testCaseResultBean).unwrap();

    }
}
