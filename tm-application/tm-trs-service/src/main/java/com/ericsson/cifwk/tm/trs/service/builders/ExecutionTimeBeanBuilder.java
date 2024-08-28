package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;

import java.util.Date;

public class ExecutionTimeBeanBuilder {

    private ExecutionTimeBean executionTimeBean;

    public ExecutionTimeBeanBuilder() {
        executionTimeBean = new ExecutionTimeBean();
    }

    public ExecutionTimeBeanBuilder withStartDate(Date startDate) {
        executionTimeBean.setStartDate(startDate);
        return this;
    }

    public ExecutionTimeBeanBuilder withStopDate(Date stopDate) {
        executionTimeBean.setStopDate(stopDate);
        return this;
    }

    public ExecutionTimeBean build() {
        return executionTimeBean;
    }
}
