package com.ericsson.cifwk.tm.trs.service.builders;

import com.ericsson.gic.tms.tvs.presentation.dto.StatisticsBean;

public class StatisticsBeanBuilder {

    private StatisticsBean statisticsBean;

    public StatisticsBeanBuilder() {
        statisticsBean = new StatisticsBean();
    }

    public StatisticsBeanBuilder withTotal(int total) {
        statisticsBean.setTotal(total);
        return this;
    }

    public StatisticsBeanBuilder withPassed(int passed) {
        statisticsBean.setPassed(passed);
        return this;
    }

    public StatisticsBeanBuilder withPending(int pending) {
        statisticsBean.setPending(pending);
        return this;
    }

    public StatisticsBeanBuilder withCancelled(int cancelled) {
        statisticsBean.setCancelled(cancelled);
        return this;
    }

    public StatisticsBeanBuilder withFailed(int failed) {
        statisticsBean.setFailed(failed);
        return this;
    }

    public StatisticsBeanBuilder withBroken(int broken) {
        statisticsBean.setBroken(broken);
        return this;
    }

    public StatisticsBean build() {
        return statisticsBean;
    }
}
