package com.ericsson.cifwk.tm.application.services.scheduling;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.application.services.TrsResendService;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TrsResendServiceITest extends BaseServiceLayerTest {

    @Inject
    private TrsResendService trsResendService;

    @Inject
    private TestExecutionRepository executionRepository;

    @Before
    public void setUp() {
        persistence.cleanupTables();
        persistEntities();
    }

    @Test
    public void checkForUnsentExecutionsAndRecordInTrs() {
        List<TestExecution> unrecordedExecutions = executionRepository.findUnrecordedExecutions();
        assertThat(unrecordedExecutions.size(), equalTo(1));

        trsResendService.recordUnsentExecutions();
        unrecordedExecutions = executionRepository.findUnrecordedExecutions();
        assertThat(unrecordedExecutions.size(), equalTo(0));
    }

    private void persistEntities() {
        Product product = fixture().persistProduct("ENM");
        ProductFeature feature = fixture().persistFeature("FM", product);
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature(feature);
        Drop drop = fixture().persistDrop(product, "16.13");
        testCampaign.setDrop(drop);
        persistence.persistInTransaction(testCampaign);
        TestCase testCase = fixture().persistTestCase(UUID.randomUUID().toString());
        ISO iso = fixture().persistIso("CXP123", "1.2.13");
        fixture().persistTestExecution(testCampaign, testCase.getCurrentVersion(), iso);
    }
}
