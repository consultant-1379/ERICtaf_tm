package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ProjectRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private ProjectRepository projectRepository;

    @Test
    public void testGetAllJiraProjects() {
        Product product1 = fixture().persistProduct("product1");
        Project project1 = fixture().persistProject("project1", "projectName1", product1);
        Project project2 = fixture().persistProject("project2", "projectName2", product1);

        Map<Long, String> allProjects = projectRepository.findAllIdsToExternalIds();
        assertEquals(2, allProjects.size());

        assertEquals(project1.getExternalId(), allProjects.get(project1.getId()));
        assertEquals(project2.getExternalId(), allProjects.get(project2.getId()));
    }

}
