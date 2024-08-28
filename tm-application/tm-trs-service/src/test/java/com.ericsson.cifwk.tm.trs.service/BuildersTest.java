package com.ericsson.cifwk.tm.trs.service;

import com.ericsson.cifwk.tm.trs.service.builders.ExecutionTimeBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.StatisticsBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestCaseResultBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestSessionBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestSuiteResultBeanBuilder;
import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;
import com.ericsson.gic.tms.tvs.presentation.dto.StatisticsBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSuiteResultBean;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BuildersTest {

    @Test
    public void executionTimeBeanBuilderTest() {
        Date startDate = new Date(System.currentTimeMillis() - 1000);
        Date stopDate = new Date(System.currentTimeMillis());

        ExecutionTimeBean executionTimeBean = new ExecutionTimeBeanBuilder()
                .withStartDate(startDate)
                .withStopDate(stopDate)
                .build();

        assertThat(executionTimeBean.getStartDate(), equalTo(startDate));
        assertThat(executionTimeBean.getStopDate(), equalTo(stopDate));
    }

    @Test
    public void statisticsBeanBuilderTest() {
        StatisticsBean statisticsBean = createStatisticsBean();

        assertThat(statisticsBean.getTotal(), equalTo(8));
        assertThat(statisticsBean.getPassed(), equalTo(2));
        assertThat(statisticsBean.getPending(), equalTo(3));
        assertThat(statisticsBean.getFailed(), equalTo(1));
        assertThat(statisticsBean.getCancelled(), equalTo(1));
        assertThat(statisticsBean.getBroken(), equalTo(1));
    }

    @Test
    public void testCaseResultBeanBuilderTest() {
        String id = UUID.randomUUID().toString();
        String name = "TEST_CASE";
        ExecutionTimeBean timeBean = createExecutionTimeBean();
        String resultCode = "PASSED";

        TestCaseResultBean resultBean = new TestCaseResultBeanBuilder()
                .withId(id)
                .withName(name)
                .withTime(timeBean)
                .withResultCode(resultCode)
                .build();

        assertThat(resultBean.getId(), equalTo(id));
        assertThat(resultBean.getName(), equalTo(name));
        assertThat(resultBean.getTime(), equalTo(timeBean));
        assertThat(resultBean.getResultCode(), equalTo(resultCode));
    }

    @Test
    public void testSuiteResultBeanBuilderTest() {
        String id = UUID.randomUUID().toString();
        ExecutionTimeBean timeBean = createExecutionTimeBean();
        StatisticsBean statisticsBean = createStatisticsBean();

        TestCaseResultBean resultBean = new TestCaseResultBeanBuilder()
                .withId(UUID.randomUUID().toString())
                .withName("Test Case")
                .withTime(createExecutionTimeBean())
                .withResultCode("PASSED")
                .build();

        TestSuiteResultBean suiteResultBean = new TestSuiteResultBeanBuilder()
                .withId(id)
                .withTime(timeBean)
                .withStatistics(statisticsBean)
                .withResults(Lists.newArrayList(resultBean))
                .build();

        assertThat(suiteResultBean.getId(), equalTo(id));
        assertThat(suiteResultBean.getTime(), equalTo(timeBean));
        assertThat(suiteResultBean.getStatistics(), equalTo(statisticsBean));
        assertThat(suiteResultBean.getTestCaseResults().size(), equalTo(1));
        assertTrue(suiteResultBean.getTestCaseResults().contains(resultBean));
    }

    @Test
    public void testSessionBuilderTest() {
        String executionId = "executionId";
        String uri = "uri";
        ExecutionTimeBean timeBean = createExecutionTimeBean();
        StatisticsBean statisticsBean = createStatisticsBean();
        Map<String, String> logReferences = ImmutableMap.of("key", "value");
        String resultCode = "PASSED";
        int passRate = 65;

        TestCaseResultBean resultBean = new TestCaseResultBeanBuilder()
                .withId(UUID.randomUUID().toString())
                .withName("Test Case")
                .withTime(timeBean)
                .withResultCode("PASSED")
                .build();

        TestSuiteResultBean suiteResultBean = new TestSuiteResultBeanBuilder()
                .withId(UUID.randomUUID().toString())
                .withTime(timeBean)
                .withStatistics(statisticsBean)
                .withResults(Lists.newArrayList(resultBean))
                .build();

        TestSessionBean sessionBean = new TestSessionBeanBuilder()
                .withExecutionId(executionId)
                .withExecutionTime(timeBean)
                .withUri(uri)
                .withTestSuites(Lists.newArrayList(suiteResultBean))
                .withLogReferences(logReferences)
                .withResultCode(resultCode)
                .withPassRate(passRate)
                .build();

        assertThat(sessionBean.getId(), equalTo(executionId));
        assertThat(sessionBean.getExecutionId(), equalTo(executionId));
        assertThat(sessionBean.getTime(), equalTo(timeBean));
        assertThat(sessionBean.getUri(), equalTo(uri));
        assertTrue(sessionBean.getTestSuites().contains(suiteResultBean));
        assertThat(sessionBean.getTestSuites().size(), equalTo(1));
        assertThat(sessionBean.getTestSuiteCount(), equalTo(1));
        assertThat(sessionBean.getTestCaseCount(), equalTo(1));
        assertThat(sessionBean.getLogReferences(), equalTo(logReferences));
        assertThat(sessionBean.getPassRate(), equalTo(passRate));
    }

    private ExecutionTimeBean createExecutionTimeBean() {
        return new ExecutionTimeBeanBuilder()
                .withStartDate(new Date(System.currentTimeMillis() - 1000))
                .withStopDate(new Date(System.currentTimeMillis()))
                .build();
    }

    private StatisticsBean createStatisticsBean() {
        return new StatisticsBeanBuilder()
                .withTotal(8)
                .withPassed(2)
                .withPending(3)
                .withFailed(1)
                .withCancelled(1)
                .withBroken(1)
                .build();
    }
}
