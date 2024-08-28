package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecordRepository;
import org.junit.Test;

import javax.inject.Inject;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrsJobRecordRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private TrsJobRecordRepository jobRepository;

    @Test
    public void shouldGetJobsByName() {
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");
        TrsJobRecord job = fixture().persistTvsJob(testCampaign);
        TrsJobRecord foundJob = jobRepository.findJobByName(testCampaign.getName()).get();

        assertThat(foundJob, equalTo(job));
    }
}
