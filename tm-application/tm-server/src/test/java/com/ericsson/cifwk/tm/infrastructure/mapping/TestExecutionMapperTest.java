package com.ericsson.cifwk.tm.infrastructure.mapping;


import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.execution.VerifyStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.impl.TestExecutionRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStepRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStepRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestStepRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.VerifyStepRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionViewFactory;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import com.ericsson.cifwk.tm.test.fixture.builders.TestExecutionBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class TestExecutionMapperTest {

    private TestExecutionMapper testExecutionMapper;

    private EnumReferenceMapper referenceMapper = new EnumReferenceMapper();

    @InjectMocks
    private TestExecutionRepository testExecutionRepository = new TestExecutionRepositoryImpl();

    @InjectMocks
    private TestStepRepository testStepRepository = new TestStepRepositoryImpl();

    @InjectMocks
    private VerifyStepRepository verifyStepRepository = new VerifyStepRepositoryImpl();

    @Before
    public void setUp() {
        User user = buildUser().build();
        TestCampaign testCampaign = buildTestPlan().build();
        TestCase testCase = buildTestCase().build();

        testExecutionRepository = spy(testExecutionRepository);
        TestExecution testExecution = buildTestExecution(user, testCampaign, testCase.getCurrentVersion())
                .withId(1L)
                .build();
        doReturn(testExecution).when(testExecutionRepository).find(1L);

        testStepRepository = spy(testStepRepository);
        TestStep testStep = buildTestStep()
                .withId(1L)
                .build();
        doReturn(testStep).when(testStepRepository).find(1L);

        verifyStepRepository = spy(verifyStepRepository);
        VerifyStep verifyStep = buildVerifyStep()
                .withId(1L)
                .build();
        doReturn(verifyStep).when(verifyStepRepository).find(1L);

        testExecutionMapper = new TestExecutionMapper(
                referenceMapper,
                new TestStepExecutionMapper(referenceMapper, testStepRepository, testExecutionRepository),
                new VerifyStepExecutionMapper(verifyStepRepository, testExecutionRepository),
                new IsoMapper(),
                new TestExecutionViewFactory()
        );
    }

    @Test
    public void testEntity() {
        TestExecution testExecution = createTestExecution().build();
        TestExecutionInfo dto = testExecutionMapper.mapEntity(testExecution, TestExecutionInfo.class);
        assertNotNull(dto);
        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getTestPlan(), equalTo(1L));
        assertThat(dto.getTestCase(), equalTo(101L));
        assertThat(dto.getResult(), equalTo(new ReferenceDataItem("2", "Pass")));
        assertThat(dto.getAuthor(), equalTo("userName1"));
        assertThat(dto.getComment(), equalTo("comment1"));
    }

    @Test
    public void testDetailedEntity() {
        TestStep testStep = buildTestStep().withId(1L).build();
        TestExecution testExecution = createTestExecution()
                .addTestStepExecution(buildTestStepExecution()
                        .withId(1L)
                        .withData("data1")
                        .withResult(TestStepResult.PASS)
                        .withTestStep(testStep)
                        .build())
                .addVerifyStepExecution(buildVerifyStepExecution()
                        .withId(1L)
                        .withVerifyStep(buildVerifyStep()
                                .withId(1L)
                                .withTestStep(testStep)
                                .build())
                        .withTestExecution(buildTestExecution().withId(1L).build())
                        .withActualResult("Actual result1")
                        .build())
                .build();

        TestExecutionInfo dto = testExecutionMapper.mapEntity(testExecution, TestExecutionInfo.class, TestExecutionView.Detailed.class);
        assertNotNull(dto);
        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getTestPlan(), equalTo(1L));
        assertThat(dto.getTestCase(), equalTo(101L));
        assertThat(dto.getAuthor(), equalTo("userName1"));
        assertThat(dto.getComment(), equalTo("comment1"));

        assertThat(dto.getTestStepExecutions().size(), equalTo(1));
        TestStepExecutionInfo testStepExecutionInfo = dto.getTestStepExecutions().iterator().next();
        assertThat(testStepExecutionInfo.getId(), equalTo(1L));
        assertThat(testStepExecutionInfo.getResult(), equalTo(new ReferenceDataItem("1", "Pass")));
        assertThat(testStepExecutionInfo.getData(), equalTo("data1"));

        assertThat(dto.getVerifyStepExecutions().size(), equalTo(1));
        VerifyStepExecutionInfo verifyStepExecutionInfo = dto.getVerifyStepExecutions().iterator().next();
        assertThat(verifyStepExecutionInfo.getId(), equalTo(1L));
        assertThat(verifyStepExecutionInfo.getActualResult(), equalTo("Actual result1"));
    }

    @Test
    public void testDto() {
        TestExecutionInfo dto = TestDtoFactory.getTestExecution(1);
        dto.addTestStepExecution(TestDtoFactory.getTestStepExecution(1, 1, 1));
        dto.addVerifyStepExecution(TestDtoFactory.getVerifyStepExecution(1, 1, 1, 1));
        TestExecution entity = testExecutionMapper.mapDto(dto, TestExecution.class);
        assertNotNull(entity);
        assertThat(entity.getId(), equalTo(1L));
        assertThat(entity.getResult(), equalTo(TestExecutionResult.PASS));
        assertThat(entity.getComment(), equalTo("comment1"));

        assertThat(entity.getTestStepExecutions().size(), equalTo(1));
        TestStepExecution testStepExecution = entity.getTestStepExecutions().iterator().next();
        assertThat(testStepExecution.getId(), equalTo(1L));
        assertThat(testStepExecution.getResult(), equalTo(TestStepResult.PASS));
        assertThat(testStepExecution.getData(), equalTo("data1"));

        assertThat(entity.getVerifyStepExecutions().size(), equalTo(1));
        VerifyStepExecution verifyStepExecution = entity.getVerifyStepExecutions().iterator().next();
        assertThat(verifyStepExecution.getId(), equalTo(1L));
        assertThat(verifyStepExecution.getActualResult(), equalTo("Actual result1"));
    }

    private TestExecutionBuilder createTestExecution() {
        Product product = buildProduct("ENM").build();
        ProductFeature feature = buildProductFeature(product).build();
        TestCampaign testCampaign = buildTestPlan(feature).build();
        testCampaign.setId(1L);

        return buildTestExecution()
                .withId(1L)
                .withTestPlan(testCampaign)
                .withTestCaseVersion(buildTestCase().withId(101L).build().getCurrentVersion())
                .withResult(TestExecutionResult.PASS)
                .withAuthor(buildUser().withExternalId("externalId1").withUserName("userName1").build())
                .withComment("comment1");
    }

}
