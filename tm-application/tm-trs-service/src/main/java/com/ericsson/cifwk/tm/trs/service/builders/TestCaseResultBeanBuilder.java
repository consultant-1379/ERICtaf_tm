package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;

import java.util.Map;

public class TestCaseResultBeanBuilder {

    private TestCaseResultBean resultBean;

    public TestCaseResultBeanBuilder() {
        resultBean = new TestCaseResultBean();
    }

    public TestCaseResultBeanBuilder withId(String id) {
        resultBean.setId(id);
        return this;
    }

    public TestCaseResultBeanBuilder withName(String name) {
        resultBean.setName(name);
        return this;
    }

    public TestCaseResultBeanBuilder withTime(ExecutionTimeBean executionTimeBean) {
        resultBean.setTime(executionTimeBean);
        return this;
    }

    public TestCaseResultBeanBuilder withResultCode(String resultCode) {
        resultBean.setResultCode(resultCode);
        return this;
    }

    public TestCaseResultBeanBuilder withAdditionalFields(Map<String, Object> additionalFields) {
        resultBean.addAdditionalFields(additionalFields);
        return this;
    }

    public TestCaseResultBean build() {
        return resultBean;
    }
}
