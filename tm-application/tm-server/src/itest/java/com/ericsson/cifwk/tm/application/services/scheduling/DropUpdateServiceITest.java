package com.ericsson.cifwk.tm.application.services.scheduling;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.application.services.DropUpdateService;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DropUpdateServiceITest extends BaseServiceLayerTest {

    @Inject
    private DropUpdateService dropUpdateService;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private TestCampaignRepository testCampaignRepository;

    private Product enm;

    @Before
    public void setUp() {
        persistence.cleanupTables();
    }

    @Test
    public void shouldUpdateDrops() {
        persistEntities();
        List<Drop> enmDrops = dropRepository.findByProduct(enm.getId());
        assertThat(enmDrops, hasSize(2));

        List<TestCampaign> allTestCampaigns = testCampaignRepository.findAll();
        assertThat(allTestCampaigns.size(), equalTo(1));

        dropUpdateService.updateDrops();
        enmDrops = dropRepository.findByProduct(enm.getId());
        assertThat(enmDrops.size(), equalTo(3));

        List<String> dropNames = enmDrops.stream()
                .map(d -> d.getName())
                .collect(Collectors.toList());

        assertThat(dropNames, containsInAnyOrder("16.7", "16.8", "16.9"));

        allTestCampaigns = testCampaignRepository.findAll();
        assertThat(allTestCampaigns.size(), equalTo(2));
        allTestCampaigns.forEach(t -> assertThat(t.getName(), equalTo("Test Campaign")));

        List<String> testCampaignDropNames = allTestCampaigns.stream()
                .map(t -> t.getDrop().getName())
                .collect(Collectors.toList());

        assertThat(testCampaignDropNames, containsInAnyOrder("16.8", "16.9"));
    }

    private void persistEntities() {
        enm = TestEntityFactory.buildProduct("ENM").build();
        enm.setDropCapable(true);
        Drop enmDrop1 = TestEntityFactory.buildDrop(enm, "16.7").build();
        Drop enmDrop2 = TestEntityFactory.buildDrop(enm, "16.8").build();
        ProductFeature fm = TestEntityFactory.buildFeature("FM", enm, Sets.newHashSet()).build();
        persistence.persistInTransaction(enm, enmDrop1, enmDrop2, fm);
        TestCampaign testCampaign = TestEntityFactory.buildTestPlan("Test Campaign", enmDrop2, fm, Sets.newHashSet()).build();
        testCampaign.setAutoCreate(true);
        persistence.persistInTransaction(testCampaign);
    }
}
