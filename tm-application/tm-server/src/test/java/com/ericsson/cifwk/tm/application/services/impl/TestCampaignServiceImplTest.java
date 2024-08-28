/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.s
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.domain.events.UserAssignedEvent;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.DefectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.EnumReferenceMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProductMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestExecutionResultMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignItemMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestStepMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.VerifyStepMapper;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ericsson.cifwk.tm.test.TestDtoFactory.getTestPlanItemInfo;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestCase;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestPlanItem;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCampaignServiceImplTest {

    @Mock
    TestCaseRepository testCaseRepository;

    @Mock
    TestCaseVersionRepository testCaseVersionRepository;

    @Mock
    TestCampaignRepository testCampaignRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserDirectory userDirectory;

    @Mock
    TestCampaignGroupRepository testCampaignGroupRepository;

    @Mock
    EventBus eventBus;

    @Mock
    ProjectMapper projectMapper = new ProjectMapper(new ProductMapper());

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductFeatureRepository featureRepository;

    @Mock
    TechnicalComponentRepository componentRepository;

    @Mock
    DropRepository dropRepository;

    @Mock
    DropMapper dropMapper;

    TestCampaignMapper testCampaignMapper;

    TestCampaignItemMapper testCampaignItemMapper;

    TestCampaignServiceImpl testPlanService;

    UserMapper userMapper;

    private final String URL = "http://test.se";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        EnumReferenceMapper referenceMapper = new EnumReferenceMapper();
        testCampaignMapper = new TestCampaignMapper(
                new ProjectMapper(new ProductMapper()),
                new TestCampaignViewFactory(),
                new TestExecutionResultMapper(),
                new DefectMapper(new ProjectMapper(new ProductMapper())),
                referenceMapper,
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                testCampaignRepository,
                testCaseVersionRepository,
                new DropMapper(new ProductMapper()),
                new ProductMapper(),
                new FeatureMapper(new ProductMapper()),
                new UserMapper(),
                new ReviewGroupMapper()
        );
        testCampaignItemMapper = new TestCampaignItemMapper(
                testCaseVersionRepository,
                testCampaignMapper.getTestCaseVersionMapper(),
                userRepository,
                userDirectory,
                referenceMapper,
                userMapper
        );
        testPlanService = new TestCampaignServiceImpl(
                testCampaignRepository,
                null,
                testCampaignMapper,
                testCampaignItemMapper,
                eventBus,
                new SearchMapping(),
                featureRepository,
                componentRepository,
                dropRepository,
                dropMapper,
                testCampaignGroupRepository
        );

        when(testCampaignRepository.save(any(TestCampaign.class))).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[0];
            }
        });
    }

    @Test
    public void testFindAll_WhenNoProjectId_ShouldReturnAllProjects() {
        List<Object> testPlans = getTestPlans();
        when(testCampaignRepository.search(any(Search.class))).thenReturn(testPlans);
        List<TestCampaign> all = testPlanService.findAll(null);
        assertThat(all.size(), equalTo(testPlans.size()));
    }

    @Test
    public void testFindAll_WhenProjectIdSpecified_ShouldReturnAllProjectsForSpecifiedProjectId() {
        List<Object> testPlans = getTestPlans();
        when(testCampaignRepository.search(any(Search.class))).thenReturn(testPlans);
        List<TestCampaign> all = testPlanService.findAll("CIP");
        assertThat(all.size(), equalTo(testPlans.size()));
    }

    @Test
    public void testFindAllOpen_SearchesForOpenTestPlans() {
        ArgumentCaptor<Search> searchArgumentCaptor = ArgumentCaptor.forClass(Search.class);
        List<Object> testPlans = getTestPlans();
        when(testCampaignRepository.search(any(Search.class))).thenReturn(testPlans);

        List<TestCampaign> all = testPlanService.findAllOpen("CIP");

        verify(testCampaignRepository).search(searchArgumentCaptor.capture());

        assertThat(all.size(), equalTo(testPlans.size()));
        assertThat(searchArgumentCaptor.getValue().getFilters(), hasItem(new Filter("locked", false)));
    }

    @Test
    public void testPopulateNewTestPlanItems() throws Exception {
        final long ASSIGNMENT1 = 1;
        final long ASSIGNMENT2 = 2;
        final long ASSIGNMENT3 = 3;

        stubUsersRepository();
        stubTestCaseRepository();
        stubTestCaseVersionRepository();

        TestCampaign target = new TestCampaign();

        TestCampaignInfo source = new TestCampaignInfo();

        source.getTestCampaignItems().add(getTestPlanItemInfo((int) ASSIGNMENT1));
        source.getTestCampaignItems().add(getTestPlanItemInfo((int) ASSIGNMENT2));
        source.getTestCampaignItems().add(getTestPlanItemInfo((int) ASSIGNMENT3));
        source.setHostname(URL);
        source.setFeatures(Sets.newHashSet());

        testPlanService.populate(target, source);
        testPlanService.notifyAboutAssignment(testPlanService.getUsersToNotify(target, source),
                target.getId(),
                source.getHostname());

        Set<Long> assignmentIds = Mapping.ids(target.getTestCampaignItems());
        Set<Long> tcIds = Sets.newHashSet();
        Set<String> userIds = Sets.newHashSet();
        for (TestCampaignItem testCampaignItem : target.getTestCampaignItems()) {
            tcIds.add(testCampaignItem.getTestCaseVersion().getId());
            userIds.add(testCampaignItem.getUser().getExternalId());
        }

        assertThat(assignmentIds, containsInAnyOrder(ASSIGNMENT1, ASSIGNMENT2, ASSIGNMENT3));
        assertThat(tcIds, containsInAnyOrder(ASSIGNMENT1 * 11, ASSIGNMENT2 * 11, ASSIGNMENT3 * 11));
        assertThat(userIds, containsInAnyOrder("mruser" + ASSIGNMENT1, "mruser" + ASSIGNMENT2, "mruser" + ASSIGNMENT3));

        verify(eventBus).post(new UserAssignedEvent(target.getId(), "mruser1", Lists.newArrayList("testCaseId11"), URL));
        verify(eventBus).post(new UserAssignedEvent(target.getId(), "mruser2", Lists.newArrayList("testCaseId22"), URL));
        verify(eventBus).post(new UserAssignedEvent(target.getId(), "mruser3", Lists.newArrayList("testCaseId33"), URL));
    }

    @Test
    public void testPopulateWhenOneAssignmentExistsAndOtherTwoHaveSameUser() throws Exception {
        final long ASSIGNMENT1 = 1;
        final long ASSIGNMENT2 = 2;
        final long ASSIGNMENT3 = 3;

        stubUsersRepository();
        stubTestCaseRepository();
        stubTestCaseVersionRepository();

        TestCampaign target = new TestCampaign();

        TestCampaignInfo source = new TestCampaignInfo();

        TestCampaignItem testCampaignItem1 = buildTestPlanItem()
                .withId(ASSIGNMENT1)
                .withUser(buildUser().withExternalId("mruser2").build())
                .withTestCaseVersion(buildTestCase()
                        .withId(11L)
                        .withTestCaseId("testCaseId11")
                        .build().getCurrentVersion())
                .build();
        target.addTestCampaignItem(testCampaignItem1);

        // ~ Test Case
        TestCaseInfo testCase = new TestCaseInfo();
        testCase.setId(11L);
        testCase.setTestCaseId(testCampaignItem1.getTestCaseVersion().getTestCase().getTestCaseId());
        testCase.setVersion(testCampaignItem1.getTestCaseVersion().getMajorVersion() + "." + testCampaignItem1.getTestCaseVersion().getMinorVersion());

        // ~ Test plan item
        TestCampaignItemInfo testCampaignItemInfo1 = new TestCampaignItemInfo();
        testCampaignItemInfo1.setId(testCampaignItem1.getId());

        UserInfo userInfo = createUser(testCampaignItem1);

        testCampaignItemInfo1.setUser(userInfo);
        testCampaignItemInfo1.setTestCase(testCase);

        TestCampaignItemInfo testCampaignItemInfo2 = getTestPlanItemInfo((int) ASSIGNMENT2);
        TestCampaignItemInfo testCampaignItemInfo3 = getTestPlanItemInfo((int) ASSIGNMENT3);
        testCampaignItemInfo3.setUser(testCampaignItemInfo2.getUser());

        source.getTestCampaignItems().add(testCampaignItemInfo1);
        source.getTestCampaignItems().add(testCampaignItemInfo2);
        source.getTestCampaignItems().add(testCampaignItemInfo3);
        source.setHostname(URL);
        source.setFeatures(Sets.newHashSet());

        testPlanService.populate(target, source);
        testPlanService.notifyAboutAssignment(testPlanService.getUsersToNotify(target, source),
                target.getId(),
                source.getHostname());

        Set<Long> assignmentIds = Mapping.ids(target.getTestCampaignItems());
        Set<Long> tcIds = Sets.newHashSet();
        Set<String> userIds = Sets.newHashSet();
        for (TestCampaignItem testCampaignItem : target.getTestCampaignItems()) {
            tcIds.add(testCampaignItem.getTestCaseVersion().getId());
            userIds.add(testCampaignItem.getUser().getExternalId());
        }

        assertThat(assignmentIds, containsInAnyOrder(ASSIGNMENT1, ASSIGNMENT2, ASSIGNMENT3));
        assertThat(tcIds, containsInAnyOrder(ASSIGNMENT1 * 11, ASSIGNMENT2 * 11, ASSIGNMENT3 * 11));

        verify(eventBus).post(new UserAssignedEvent(target.getId(), "mruser2", Lists.newArrayList("testCaseId22", "testCaseId33"), URL));
    }

    private void stubTestCaseRepository() {
        when(testCaseRepository.find(anyLong())).thenAnswer(new Answer<TestCase>() {
            @Override
            public TestCase answer(InvocationOnMock invocation) throws Throwable {
                Long id = (Long) invocation.getArguments()[0];
                TestCase testCase = new TestCase();
                testCase.setId(id);
                TestCaseVersion testCaseVersion = new TestCaseVersion();
                testCaseVersion.setId(id);
                testCase.setCurrentVersion(testCaseVersion);
                return testCase;
            }
        });
    }

    private void stubTestCaseVersionRepository() {
        when(testCaseVersionRepository.find(anyLong()))
                .thenAnswer(new Answer<TestCaseVersion>() {
                    @Override
                    public TestCaseVersion answer(InvocationOnMock invocation) throws Throwable {
                        TestCaseVersion testCaseVersion = new TestCaseVersion();
                        Long id = (Long) invocation.getArguments()[0];
                        testCaseVersion.setId(id);
                        testCaseVersion.setTestCase(buildTestCase()
                                .withTestCaseId("testCaseId" + id)
                                .withId(id)
                                .build());
                        return testCaseVersion;
                    }
                });
        when(testCaseVersionRepository.findByTestCaseAndSequence(anyLong(), anyLong(), anyLong()))
                .thenAnswer(new Answer<TestCaseVersion>() {
                    @Override
                    public TestCaseVersion answer(InvocationOnMock invocation) throws Throwable {
                        Object[] arguments = invocation.getArguments();
                        TestCaseVersion testCaseVersion = new TestCaseVersion();
                        Long id = (Long) arguments[0];
                        testCaseVersion.setId(id);
                        testCaseVersion.setTestCase(buildTestCase()
                                .withTestCaseId("testCaseId" + id)
                                .withId(id)
                                .build());
                        testCaseVersion.setMajorVersion((Long) arguments[1]);
                        return testCaseVersion;
                    }
                });
    }

    private void stubUsersRepository() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                Object[] ids = invocation.getArguments();
                return TestEntityFactory.buildUser()
                        .withExternalId(String.valueOf(ids[0]))
                        .build();
            }
        }).when(userRepository).findByExternalId(anyString());
    }

    private List<Object> getTestPlans() {
        List<Object> testPlans = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            testPlans.add(new TestCampaign());
        }
        return testPlans;
    }

    private UserInfo createUser(TestCampaignItem testCampaignItem1) {
        UserInfo userInfo = new UserInfo();

        userInfo.setId(testCampaignItem1.getUser().getId());
        userInfo.setUserId(testCampaignItem1.getUser().getExternalId());
        userInfo.setSurname(testCampaignItem1.getUser().getExternalSurname());
        userInfo.setUserName(testCampaignItem1.getUser().getUserName());
        return userInfo;
    }

}
