package com.ericsson.cifwk.tm.integration.jira.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.infrastructure.GuiceTestRunner;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.mapping.MetadataMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(GuiceTestRunner.class)
public class DefectManagementImplTest extends BaseServiceLayerTest {

    private static final String TORF = "TORF";

    @Inject
    private DefectManagement defectManagement;

    @Inject
    private MetadataMapper metadataMapper;

    @Test
    public void getDefectMetaData() {
        DefectMetadata metadata = defectManagement.getDefectMetadata(TORF);
        List<ReferenceData> mapped = metadataMapper.mapFieldMetadata(metadata.getFields());
        assertThat(mapped, hasSize(6));
    }

}
