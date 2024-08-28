package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.TrsResendService;
import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.google.common.collect.Lists;
import com.google.inject.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrsResendServiceImpl implements TrsResendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrsResendServiceImpl.class);

    @Inject
    private TrsService trsService;

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Override
    public void recordUnsentExecutions() {
        List<TestExecution> executions = testExecutionRepository.findUnrecordedExecutions();
        if (executions.isEmpty()) {
            return;
        }

        Map<TestCampaign, Map<ISO, List<TestExecution>>> groupedByTestCampaignAndIso = executions.stream()
                .collect(
                        Collectors.groupingBy(e -> e.getTestCampaign(), Collectors.groupingBy(e -> e.getIso()))
                );

        groupedByTestCampaignAndIso.entrySet().forEach(testCampaignEntry -> {
            TestCampaign testCampaign = testCampaignEntry.getKey();
            Map<ISO, List<TestExecution>> groupedByIso = testCampaignEntry.getValue();

            groupedByIso.entrySet().forEach(isoEntry -> {
                List<TestExecution> testExecutionsByIso = isoEntry.getValue();

                Map<TestCase, List<TestExecution>> groupedByTestCase = testExecutionsByIso.stream()
                        .collect(
                                Collectors.groupingBy(e -> e.getTestCaseVersion().getTestCase())
                        );

                List<TestExecution> executionsByTestCampaignAndIso = Lists.newArrayList();

                groupedByTestCase.entrySet().forEach(testCaseEntry -> {
                    Comparator<TestExecution> byDate = Comparator.comparing(TestExecution::getCreatedAt);

                    List<TestExecution> executionsOfTestCase = testCaseEntry.getValue();
                    Collections.sort(executionsOfTestCase, byDate.reversed());

                    TestExecution latestUnrecordedExecution = executionsOfTestCase.get(0);

                    if (testExecutionRepository.isLatestForTestCaseAndIso(latestUnrecordedExecution)) {
                        executionsByTestCampaignAndIso.add(latestUnrecordedExecution);
                        executionsOfTestCase.remove(latestUnrecordedExecution);
                    }
                    // mark older executions of test case as recorded in TRS as they will never be sent -
                    // newer executions of the same test case make these obsolete
                    markAsRecorded(executionsOfTestCase);
                });

                // record latest executions of test cases by test campaign and iso
                try {
                    if (!executionsByTestCampaignAndIso.isEmpty()) {
                        Optional<TrsRecord> record = recordExecutions(testCampaign, executionsByTestCampaignAndIso);
                        if (record.isPresent()) {
                            markAsRecorded(executionsByTestCampaignAndIso);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Could not record Test Execution in TRS", e);
                }

            });
        });
        LOGGER.info("Exiting recordUnsentExecutions");
    }

    @Transactional
    public Optional<TrsRecord> recordExecutions(TestCampaign testCampaign, List<TestExecution> testExecutions) {
        TestExecution[] executionsArray = new TestExecution[testExecutions.size()];
        executionsArray = testExecutions.toArray(executionsArray);
        return trsService.recordExecutions(testCampaign, executionsArray);
    }

    @Transactional
    public void markAsRecorded(List<TestExecution> testExecutions) {
        testExecutions.forEach(e -> e.setRecordedInTrs(true));
        testExecutionRepository.save(testExecutions);
    }
}
