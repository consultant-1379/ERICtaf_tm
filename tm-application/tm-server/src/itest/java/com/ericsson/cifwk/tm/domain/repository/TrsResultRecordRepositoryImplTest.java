package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecordRepository;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.google.common.collect.Lists;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TrsResultRecordRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private TrsResultRecordRepository trsResultRecordRepository;

    @Test
    public void shouldRetrieveResultsBySession() {
        TestCase testCase1 = fixture().persistTestCase(UUID.randomUUID().toString());
        TestCase testCase2 = fixture().persistTestCase(UUID.randomUUID().toString());
        TrsSessionRecord session = persistSession();
        TrsResultRecord result1 = persistResult(testCase1, session);
        TrsResultRecord result2 = persistResult(testCase2, session);
        List<TrsResultRecord> savedResults = trsResultRecordRepository.findResultsBySession(session.getExecutionId());

        assertThat(savedResults.size(), equalTo(2));
        assertTrue(savedResults.containsAll(Lists.newArrayList(result1, result2)));
    }

    @Test
    public void shouldRetrievResultByTestCaseAndSession() {
        TestCase testCase = fixture().persistTestCase(UUID.randomUUID().toString());
        TrsSessionRecord session = persistSession();
        TrsResultRecord savedResult = persistResult(testCase, session);

        TrsResultRecord retrievedResult = trsResultRecordRepository.findResultBySessionAndTestCase(session.getExecutionId(), testCase.getId()).get();
        assertThat(retrievedResult.getId(), equalTo(savedResult.getId()));
    }

    public TrsSessionRecord persistSession() {
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");
        TrsJobRecord job = fixture().persistTvsJob(testCampaign);
        ISO iso = fixture().persistIso("ENM_ISO", "1.11.2");
        return fixture().persistTvsSession(iso, job);
    }

    public TrsResultRecord persistResult(TestCase testCase, TrsSessionRecord session) {
        return fixture().persistTvsResult(testCase, session);
    }

}
