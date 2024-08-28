package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;

import java.util.List;

public class JobBeanBuilder {

    private JobBean jobBean;

    public JobBeanBuilder() {
        jobBean = new JobBean();
    }

    public JobBeanBuilder withContext(String context) {
        jobBean.setContext(context);
        return this;
    }

    public JobBeanBuilder withSessions(List<TestSessionBean> sessions) {
        jobBean.setTestSessions(sessions);
        return this;
    }

    public JobBean build() {
        return jobBean;
    }
}
