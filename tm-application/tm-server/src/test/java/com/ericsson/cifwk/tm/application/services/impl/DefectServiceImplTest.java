/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefectType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildDefect;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefectServiceImplTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectManagement defectManagement;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private DefectServiceImpl defectService = new DefectServiceImpl();

    @Test
    public void testGetByExternalId_WhenNoDefectIds_ShouldReturnEmptySet() {
        assertTrue(defectService.findAll(Collections.EMPTY_SET).isEmpty());
    }

    @Test
    public void testGetByExternalId_WhenDefectIdExistsInRepository_ShouldReturnFetchedFromRepo() {
        String externalId = "TAF-1";
        Defect defect = createDefectWithExternalId(externalId);

        when(defectRepository.findAllByExternalId(Sets.newHashSet(externalId))).thenReturn(Lists.newArrayList(defect));

        List<Defect> defects = defectService.findAll(Sets.newHashSet(externalId));

        assertThat(defects.size(), is(1));
        assertThat(defects, hasItem(defect));
    }


    @Test
    public void testGetByExternalId_WhenDefectsAreMissingInLocalRepo_ShouldFetchDefectManagementSystem() {
        String externalId = "TAF-1";

        Defect defect = createDefectWithExternalId(externalId);
        HashSet<String> externalIds = Sets.newHashSet(externalId);

        when(defectRepository.findAllByExternalId(externalIds)).thenReturn(Lists.<Defect>newArrayList());
        when(defectManagement.fetchById(externalId)).thenReturn(
                ExternalDefect.builder(defect.getExternalId(), ExternalDefectType.BUG)
                        .summary(defect.getExternalSummary())
                        .title(defect.getExternalTitle())
                        .build());
        when(projectRepository.findByExternalId(anyString())).thenReturn(null);

        List<Defect> defects = defectService.findAll(externalIds);

        assertThat(defects.size(), is(1));
        assertThat(defects, hasItem(defect));

    }

    private Defect createDefectWithExternalId(String externalId) {
        return buildDefect()
                .withExternalId(externalId)
                .build();
    }
}
