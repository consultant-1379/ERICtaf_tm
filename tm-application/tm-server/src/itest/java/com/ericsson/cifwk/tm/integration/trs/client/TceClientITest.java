package com.ericsson.cifwk.tm.integration.trs.client;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.tce.TceClient;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class TceClientITest extends BaseServiceLayerTest {

    @Inject
    private TceClient tceClient;

    @Test
    public void testGetContexts() {
        String apTribeUuid = tceClient.getContextIdByName("AP Tribe");
        assertThat(apTribeUuid, notNullValue());
    }
}
