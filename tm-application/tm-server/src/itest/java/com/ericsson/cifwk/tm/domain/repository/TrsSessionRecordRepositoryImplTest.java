package com.ericsson.cifwk.tm.domain.repository;


import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecordRepository;
import org.junit.Test;

import javax.inject.Inject;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrsSessionRecordRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private TrsSessionRecordRepository trsSessionRecordRepository;

    private TrsJobRecord trsJobRecord;

    private ISO iso;

    @Test
    public void shouldRetreiveSessionByJobAndIso() {
        TrsSessionRecord savedSession = persistSession();

        TrsSessionRecord retrievedSession =
                trsSessionRecordRepository.findSessionByJobAndIso(trsJobRecord.getJobId(), iso.getId()).get();

        assertThat(retrievedSession.getId(), equalTo(savedSession.getId()));
        assertThat(retrievedSession.getIso(), equalTo(iso));
        assertThat(retrievedSession.getJob(), equalTo(trsJobRecord));
    }

    public TrsSessionRecord persistSession() {
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");
        trsJobRecord = fixture().persistTvsJob(testCampaign);
        iso = fixture().persistIso("ENM_ISO", "1.11.2");
        return fixture().persistTvsSession(iso, trsJobRecord);
    }
}
