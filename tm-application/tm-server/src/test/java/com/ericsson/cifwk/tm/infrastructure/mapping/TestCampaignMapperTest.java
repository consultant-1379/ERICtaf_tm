
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
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestCaseVersionRepositoryImpl;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static com.ericsson.cifwk.tm.test.fixture.Faker.autoIncrement;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestCase;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestPlan;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestPlanItem;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCampaignMapperTest {

    private TestCampaignMapper testCampaignMapper;
    private TestCampaignItemMapper testCampaignItemMapper;

    @Mock
    private TestCaseVersionRepository testCaseVersionRepository = new TestCaseVersionRepositoryImpl();

    @Before
    public void setUp() {
        ProjectMapper projectMapper = new ProjectMapper(new ProductMapper());
        testCampaignItemMapper = new TestCampaignItemMapper(
                new TestCaseVersionMapper(
                        new EnumReferenceMapper(),
                        new TestStepMapper(new VerifyStepMapper()),
                        new TestCaseViewFactory(),
                        null,
                        null,
                        new ProjectMapper(new ProductMapper()),
                        new FeatureMapper(new ProductMapper()),
                        new ReviewGroupMapper(),
                        new UserMapper(),
                        testCaseVersionRepository
                ),
                new EnumReferenceMapper(),
                new UserMapper()
        );
        testCampaignMapper = new TestCampaignMapper(
                new ProjectMapper(new ProductMapper()),
                new TestCampaignViewFactory(),
                new TestExecutionResultMapper(),
                new DefectMapper(projectMapper),
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                null,
                testCaseVersionRepository,
                new DropMapper(new ProductMapper()),
                new ProductMapper(),
                new FeatureMapper(new ProductMapper()),
                new UserMapper(),
                new ReviewGroupMapper()
        );
    }

    @Test
    public void testToDto() {
        TestCampaign entity = mock(TestCampaign.class, RETURNS_DEEP_STUBS);
        when(entity.getId()).thenReturn(1L);
        when(entity.getName()).thenReturn("TestPlanName");
        when(entity.getDescription()).thenReturn("TestPlanDescription");
        when(entity.getEnvironment()).thenReturn("TestEnvironment");
        when(entity.getFeatures()).thenReturn(Sets.newHashSet());
        when(entity.isLocked()).thenReturn(true);

        TestCampaignInfo testCampaignInfo = testCampaignMapper.mapEntity(entity, TestCampaignInfo.class);

        assertThat(testCampaignInfo.getId(), equalTo(entity.getId()));
        assertThat(testCampaignInfo.getName(), equalTo(entity.getName()));
        assertThat(testCampaignInfo.getDescription(), equalTo(entity.getDescription()));
        assertThat(testCampaignInfo.getEnvironment(), equalTo(entity.getEnvironment()));
        assertThat(testCampaignInfo.isLocked(), equalTo(entity.isLocked()));
    }

    @Test
    public void testSyncAssignments() {
        TestCampaignItem existingTestCampaignItem = createTestPlanItem(1L);
        TestCampaignItem existingTestCampaignItem2Del = createTestPlanItem(3L);
        TestCampaign dst = buildTestPlan()
                .addTestPlanItem(existingTestCampaignItem)
                .addTestPlanItem(existingTestCampaignItem2Del)
                .build();

        String testCaseId = existingTestCampaignItem.getTestCaseVersion().getTestCase().getTestCaseId();
        String testCaseId3 = existingTestCampaignItem2Del.getTestCaseVersion().getTestCase().getTestCaseId();

        when(testCaseVersionRepository.findByTestCase(testCaseId)).thenReturn(Lists.newArrayList());
        when(testCaseVersionRepository.findByTestCase(testCaseId3)).thenReturn(Lists.newArrayList());

        TestCampaignInfo src = testCampaignMapper.mapEntity(dst, TestCampaignInfo.class, TestCampaignView.Detailed.class);
        src.setTestCampaignItems(Sets.newHashSet(src.getTestCampaignItems()));
        Iterator<TestCampaignItemInfo> iterator = src.getTestCampaignItems().iterator();
        while (iterator.hasNext()) {
            TestCampaignItemInfo next = iterator.next();
            if (next.getId().equals(existingTestCampaignItem2Del.getId())) {
                iterator.remove();
            }
        }

        TestCampaignItem newTestCampaignItem = createTestPlanItem(3L);
        src.getTestCampaignItems().add(testCampaignItemMapper.mapEntity(newTestCampaignItem, TestCampaignItemInfo.class));

        TestCampaign result = testCampaignMapper.mapDto(src, dst);

        assertThat(Mapping.ids(result.getTestCampaignItems()),
                containsInAnyOrder(
                        existingTestCampaignItem.getId(),
                        newTestCampaignItem.getId()));
    }

    public TestCampaignItem createTestPlanItem(Long id) {
        return buildTestPlanItem()
                .withUser(buildUser().build())
                .withTestPlan(buildTestPlan().build())
                .withTestCaseVersion(buildTestCase().withId(autoIncrement()).build().getCurrentVersion())
                .withId(id)
                .build();
    }
}
