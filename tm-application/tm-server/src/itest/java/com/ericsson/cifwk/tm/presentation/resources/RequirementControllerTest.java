/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.CompletionItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.TestRequirements.EPIC1;
import static com.ericsson.cifwk.tm.test.TestRequirements.EPIC2;
import static com.ericsson.cifwk.tm.test.TestRequirements.STORY1;
import static com.ericsson.cifwk.tm.test.TestRequirements.SUBTASK1;
import static com.google.common.collect.Iterables.getLast;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class RequirementControllerTest extends BaseControllerLevelTest {

    private static final GenericType<List<RequirementInfo>> REQUIREMENT_INFO_LIST =
            new GenericType<List<RequirementInfo>>() {
            };

    private static final String PROJECT1 = "Project1";

    @Test
    public void shouldGetEmptyRequirements() {
        Project project = fixture().persistProject(PROJECT1);
        app.persistence().persistInTransaction(project);

        Response response = app.client().path("/tm-server/api/requirements/")
                .queryParam("projectId", PROJECT1)
                .request().get();

        assertStatus(response, Response.Status.OK);

        List<RequirementInfo> infos = response.readEntity(REQUIREMENT_INFO_LIST);
        response.close();

        assertThat(infos.size(), equalTo(0));
    }

    @Test
    public void shouldGetRequirements() {
        saveDefaultRequirements();

        List<RequirementInfo> infos = app
                .client().path("/tm-server/api/requirements")
                .queryParam("projectId", PROJECT1)
                .queryParam("view", "tree")
                .request().get(REQUIREMENT_INFO_LIST);

        assertThat(infos, hasSize(2));

        RequirementInfo epic2 = infos.get(0);
        assertThat(epic2.getExternalId(), equalTo(EPIC2));
        assertThat(epic2.getChildren(), empty());

        RequirementInfo epic1 = infos.get(1);
        assertThat(epic1.getChildren(), hasSize(1));
        RequirementInfo story = getLast(epic1.getChildren());
        assertThat(story.getExternalId(), equalTo(STORY1));
        assertThat(story.getChildren(), hasSize(1));
        assertThat(story.getDeliveredIn(), equalTo("15.14"));
        RequirementInfo subtask = getLast(story.getChildren());
        assertThat(subtask.getExternalId(), equalTo(SUBTASK1));
        assertThat(subtask.getChildren(), empty());
    }

    @Test
    public void shouldIgnoreDeletedRequirements() {
        Requirement requirement = saveDefaultRequirements();
        requirement.delete();
        app.persistence().persistInTransaction(requirement);

        List<RequirementInfo> infos = app
                .client().path("/tm-server/api/requirements")
                .queryParam("projectId", PROJECT1)
                .queryParam("view", "tree")
                .request().get(REQUIREMENT_INFO_LIST);

        assertThat(infos, hasSize(2));
        assertThat(infos.get(1).getChildren(), hasSize(0));
    }

    @Test
    public void shouldGetUnknownRequirement() {
        Response response = app.client()
                .path("/tm-server/api/requirements/NONEXISTING-000")
                .request().get();

        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void shouldFilterRequirementTreeByProject() {
        Requirement story1 = saveDefaultRequirements();
        TestCaseVersion testCaseVersion = saveDefaultTestCase(story1);

        String project2Id = "Project2";
        Project project2 = fixture().persistProject(project2Id);
        String epic2Id = "Epic-Project2";
        Requirement epic2 = createRequirement(epic2Id, project2);
        Requirement story2 = createRequirement("Story-Project2", project2);
        epic2.addChild(story2);
        testCaseVersion.addRequirement(story2);
        app.persistence().persistInTransaction(project2, epic2, story2, testCaseVersion);

        // Query with default project (in all projects)
        Response responseForAll = app.client()
                .path("/tm-server/api/requirements")
                .queryParam("projectId", PROJECT1)
                .queryParam("view", "tree")
                .request().get();

        assertStatus(responseForAll, Response.Status.OK);

        List<RequirementInfo> allInfos = responseForAll.readEntity(REQUIREMENT_INFO_LIST);
        responseForAll.close();

        assertThat(allInfos, hasSize(2));
        assertThat(allInfos, Matchers.<RequirementInfo>hasItems(
                hasProperty("externalId", equalTo(EPIC1)),
                hasProperty("externalId", equalTo(EPIC2))
        ));

        // Query with project
        Response responseForProject = app.client()
                .path("/tm-server/api/requirements")
                .queryParam("projectId", project2Id)
                .queryParam("view", "tree")
                .request().get();

        assertStatus(responseForProject, Response.Status.OK);

        List<RequirementInfo> projectInfos = responseForProject.readEntity(REQUIREMENT_INFO_LIST);
        responseForProject.close();

        assertThat(projectInfos, hasSize(1));
        assertThat(projectInfos.get(0).getExternalId(), equalTo(epic2Id));
    }

    @Test
    public void shouldGetRequirementTestCases() {
        Requirement requirement = saveDefaultRequirements();
        String requirementId = requirement.getExternalId();
        TestCaseVersion testCaseVersion = saveDefaultTestCase(requirement);

        Response response = app.client()
                .path("/tm-server/api/requirements/" + requirementId)
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);

        RequirementInfo requirementInfo = response.readEntity(RequirementInfo.class);
        response.close();
        Set<TestCaseInfo> testCases = requirementInfo.getTestCases();

        assertThat(testCases.size(), equalTo(1));

        TestCaseInfo firstTestCase = testCases.iterator().next();

        assertThat(firstTestCase.getTestCaseId(), equalTo(testCaseVersion.getTestCase().getTestCaseId()));
    }

    @Test
    public void shouldAutoCompleteRequirements() {
        saveDefaultRequirements();

        assertCompletion(getCompletion("CIP-4", 5), Arrays.asList(STORY1, SUBTASK1));
        assertCompletion(getCompletion("CIP-*3", 5), Arrays.asList(SUBTASK1, EPIC1));
        assertCompletion(getCompletion("CIP-", 2), Arrays.asList(EPIC2, STORY1));
        assertCompletion(getCompletion("", 2), Collections.<String>emptyList());
        assertThat(getCompletion("CIP", 0).getStatus(), equalTo(400));
    }

    private Response getCompletion(String search, int limit) {
        return app.client()
                .path("/tm-server/api/requirements/completion")
                .queryParam("search", search)
                .queryParam("limit", limit)
                .request()
                .get();
    }

    private void assertCompletion(Response response, List<String> expected) {
        assertStatus(response, Response.Status.OK);
        CompletionInfo info = response.readEntity(CompletionInfo.class);
        List<String> values = Lists.newArrayList();
        for (CompletionItemInfo item : info.getItems()) {
            values.add(item.getValue());
        }
        assertThat(values, equalTo(expected));
    }

    private Requirement saveDefaultRequirements() {
        Project project = fixture().persistProject(PROJECT1);
        Requirement epic1 = createRequirement(EPIC1, project);
        Requirement userStory1 = createRequirement(STORY1, project);
        epic1.addChild(userStory1);
        Requirement subTask = createRequirement(SUBTASK1, project);
        userStory1.addChild(subTask);
        Requirement epic2 = createRequirement(EPIC2, project);

        app.persistence().persistInTransaction(project, epic1, userStory1, subTask, epic2);

        return userStory1;
    }

    private TestCaseVersion saveDefaultTestCase(Requirement requirement) {

        ProductFeature productFeature = new ProductFeature();
        Product product = requirement.getProject().getProduct();
        productFeature.setProduct(product);
        productFeature.setName(UUID.randomUUID().toString());

        TestType testType = new TestType();
        testType.setProduct(product);
        testType.setName("Functional");

        app.persistence().persistInTransaction(productFeature, testType);

        TestCase testCase = TestCaseFactory.createTestCase();
        TestCaseVersion testCaseVersion1 = testCase.getCurrentVersion();
        testCase.setTestCaseId(STORY1 + "_Func_1");
        testCaseVersion1.setTitle("Default title");
        testCaseVersion1.setType(testType);
        testCaseVersion1.setTestCaseStatus(TestCaseStatus.PRELIMINARY);

        testCaseVersion1.setProductFeature(productFeature);

        saveTestCase(requirement, testCase);
        TestCaseVersion testCaseVersion2 = testCase.addNewMinorVersion();
        testCaseVersion2.setTitle("Default title");
        testCaseVersion2.setProductFeature(productFeature);
        return saveTestCase(requirement, testCase);
    }

    private TestCaseVersion saveTestCase(Requirement requirement, TestCase testCase) {
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        testCaseVersion.setPriority(Priority.BLOCKER);
        testCaseVersion.addRequirement(requirement);
        testCaseVersion.setAutomationCandidate(AutomationCandidate.NO);
        app.persistence().persistInTransaction(testCase);
        return testCaseVersion;
    }

    private Requirement createRequirement(String id, Project project) {
        Requirement requirement = new Requirement(id);
        requirement.setExternalType("Story");
        requirement.setExternalLabel(id);
        requirement.setProject(project);
        requirement.setDeliveredIn("15.14");
        return requirement;
    }

}
