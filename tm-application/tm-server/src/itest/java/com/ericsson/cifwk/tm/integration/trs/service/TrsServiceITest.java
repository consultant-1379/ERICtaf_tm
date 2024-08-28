package com.ericsson.cifwk.tm.integration.trs.service;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecordRepository;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class TrsServiceITest extends BaseServiceLayerTest {

    @Inject
    private TrsService trsService;

    @Inject
    private TrsJobRecordRepository jobRecordRepository;

    private TestCampaign testCampaign;
    private TestCase testCase1;
    private TestCase testCase2;
    private TestCase testCase3;
    private TestExecution[] testExecutions = new TestExecution[2];
    private ISO iso;

    @Before
    public void setUp() {
        persistence().cleanupTables();
        testCampaign = fixture().persistTestCampaignWithFeature("TMS_JOB", "ENM", "FM");
        Product product = testCampaign.getFeatures().stream().findFirst().get().getProduct();
        Drop drop = fixture().persistDrop(product, "16.11");
        testCampaign.setDrop(drop);
        persistence().persistInTransaction(testCampaign);

        iso = fixture().persistIso("CXP_123", "1.0.1");
        testCase1 = fixture().persistTestCase("Test Case 1");
        testCase2 = fixture().persistTestCase("Test Case 2");
        testCase3 = fixture().persistTestCase("Test Case 3");

        User user = fixture().persistUser();
        TestCampaignItem testCampaignItem1 = fixture().persistTestPlanItem(user, testCampaign, testCase1.getCurrentVersion());
        TestCampaignItem testCampaignItem2 = fixture().persistTestPlanItem(user, testCampaign, testCase2.getCurrentVersion());
        TestCampaignItem testCampaignItem3 = fixture().persistTestPlanItem(user, testCampaign, testCase3.getCurrentVersion());

        testCampaign.addTestCampaignItem(testCampaignItem1);
        testCampaign.addTestCampaignItem(testCampaignItem2);
        testCampaign.addTestCampaignItem(testCampaignItem3);

        testExecutions[0] = fixture().persistTestExecution(testCampaign, testCase1.getCurrentVersion(), iso);
        testExecutions[1] = fixture().persistTestExecution(testCampaign, testCase2.getCurrentVersion(), iso);
    }

    @Test
    public void testRecordingOfExecutions() {
        trsService.recordExecutions(testCampaign, testExecutions);

        TrsJobRecord jobRecord = jobRecordRepository.findJobByName(testCampaign.getName()).get();
        assertThat(jobRecord, notNullValue());
        assertThat(jobRecord.getJobName(), equalTo(testCampaign.getName()));
        assertThat(jobRecord.getJobId(), notNullValue());

        List<TrsSessionRecord> sessionRecords = jobRecord.getSessions();
        assertThat(sessionRecords.size(), equalTo(1));
        TrsSessionRecord firstSessionRecord = jobRecord.getSessionByIso(iso);
        assertThat(firstSessionRecord.getJob(), equalTo(jobRecord));
        assertTrue(firstSessionRecord.getExecutionId().startsWith(iso.getName() + "_" + iso.getVersion()));

        Set<TrsResultRecord> resultRecords = firstSessionRecord.getResults();
        assertThat(resultRecords.size(), equalTo(3));
        resultRecords.forEach(r -> assertThat(r.getTrsResultId(), notNullValue()));
        resultRecords.forEach(r -> assertThat(r.getSession(), equalTo(firstSessionRecord)));
        List<TestCase> testCases = resultRecords.stream()
                .map(r -> r.getTestCase())
                .collect(Collectors.toList());

        assertTrue(testCases.containsAll(Lists.newArrayList(testCase1, testCase2, testCase3)));
    }

}
