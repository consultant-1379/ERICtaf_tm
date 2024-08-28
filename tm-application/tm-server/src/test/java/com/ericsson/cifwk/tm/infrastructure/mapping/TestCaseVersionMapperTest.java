/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.management.impl.TestCampaignRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.FieldType;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestCaseVersionRepositoryImpl;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class TestCaseVersionMapperTest {

    @InjectMocks
    private TestCaseVersionMapper testCaseVersionMapper;

    private TestCampaignRepository testCampaignRepository = new TestCampaignRepositoryImpl();

    private TestCaseVersionRepository testCaseVersionRepository = new TestCaseVersionRepositoryImpl();

    @Before
    public void setUp() {
        ProjectMapper projectMapper = new ProjectMapper(new ProductMapper());
        testCampaignRepository = spy(testCampaignRepository);

        TestCampaignMapper testCampaignMapper = new TestCampaignMapper(
                new ProjectMapper(new ProductMapper()),
                new TestCampaignViewFactory(),
                new TestExecutionResultMapper(),
                new DefectMapper(projectMapper),
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                testCampaignRepository,
                null,
                new DropMapper(new ProductMapper()),
                new ProductMapper(),
                new FeatureMapper(new ProductMapper()),
                new UserMapper(),
                new ReviewGroupMapper()
        );

        testCaseVersionMapper = new TestCaseVersionMapper(
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                testCampaignRepository,
                testCampaignMapper,
                new ProjectMapper(new ProductMapper()),
                new FeatureMapper(new ProductMapper()),
                new ReviewGroupMapper(),
                new UserMapper(),
                testCaseVersionRepository
        );
    }

    @Test
    public void testNullArgument() {
        TestCaseInfo info = testCaseVersionMapper.mapEntity(null, TestCaseInfo.class);

        assertThat(info, nullValue());
    }

    @Test
    public void testSimpleEntity() {
        TestCase testCase = buildTestCase()
                .withId(1L)
                .withTitle("testCaseTitle1")
                .withDescription("description1")
                .withTestCaseId(null)
                .build();
        TestCaseVersion entity = testCase.getCurrentVersion();
        TestCaseInfo dto = testCaseVersionMapper.mapEntity(entity, TestCaseInfo.class);

        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getTitle(), equalTo("testCaseTitle1"));
        assertThat(dto.getTestCaseId(), nullValue());
        assertThat(dto.getDescription(), equalTo("description1"));
        assertThat(dto.getVersion(), equalTo("0.1"));
    }

    @Test
    public void testSimpleEntityRequirements() {
        TestCase testCase = buildTestCase()
                .withId(1L)
                .withTitle("testCaseTitle1")
                .withDescription("description1")
                .withTestCaseId(null)
                .addRequirement(buildRequirement()
                        .withId(3L)
                        .withExternalId("3")
                        .build())
                .addRequirement(buildRequirement()
                        .withId(1L)
                        .withExternalId("1")
                        .build())
                .addRequirement(buildRequirement()
                        .withId(2L)
                        .withExternalId("2")
                        .build())
                .build();

        TestCaseInfo dto = testCaseVersionMapper
                .mapEntity(testCase.getCurrentVersion(), TestCaseInfo.class, TestCaseView.SimpleRequirements.class);

        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getTitle(), equalTo("testCaseTitle1"));
        assertThat(dto.getTestCaseId(), nullValue());
        assertThat(dto.getDescription(), equalTo("description1"));
        assertThat(dto.getRequirementIds().size(), equalTo(3));

        Iterator<String> requirementIterator = dto.getRequirementIds().iterator();

        assertThat(requirementIterator.next(), equalTo("1"));
        assertThat(requirementIterator.next(), equalTo("2"));
        assertThat(requirementIterator.next(), equalTo("3"));
    }

    @Test
    public void testDetailedEntity() {
        List<TestCampaign> testCampaigns = Lists.newArrayList(
                buildTestPlan()
                        .withId(1L)
                        .withName("testPlanName1")
                        .build(),
                buildTestPlan()
                        .withId(2L)
                        .withName("testPlanName2")
                        .build()
        );
        doReturn(testCampaigns).when(testCampaignRepository).findByTestCaseVersionId(1L);

        TechnicalComponent technicalComponent = buildTechnicalComponent()
                .withName("component")
                .build();

        TestField componentField = buildTestField()
                .withFieldType(FieldType.STRING)
                .withName(TestField.COMPONENT)
                .withValue("textComponent")
                .build();

        TestType testType = new TestType();
        testType.setId(3L);
        testType.setName("Robustness");

        TestCase testCase = buildTestCase()
                .withTitle("testCaseTitle")
                .withTestCaseId(null)
                .withDescription("description")
                .withType(testType)
                .withPriority(Priority.MINOR)
                .withPrecondition("precondition")
                .addTechnicalComponent(technicalComponent)
                .withOptionalField(componentField)
                .withId(1L)
                .addRequirement(buildRequirement()
                        .withId(2L)
                        .withExternalId("requirementExternalId")
                        .build())
                .addTestStep(buildTestStep()
                        .withId(22L)
                        .withExecute("testStep22")
                        .withComment("comment22")
                        .withData("data22")
                        .build())
                .addTestStep(buildTestStep()
                        .withId(21L)
                        .withExecute("testStep21")
                        .withComment("comment21")
                        .withData("data21")
                        .build())
                .build();
        TestCaseVersion entity = testCase.getCurrentVersion();
        TestCaseInfo dto = testCaseVersionMapper.mapEntity(entity, TestCaseInfo.class, TestCaseView.Detailed.class);

        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getTitle(), equalTo("testCaseTitle"));
        assertThat(dto.getTestCaseId(), nullValue());
        assertThat(dto.getDescription(), equalTo("description"));
        assertThat(dto.getType(), equalTo(new ReferenceDataItem("3", "Robustness")));
        assertThat(dto.getPriority(), equalTo(new ReferenceDataItem("3", "Minor")));
        assertThat(dto.getPrecondition(), equalTo("precondition"));
        assertThat(dto.getComponentTitle(), equalTo("component"));
        assertTrue(dto.getTechnicalComponents().contains(new ReferenceDataItem("", "component")));
        assertThat(dto.getRequirementIds().contains("requirementExternalId"), is(true));
        assertThat(dto.getComment(), equalTo(entity.getComment()));

        TestStepInfo testStep22 = new TestStepInfo();
        testStep22.setId(22L);
        testStep22.setName("testStep22");
        testStep22.setComment("comment22");
        testStep22.setData("data22");
        TestStepInfo testStep21 = new TestStepInfo();
        testStep21.setId(21L);
        testStep21.setName("testStep21");
        testStep21.setComment("comment21");
        testStep21.setData("data21");
        assertThat(dto.getTestSteps(), hasItems(testStep21, testStep22));

        List<TestCampaignInfo> associatedTestPlans = dto.getAssociatedTestCampaigns();
        assertThat(associatedTestPlans.size(), equalTo(2));
        assertThat(associatedTestPlans.get(0).getId(), equalTo(1L));
        assertThat(associatedTestPlans.get(0).getName(), equalTo("testPlanName1"));
        assertThat(associatedTestPlans.get(1).getId(), equalTo(2L));
        assertThat(associatedTestPlans.get(1).getName(), equalTo("testPlanName2"));
    }

    @Test
    public void testDto() {
        TestCaseInfo dto = TestDtoFactory.getTestCaseWithSteps(2);
        TestCaseVersion entity = testCaseVersionMapper.mapDto(dto, TestCaseVersion.class);

        assertThat(entity.getId(), equalTo(2L));
        assertThat(entity.getTitle(), equalTo("testCaseTitle2"));
        assertThat(entity.getDescription(), equalTo("description2"));
        assertThat(entity.getPriority(), equalTo(Priority.MINOR));
        assertThat(entity.getPrecondition(), equalTo("precondition2"));
        assertThat(entity.getComment(), equalTo(entity.getComment()));

        Set<TestStep> testSteps = entity.getTestSteps();

        assertThat(testSteps, hasSize(2));
    }
    @Test
    public void testGetVersion() {
        Map<String, Long> version = testCaseVersionMapper.getVersion("3.1");

        assertThat(version.get(testCaseVersionMapper.MAJOR), equalTo(3L));
        assertThat(version.get(testCaseVersionMapper.MINOR), equalTo(1L));
    }

}
