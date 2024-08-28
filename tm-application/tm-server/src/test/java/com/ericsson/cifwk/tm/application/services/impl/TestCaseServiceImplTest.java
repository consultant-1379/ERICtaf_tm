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

import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.application.services.helper.TestCaseServiceHelper;
import com.ericsson.cifwk.tm.application.services.validation.TestCaseValidation;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.EnumReferenceMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProductMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestStepMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.VerifyStepMapper;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCaseServiceImplTest {

    @Mock
    private TechnicalComponentRepository componentRepo;

    @Mock
    private RequirementService requirementService;

    @Mock
    private ScopeRepository scopeRepository;

    @Mock
    private TestCaseValidation validation;

    @InjectMocks
    private TestCaseServiceImpl testCaseService = new TestCaseServiceImpl();

    @Mock
    private TestCaseServiceHelper testCaseServiceHelper;

    @Mock
    private Requirement requirement;

    @Mock
    private TechnicalComponent component;

    @Spy
    private EnumReferenceMapper referenceMapper = new EnumReferenceMapper();

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @Mock
    private TestCaseVersionRepository testCaseVersionRepository;

    @Mock
    private TestCampaignMapper testCampaignMapper;

    @Mock
    private ProductFeatureRepository productFeatureRepository;

    @Mock
    private TestTypeRepository testTypeRepository;

    @Spy
    private TestCaseVersionMapper testCaseVersionMapper = new TestCaseVersionMapper(
            referenceMapper,
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

    @Spy
    private TestCaseMapper testCaseMapper = new TestCaseMapper(testCaseVersionMapper, new TestCaseViewFactory());

    private Scope scope1 = new Scope("scope1");

    private Scope scope2 = new Scope("scope2");

    private TestCaseInfo dto;

    String requirementId = "externalRequirementId";

    @Before
    public void setUp() throws Exception {
        Product product = new Product("product");
        product.setId(1L);

        Project project = new Project("project");
        project.setId(1L);
        project.setProduct(product);

        scope1.setProduct(product);
        scope2.setProduct(product);

        ProductFeature feature = new ProductFeature();
        feature.setName("Other");
        feature.setId(1L);
        feature.setProduct(product);

        TestType testType = new TestType();
        testType.setId(1L);
        testType.setName("type");

        Optional<TestType> optional = Optional.ofNullable(testType);

        when(validation.validateRequirements(any(TestCaseVersion.class))).thenReturn(product);
        when(requirement.getExternalId()).thenReturn(requirementId);
        when(requirementService.findAllOrImport(Collections.singleton(requirementId)))
                .thenReturn(Collections.singletonList(requirement));
        when(componentRepo.find(1L)).thenReturn(component);
        when(componentRepo.findByName(eq("component"), anyLong())).thenReturn(component);
        when(componentRepo.countForProduct(1L)).thenReturn(1);
        when(scopeRepository.find(11L, 22L)).thenReturn(new Scope[]{scope1, scope2});
        when(scopeRepository.find(22L, 11L)).thenReturn(new Scope[]{scope1, scope2});
        when(testTypeRepository.findByNameAndProductId("type", product.getId())).thenReturn(optional);

        doReturn(feature).when(productFeatureRepository).find(feature.getId());

        dto = new TestCaseInfo();
        dto.setPriority(new ReferenceDataItem(Priority.BLOCKER.name(), Priority.BLOCKER.toString()));
        dto.setRequirements(Collections.singletonList(new RequirementInfo()));
        dto.setDescription("description");
        dto.setTestCaseId("testCaseId");
        dto.setPackageName("package");
        dto.setPrecondition("precondition");
        dto.addTechnicalComponent(new ReferenceDataItem("1", "component"));
        dto.setRequirementIds(Sets.newLinkedHashSet(Lists.newArrayList(requirementId)));
        dto.setTitle("title");
        dto.setType(new ReferenceDataItem("3", "type"));
        dto.addGroup(new ReferenceDataItem("11", "group11"));
        dto.addGroup(new ReferenceDataItem("22", "group22"));
        dto.addContext(new ReferenceDataItem("1", "context1"));
        dto.addContext(new ReferenceDataItem("2", "context2"));
        dto.setExecutionType(new ReferenceDataItem("2", "Automated"));
        dto.setTestCaseStatus(new ReferenceDataItem("1", "APPROVED"));
        FeatureInfo other = new FeatureInfo(1L, "Other");
        dto.setFeature(other);

        when(testCaseServiceHelper.findTechnicalComponents(dto, other.getId())).thenReturn(Sets.newHashSet(component));

    }

    @Test
    public void populate() throws Exception {
        TestCase testCase = TestCaseFactory.createTestCase();
        TestCaseVersion entity = testCase.getCurrentVersion();

        testCaseService.populate(entity, dto);

        assertTrue(entity.getTechnicalComponents().contains(component));
        // checking scopes
        List<Scope> scopesList = newArrayList(entity.getScopes());
        sort(scopesList);
        Iterator<Scope> scopes = scopesList.iterator();
        assertEquals(scope1, scopes.next());
        assertEquals(scope2, scopes.next());
    }

    @Test
    public void diffRequirements() {
        Requirement a1 = requirementMock("A");
        Requirement b1 = requirementMock("B");
        Requirement c1 = requirementMock("C");
        Requirement b2 = requirementMock("B");
        Requirement c2 = requirementMock("C");
        Requirement d2 = requirementMock("D");
        TestCaseVersion testCaseVersion = new TestCaseVersion();
        testCaseVersion.addRequirements(Sets.newHashSet(a1, b1, c1));

        TestCaseServiceHelper testCaseServiceHelper = new TestCaseServiceHelper();

        TestCaseVersion testCaseVersionSpy = spy(testCaseVersion);
        testCaseServiceHelper.diffRequirements(testCaseVersionSpy, Arrays.asList(c2, d2, b2));

        verify(testCaseVersionSpy, times(1)).addRequirement(any(Requirement.class));
        verify(testCaseVersionSpy).addRequirement(d2);
        verify(testCaseVersionSpy, times(1)).removeRequirement(any(Requirement.class));
        verify(testCaseVersionSpy).removeRequirement(a1);
        assertEquals(Sets.newHashSet(b1, c1, d2), testCaseVersion.getRequirements());
    }

    private Requirement requirementMock(String externalId) {
        Requirement mock = mock(Requirement.class);
        when(mock.getExternalId()).thenReturn(externalId);
        return mock;
    }

}
