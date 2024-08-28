package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSuiteResultBean;

import java.util.List;
import java.util.Map;

public class TestSessionBeanBuilder {

    private TestSessionBean sessionBean;

    public TestSessionBeanBuilder() {
        sessionBean = new TestSessionBean();
    }

    public TestSessionBeanBuilder withExecutionId(String executionId) {
        sessionBean.setExecutionId(executionId);
        return this;
    }

    public TestSessionBeanBuilder withExecutionTime(ExecutionTimeBean timeBean) {
        sessionBean.setTime(timeBean);
        return this;
    }

    public TestSessionBeanBuilder withUri(String uri) {
        sessionBean.setUri(uri);
        return this;
    }

    public TestSessionBeanBuilder withTestSuites(List<TestSuiteResultBean> testSuites) {
        sessionBean.setTestSuites(testSuites);
        sessionBean.setTestSuiteCount(testSuites.size());
        sessionBean.setTestCaseCount(getTestCaseCount(testSuites));
        return this;
    }

    public TestSessionBeanBuilder withLogReferences(Map<String, String> logReferences) {
        sessionBean.setLogReferences(logReferences);
        return this;
    }

    public TestSessionBeanBuilder withResultCode(String resultCode) {
        sessionBean.setResultCode(resultCode);
        return this;
    }

    public TestSessionBeanBuilder withPassRate(Integer passRate) {
        sessionBean.setPassRate(passRate);
        return this;
    }

    public TestSessionBeanBuilder withAdditionalFields(Map<String, Object> additionalFields) {
        sessionBean.addAdditionalFields(additionalFields);
        return this;
    }

    private Integer getTestCaseCount(List<TestSuiteResultBean> testSuites) {
        Integer testCaseCount = 0;
        for (TestSuiteResultBean suite : testSuites) {
            testCaseCount += suite.getTestCaseResults().size();
        }
        return testCaseCount;
    }

    public TestSessionBean build() {
        return sessionBean;
    }

}
