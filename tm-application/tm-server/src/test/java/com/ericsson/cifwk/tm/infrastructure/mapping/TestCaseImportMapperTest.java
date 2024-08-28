package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.application.services.ExcelObject;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.List;

import static com.ericsson.cifwk.tm.application.services.ReportingConstants.COMPONENT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.CONTEXT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.DESCRIPTION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.END_OF_DATA;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.EXECUTION_TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.FEATURE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.GROUPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRECONDITION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRIORITY;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.REQUIREMENTS_IDS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.STATUS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_CASE_ID;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_DATA;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP_SPLITTER;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TITLE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.VERIFY_STEP;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

/**
 * Created by egergle on 11/04/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCaseImportMapperTest {

    @Mock
    private ScopeRepository scopeRepository;

    @Mock
    private RequirementRepository requirementRepository;

    @Mock
    private ProductFeatureRepository featureRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private TestCaseImportMapper testCaseImportMapper = new TestCaseImportMapper();

    private String testStep1 = "Test Step 1";
    private String testStep2 = "Test Step 2";
    private String testData = "Test Data";
    private String verifyStep1 = "Verify Step 1";
    private String verifyStep2 = "Verify Step 2";

    private String verifyStepTest = "Verify Step ";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Requirement requirement = new Requirement();
        requirement.setExternalId("CIP-1111");
        Requirement requirement2 = new Requirement();
        requirement2.setExternalId("CIP-1112");

        Product product = new Product();
        product.setId(1L);
        product.setExternalId("OSS-RC");

        Project project = new Project();
        project.setId(1L);
        project.setProduct(product);

        requirement.setProject(project);
        requirement2.setProject(project);

        ProductFeature feature = new ProductFeature();
        feature.setName("Other");
        feature.setId(1L);
        feature.setProduct(product);

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1l);
        productInfo.setName("OSS-RC");
        productInfo.setDropCapable(true);

        doReturn(requirement).when(requirementRepository).findByExternalId("CIP-1111");
        doReturn(requirement2).when(requirementRepository).findByExternalId("CIP-1112");
        doReturn(feature).when(featureRepository).findByProductAndName(product.getId(), "Other");
        doReturn(productInfo).when(productMapper).mapEntity(product, ProductInfo.class);


        Scope scope = new Scope();
        scope.setId(1L);
        scope.setName("test");
        doReturn(scope).when(scopeRepository).findByNameAndProduct("CIP-1112", 1L);


    }

    @Test
    public void testMapTestCases() throws Exception {
        ExcelObject excelObject = new ExcelObject();
        LinkedHashMap<String, String> columns = excelObject.createColumns();

        Joiner joiner = Joiner.on(",");

        String testId = "testId";
        String feature = "Other";
        List<String> requirements = Lists.newArrayList("CIP-1111", "CIP-1112");
        String title = "title";
        String description = "description";
        String precondition = "precondition";
        String component = "test component";
        String type = "Functional";
        String priority = "Minor";
        List<String> groups = Lists.newArrayList("RFA", "Upgrade");
        List<String> contexts = Lists.newArrayList("REST", "UI");
        String executionType = "Manual";
        String status = "Review";
        String testSteps = "";


        columns.put(TEST_CASE_ID, testId);
        columns.put(FEATURE, feature);
        columns.put(REQUIREMENTS_IDS, joiner.join(requirements));
        columns.put(TITLE, title);
        columns.put(DESCRIPTION, description);
        columns.put(PRECONDITION, precondition);
        columns.put(COMPONENT, component);
        columns.put(TYPE, type);
        columns.put(PRIORITY, priority);
        columns.put(GROUPS, joiner.join(groups));
        columns.put(CONTEXT, joiner.join(contexts));
        columns.put(EXECUTION_TYPE, executionType);
        columns.put(STATUS, status);
        columns.put(TEST_STEPS, testSteps);
        excelObject.addRow(columns);

        List<TestCaseInfo> testCaseInfos = testCaseImportMapper.mapTestCases(excelObject);
        assertEquals(testCaseInfos.size(), 1);
        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            assertEquals(testCaseInfo.getTestCaseId(), testId);
        }

        TestCaseInfo testCaseInfo = testCaseInfos.get(0);
        assertEquals(testCaseInfo.getTestCaseId(), testId);
        assertEquals(testCaseInfo.getFeature().getName(), feature);
        for (String requirementId : testCaseInfo.getRequirementIds()) {
            assertThat(requirementId, isIn(requirements));
        }
        assertEquals(testCaseInfo.getTitle(), title);
        assertEquals(testCaseInfo.getDescription(), description);
        assertEquals(testCaseInfo.getPrecondition(), precondition);
        assertEquals(testCaseInfo.getType().getTitle(), type);
        assertEquals(testCaseInfo.getPriority().getTitle(), priority);
        for (ReferenceDataItem group : testCaseInfo.getGroups()) {
            assertThat(group.getTitle(), isIn(groups));
        }
        for (ReferenceDataItem context : testCaseInfo.getContexts()) {
            assertThat(context.getTitle(), isIn(contexts));
        }
        assertEquals(testCaseInfo.getExecutionType().getTitle(), executionType);
        assertEquals(testCaseInfo.getTestCaseStatus().getTitle(), status);

    }

    @Test
    public void testGetTestSteps() throws Exception {

        String content = TEST_STEP + testStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER + TEST_STEP + testStep2 + END_OF_DATA + "\n" +
                TEST_DATA + testData + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep2 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER;

        List<TestStepInfo> testSteps = testCaseImportMapper.getTestSteps(content);

        assertEquals(testSteps.get(0).getName(), testStep1);

        List<VerifyStepInfo> verifies = testSteps.get(0).getVerifies();
        assertVerifies(verifyStepTest, verifies);

        assertEquals(testSteps.get(1).getName(), testStep2);
        assertEquals(testSteps.get(1).getData(), testData);

        List<VerifyStepInfo> verifies2 = testSteps.get(1).getVerifies();
        assertVerifies(verifyStepTest, verifies2);

    }

    @Test
    public void testGetTestStepsWithMissingTestStepSplitter() throws Exception {

        String content = TEST_STEP + testStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                TEST_DATA + testData + END_OF_DATA + "\n" +
                TEST_STEP + testStep2 + END_OF_DATA + "\n" +
                TEST_DATA + testData+2 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep2 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER;

        List<TestStepInfo> testSteps = testCaseImportMapper.getTestSteps(content);

        assertEquals(testSteps.get(0).getName(), testStep1);
        assertEquals(testSteps.get(0).getData(), testData);

        List<VerifyStepInfo> verifies = testSteps.get(0).getVerifies();
        assertEquals(verifies.size(), 3);

        assertEquals(verifies.get(0).getName(), verifyStepTest + 1);
        assertEquals(verifies.get(1).getName(), verifyStepTest + 1);
        assertEquals(verifies.get(2).getName(), verifyStepTest + 2);

    }

    @Test
    public void testGetTestStepsWithBadVerifyEndData() throws Exception {

        String content = TEST_STEP + testStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + "\n" +
                VERIFY_STEP + verifyStep2 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER + TEST_STEP + testStep2 + END_OF_DATA + "\n" +
                TEST_DATA + testData + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep2 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER;

        List<TestStepInfo> testSteps = testCaseImportMapper.getTestSteps(content);

        assertEquals(testSteps.get(0).getName(), testStep1);

        List<VerifyStepInfo> verifies = testSteps.get(0).getVerifies();
        assertEquals(verifies.size(), 1);

        verifies = testSteps.get(1).getVerifies();
        assertEquals(verifies.size(), 2);

    }

    @Test
    public void testGetTestStepsWithBadEndData() throws Exception {

        String content = TEST_STEP + testStep1 + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER + TEST_STEP + testStep2 + END_OF_DATA + "\n" +
                TEST_DATA + testData + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep1 + END_OF_DATA + "\n" +
                VERIFY_STEP + verifyStep2 + END_OF_DATA + "\n" +
                TEST_STEP_SPLITTER;

        List<TestStepInfo> testSteps = testCaseImportMapper.getTestSteps(content);

        assertNotNull(testSteps.get(0).getName());
        assertEquals(testSteps.size(), 2);
    }

    private void assertVerifies(String verifyStepTest, List<VerifyStepInfo> verifies) {
        int index = 1;
        for (VerifyStepInfo verify : verifies) {
            assertEquals(verify.getName(), verifyStepTest + index);
            index++;
        }
    }
}