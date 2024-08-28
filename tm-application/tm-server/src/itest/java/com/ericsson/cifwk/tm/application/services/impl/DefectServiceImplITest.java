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

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.infrastructure.stubs.JiraDefectManagementStub;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefectType;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefectServiceImplITest extends BaseServiceLayerTest {

    @Inject
    private DefectServiceImpl defectService;

    @Inject
    private DefectRepository defectRepository;

    @Before
    public void setUp() {
        persistence.cleanupTables();
    }

    @Test
    public void testGetByExternalId_WhenDefectExist_ShouldReturnDefectWithCorrectExternalId() {
        Defect defect = new Defect("TMS-1");
        defect.setExternalTitle("Get this from repository");
        persistence.persistInTransaction(defect);

        List<Defect> foundDefects = defectService.findAll(Sets.newHashSet("TMS-1"));

        assertThat(foundDefects.size(), is(1));
        assertThat(foundDefects, hasItem(defect));
    }

    @Test
    public void testGetByExternalId_WhenNoDefectExist_ShouldFindInJiraAndCreate() {

        List<Defect> foundDefects = defectService.findAll(Sets.newHashSet("TMS-1", "TOR-193"));

        assertThat(foundDefects.size(), is(1));
        Defect defectFromJira = foundDefects.get(0);

        assertThat(defectFromJira.getExternalId(), is(JiraDefectManagementStub.BUG_2));
        assertThat(defectFromJira.getExternalTitle(), is(JiraDefectManagementStub.BUG_2));
        assertThat(defectFromJira.getExternalSummary(), is(JiraDefectManagementStub.BUG_2_DESCRIPTION));
    }


    @Test
    public void testFetchUpdatedRequirements_ShouldPersistNewRequirementsInDatabase() {
        List<Defect> emptyDefectList = defectRepository.findAll();
        assertThat(emptyDefectList.isEmpty(), is(true));

        defectService.fetchAndSaveUpdatedDefects();

        List<Defect> all = defectRepository.findAll();

        assertThat(all.size(), is(5));
    }

    @Test
    public void testSaveOrUpdate_WhenEntityNotExist_ShouldCreateNew() {
        ExternalDefect externalDefect = ExternalDefect.builder("TMS-2", ExternalDefectType.BUG).summary("Create new").title("New title").build();

        Defect createdDefect = defectService.saveOrUpdate("TMS-2", externalDefect);

        assertThat(createdDefect, notNullValue());
        assertThat(createdDefect.getExternalId(), is(externalDefect.getId()));
        assertThat(createdDefect.getExternalTitle(), is(externalDefect.getTitle()));
        assertThat(createdDefect.getExternalSummary(), is(externalDefect.getSummary()));
    }


    @Test
    public void testSaveOrUpdate_WhenEntityExists_ShouldUpdateIt() {
        Defect defect = new Defect("TMS-3");
        defect.setExternalTitle("Title should be updated");
        defect.setExternalSummary("Summary should be updated");
        persistence.persistInTransaction(defect);

        ExternalDefect externalDefect = ExternalDefect.builder("TMS-3", ExternalDefectType.BUG)
                .summary("Summary is Updated")
                .title("Title is Updated")
                .build();

        Defect updatedDefect = defectService.saveOrUpdate("TMS-3", externalDefect);

        assertThat(updatedDefect, notNullValue());
        assertThat(updatedDefect.getExternalId(), is(externalDefect.getId()));
        assertThat(updatedDefect.getExternalTitle(), is(externalDefect.getTitle()));
        assertThat(updatedDefect.getExternalSummary(), is(externalDefect.getSummary()));
    }

    @Test
    public void testFindOrCreate_WhenEntityExists_ShouldReturnIt() {
        Defect defect = new Defect("TMS-5");
        persistence.persistInTransaction(defect);

        Defect existingDefect = defectService.findOrCreateDefect("TMS-5");

        assertThat(existingDefect.getId(), is(defect.getId()));
    }

    @Test
    public void testFindOrCreate_WhenEntityNotExists_ShouldCreateNewAndSaveIt() {
        Defect existingDefect = defectService.findOrCreateDefect("TMS-5");

        assertThat(existingDefect.getId(), notNullValue());
        assertThat(existingDefect.getExternalId(), is("TMS-5"));
        assertThat(existingDefect.getExternalTitle(), nullValue());
        assertThat(existingDefect.getExternalSummary(), nullValue());
    }
}
