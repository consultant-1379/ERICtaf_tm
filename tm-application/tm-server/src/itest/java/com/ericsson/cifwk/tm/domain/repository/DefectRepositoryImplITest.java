/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.google.common.collect.Sets;
import com.googlecode.genericdao.search.Search;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class DefectRepositoryImplITest extends BaseServiceLayerTest {

    @Inject
    private DefectRepository defectRepository;

    @Test
    public void testSearch() {
        Project project = fixture().persistProject("ProjectId", "ProjectName");
        Defect defect = fixture().persistDefect(project);

        Search search = new Search();
        search.addFilterEqual("externalId", defect.getExternalId());
        Defect persistedDefect = defectRepository.searchUnique(search);

        assertThat(persistedDefect.getExternalId(), equalTo(defect.getExternalId()));
        assertThat(persistedDefect.getExternalSummary(), equalTo(defect.getExternalSummary()));
        assertThat(persistedDefect.getExternalTitle(), equalTo(defect.getExternalTitle()));
        assertThat(persistedDefect.getProject().getExternalId(), equalTo(project.getExternalId()));
    }

    @Test
    public void testFindByExternalId_WhenEntityExist_ShouldReturnIt() {
        Project project = fixture().persistProject("ProjectId", "ProjectName");
        Defect defect = fixture().persistDefect(project);

        Defect persistedDefect = defectRepository.findByExternalId(defect.getExternalId());

        assertThat(persistedDefect.getExternalId(), equalTo(defect.getExternalId()));
        assertThat(persistedDefect.getExternalSummary(), equalTo(defect.getExternalSummary()));
        assertThat(persistedDefect.getExternalTitle(), equalTo(defect.getExternalTitle()));
        assertThat(persistedDefect.getProject().getExternalId(), equalTo(project.getExternalId()));
    }

    @Test
    public void testFindByExternalId_WhenEntityNotExist_ShouldReturnNull() {
        Defect empty = defectRepository.findByExternalId("null");

        assertThat(empty, nullValue());
    }

    @Test
    public void testFindAllByExternalId_WhenEntityExist_ShouldReturnAll() {
        fixture().persistDefect("TMS-1");
        fixture().persistDefect("TMS-2");
        fixture().persistDefect("TMS-3");

        List<Defect> allByExternalId = defectRepository.findAllByExternalId(Sets.newHashSet("TMS-1", "TMS-2", "TMS-3"));

        assertThat(allByExternalId.size(), is(3));
    }

    @Test
    public void testFindAllByExternalId_WhenNotAllEntitiesExist_ShouldReturnAllExisting() {
        fixture().persistDefect("TMS-1");
        fixture().persistDefect("TMS-2");

        List<Defect> allByExternalId = defectRepository.findAllByExternalId(Sets.newHashSet("TMS-1", "TMS-2", "TMS-3"));

        assertThat(allByExternalId.size(), is(2));
    }

    @Test
    public void testFindMatchingId_WhenNotAllEntitiesExist_ShouldReturnAllExisting() {
        fixture().persistDefect("TMS-1");
        fixture().persistDefect("TMS-2");
        fixture().persistDefect("DURACI-2960");

        List<Defect> matchingIdEntities = defectRepository.findMatchingExternalId("TMS", 20);

        assertThat(matchingIdEntities.size(), is(2));
    }
}
