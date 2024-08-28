package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;
import com.ericsson.gic.tms.tvs.presentation.dto.StatisticsBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSuiteResultBean;

import java.util.List;

public class TestSuiteResultBeanBuilder {

    private TestSuiteResultBean resultBean;

    public TestSuiteResultBeanBuilder() {
        resultBean = new TestSuiteResultBean();
    }

    public TestSuiteResultBeanBuilder withId(String id) {
        resultBean.setId(id);
        return this;
    }

    public TestSuiteResultBeanBuilder withName(String name) {
        resultBean.setId(name);
        return this;
    }

    public TestSuiteResultBeanBuilder withTime(ExecutionTimeBean executionTime) {
        resultBean.setTime(executionTime);
        return this;
    }

    public TestSuiteResultBeanBuilder withStatistics(StatisticsBean statistics) {
        resultBean.setStatistics(statistics);
        return this;
    }

    public TestSuiteResultBeanBuilder withResults(List<TestCaseResultBean> results) {
        resultBean.setTestCaseResults(results);
        return this;
    }

    public TestSuiteResultBean build() {
        return resultBean;
    }
}
