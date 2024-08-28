package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RequirementRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    RequirementRepository repository;

    @Test
    public void shouldFindTopLevelRequirementsByProject() {
        Project project = fixture().persistProject("TAF", "Taf");
        fixture().persistRequirement("R1", project);
        fixture().persistRequirement("R2", project);
        fixture().persistRequirement("R3", project);

        Project project2 = fixture().persistProject("CIP", "Cip");
        fixture().persistRequirement("R4", project2);

        List<Requirement> requirements1 = repository.findTopLevel("TAF");
        List<Requirement> requirements2 = repository.findTopLevel("CIP");
        List<Requirement> allRequirements = repository.findTopLevel(null);

        assertThat(requirements1, hasSize(3));
        assertThat(requirements2, hasSize(1));
        assertThat(allRequirements, hasSize(4));
    }

    @Test
    public void testGetAllJiraExternalId() {
        final String ID1 = "id1";
        final String ID2 = "id2";
        final String ID3 = "id3";

        Requirement requirement1 = new Requirement(ID1);

        Requirement requirement2 = new Requirement(ID2);
        requirement1.addChild(requirement1);

        Requirement requirement3 = new Requirement(ID3);

        persistence.persistInTransaction(requirement1, requirement2, requirement3);

        List<String> allJiraExternalIds = repository.getAllJiraExternalIds();

        assertThat(allJiraExternalIds, containsInAnyOrder(ID1, ID2, ID3));

        //Child requirements should go after parent to optimize query to Jira
        assertThat(allJiraExternalIds.get(2), Matchers.equalTo(ID1));
    }

}
