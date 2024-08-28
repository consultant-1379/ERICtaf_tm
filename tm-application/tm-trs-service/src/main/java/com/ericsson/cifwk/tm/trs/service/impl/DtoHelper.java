package com.ericsson.cifwk.tm.trs.service.impl;

import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.trs.service.Constants;
import com.ericsson.cifwk.tm.trs.service.builders.ExecutionTimeBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.JobBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestCaseResultBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestSessionBeanBuilder;
import com.ericsson.cifwk.tm.trs.service.builders.TestSuiteResultBeanBuilder;
import com.ericsson.gic.tms.tvs.presentation.dto.ExecutionTimeBean;
import com.ericsson.gic.tms.tvs.presentation.dto.JobBean;
import com.ericsson.gic.tms.tvs.presentation.dto.StatisticsBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestCaseResultBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSessionBean;
import com.ericsson.gic.tms.tvs.presentation.dto.TestSuiteResultBean;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ericsson.gic.tms.tvs.presentation.constant.FieldNameConst.*;

public class DtoHelper {

    public TestCaseResultBean createResultBean(Optional<String> optionalResultId, TestExecution testExecution) {
        String name = testExecution.getTestCaseVersion().getTestCase().getTestCaseId();
        String resultCode = testExecution.getResult().name();
        String resultId = optionalResultId.isPresent() ? optionalResultId.get() : generateUuid();

        return new TestCaseResultBeanBuilder()
                .withId(resultId)
                .withName(name)
                .withTime(createExecutionTimeBean())
                .withResultCode(resultCode)
                .withAdditionalFields(getTestCaseData(testExecution.getTestCaseVersion()))
                .withAdditionalFields(getDropAndIsoData(testExecution))
                .build();
    }

    private TestCaseResultBean createDefaultResultBean(TestCase testCase) {
        String name = testCase.getTestCaseId();
        String resultCode = TestExecutionResult.NOT_STARTED.name();

        return new TestCaseResultBeanBuilder()
                .withId(generateUuid())
                .withName(name)
                .withTime(createExecutionTimeBean())
                .withResultCode(resultCode)
                .withAdditionalFields(getTestCaseData(testCase.getCurrentVersion()))
                .build();
    }

    private List<TestCaseResultBean> createResultBeanList(TestCampaign testCampaign, List<TestExecution> testExecutions) {
        Set<TestCampaignItem> testCampaignItems = testCampaign.getTestCampaignItems();
        List<TestCase> testCases = testCampaignItems.stream()
                .map(item -> item.getTestCaseVersion().getTestCase())
                .collect(Collectors.toList());

        List<TestCase> executedTestCases = testExecutions.stream()
                .map(execution -> execution.getTestCaseVersion()
                        .getTestCase())
                .collect(Collectors.toList());

        // for creation of default 'NOT_STARTED' results
        testCases.removeAll(executedTestCases);

        List<TestCaseResultBean> defaultResults = testCases.stream()
                .map(testCase -> createDefaultResultBean(testCase))
                .collect(Collectors.toList());

        List<TestCaseResultBean> executedTestCasesResults = testExecutions.stream()
                .map(execution -> createResultBean(Optional.empty(), execution))
                .collect(Collectors.toList());

        List<TestCaseResultBean> allResults = Lists.newArrayList();
        allResults.addAll(defaultResults);
        allResults.addAll(executedTestCasesResults);

        return allResults;
    }

    private TestSuiteResultBean createSuiteBean(List<TestCaseResultBean> results) {
        return new TestSuiteResultBeanBuilder()
                .withId(generateUuid())
                .withName(Constants.MANUAL_TEST_SUITE_NAME)
                .withTime(createExecutionTimeBean())
                .withResults(results)
                .withStatistics(new StatisticsBean())
                .build();
    }

    public TestSessionBean createSessionBean(TestCampaign testCampaign, List<TestExecution> testExecutions) {
        List<TestCaseResultBean> testCaseResultBeans = createResultBeanList(testCampaign, testExecutions);
        TestSuiteResultBean suiteResultBean = createSuiteBean(testCaseResultBeans);

        TestExecution testExecution = testExecutions.get(0);

        return new TestSessionBeanBuilder()
                .withExecutionId(getIsoDetails(testExecution))
                .withExecutionTime(createExecutionTimeBean())
                .withTestSuites(Lists.newArrayList(suiteResultBean))
                .withPassRate(calculatePassRate(suiteResultBean.getStatistics()))
                .withAdditionalFields(getDropAndIsoData(testExecution))
                .build();
    }

    public TestSessionBean createSessionBeanForUpdate(TrsSessionRecord sessionRecord, Map<String, TestExecution> resultMapping) {
        Map<String, Object> dropAndIsoData = null;
        List<TestCaseResultBean> results = Lists.newArrayList();

        for (Map.Entry<String, TestExecution> entry : resultMapping.entrySet()) {
            String trsResultId = entry.getKey();
            TestExecution execution = entry.getValue();
            TestCaseResultBean resultBean = createResultBean(Optional.of(trsResultId), execution);
            results.add(resultBean);

            if (dropAndIsoData == null) {
                dropAndIsoData = getDropAndIsoData(execution);
            }
        }

        TestSuiteResultBean suiteResultBean = createSuiteBean(results);

        return new TestSessionBeanBuilder()
                .withExecutionId(sessionRecord.getExecutionId())
                .withExecutionTime(createExecutionTimeBean())
                .withTestSuites(Lists.newArrayList(suiteResultBean))
                .withPassRate(calculatePassRate(suiteResultBean.getStatistics()))
                .withAdditionalFields(dropAndIsoData)
                .build();
    }

    public JobBean createJobBean(String context, TestCampaign testCampaign, List<TestExecution> testExecutions) {
        TestSessionBean testSessionBean = createSessionBean(testCampaign, testExecutions);
        return new JobBeanBuilder()
                .withContext(context)
                .withSessions(Lists.newArrayList(testSessionBean))
                .build();
    }

    private ExecutionTimeBean createExecutionTimeBean() {
        return new ExecutionTimeBeanBuilder()
                .withStartDate(new Date(System.currentTimeMillis() - 5000))
                .withStopDate(new Date(System.currentTimeMillis()))
                .build();
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private String getIsoDetails(TestExecution testExecution) {
        ISO iso = testExecution.getIso();
        return iso.getName() + "_" + iso.getVersion() + "_" + generateUuid();
    }

    private int calculatePassRate(StatisticsBean statisticsBean) {
        return Math.round((float)statisticsBean.getPassed() / statisticsBean.getTotal());
    }

    private Map<String, Object> getDropAndIsoData(TestExecution testExecution) {
        return ImmutableMap.<String, Object>builder()
                .put(DROP_NAME, testExecution.getTestCampaign().getDrop().getName())
                .put(ISO_VERSION, testExecution.getIso().getVersion())
                .build();
    }

    private Map<String, Object> getTestCaseData(TestCaseVersion testCase) {
        String priority = testCase.getPriority().name();
        
        List<String> componentNames = testCase.getTechnicalComponents()
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        List<String> requirementNames = testCase.getRequirements().stream()
                .map(r -> r.getExternalId())
                .collect(Collectors.toList());

        List<String> scopeNames = testCase.getScopes().stream()
                .map(s -> s.getName())
                .collect(Collectors.toList());

        return ImmutableMap.<String, Object>builder()
                .put(PRIORITY, priority)
                .put(COMPONENTS, componentNames)
                .put(GROUPS, scopeNames)
                .put(REQUIREMENTS, requirementNames)
                .build();
    }
}
