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

import com.ericsson.cifwk.tm.application.annotations.Service;
import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.application.services.helper.TestCaseServiceHelper;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidation;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.application.services.validation.TestCaseValidation;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Inject
    private TechnicalComponentRepository componentRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private ScopeRepository scopeRepository;

    @Inject
    private RequirementService requirementService;

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Inject
    private TestTypeRepository testTypeRepository;

    @Inject
    private TestTeamRepository testTeamRepository;

    @Inject
    private TestSuiteRepository testSuiteRepository;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private TestCaseValidation validation;

    @Inject
    private TestCaseServiceHelper testCaseServiceHelper;

    @Override
    public Paginated<TestCase> customSearch(Query query, int page, int perPage) {
        Map<String, QueryField> testCaseFields = searchMapping.getTestCaseFields();
        Search search = query.convertToSearch(TestCase.class, testCaseFields);
        search.setDistinct(true);
        return testCaseRepository.searchPaginated(search, page, perPage);
    }

    @Override
    public void populate(TestCaseVersion entity, TestCaseInfo testCaseInfo) throws ServiceValidationException {
        // Requirement look-up
        Set<String> requirementIds = Sanitization.normalizeCommaSeparated(testCaseInfo.getRequirementIds());
        List<Requirement> requirements = requirementService.findAllOrImport(requirementIds);
        testCaseServiceHelper.diffRequirements(entity, requirements);

        Product requirementProduct = validation.validateRequirements(entity);
        validation.validateRequirementsTypes(entity);

        ProductFeature productFeature = productFeatureRepository.find(testCaseInfo.getFeature().getId());
        validation.validateRequirementsWithProductFeature(productFeature, requirementProduct);
        validation.validateProductFeatureWithProduct(productFeature, requirementProduct);
        validation.validateProductFeature(productFeature);
        entity.setProductFeature(productFeature);

        // Component look-up
        int componentCount = componentRepository.countForProduct(requirementProduct.getId());
        entity.setTechnicalComponents(Sets.newHashSet());
        if (componentCount > 0) {
            Set<TechnicalComponent> technicalComponents = testCaseServiceHelper.findTechnicalComponents(testCaseInfo, productFeature.getId());
            entity.setTechnicalComponents(technicalComponents);
            validation.validateTechnicalComponents(entity, requirementProduct, entity.getProductFeature());
        }

        Optional<TestType> testType = testTypeRepository.findByNameAndProductId(
                testCaseInfo.getType().getTitle(),
                requirementProduct.getId());
        validation.validateOptional(testType, "Test Type was not found");
        entity.setType(testType.get());

        addTestTeam(entity, testCaseInfo);
        addTestSuite(entity, testCaseInfo);

        // Scope look-up
        Set<ReferenceDataItem> groupIdsAsStrings = testCaseInfo.getGroups();
        List<Long> groupIds = newArrayList(transform(groupIdsAsStrings, new Function<ReferenceDataItem, Long>() {
            @Override
            public Long apply(ReferenceDataItem group) {
                return Long.valueOf(group.getId());
            }
        }));
        Scope[] scopes = scopeRepository.find(groupIds.toArray(new Long[groupIds.size()]));
        boolean hasNullScopes = Iterables.any(Arrays.asList(scopes), Predicates.isNull());
        ServiceValidation.check(!hasNullScopes, "Some groups don't exist");

        entity.clearScopes();
        entity.addScopes(asList(scopes));
        validation.validateScopes(entity, requirementProduct);

    }

    private void addTestSuite(TestCaseVersion entity, TestCaseInfo testCaseInfo) {
        if (testCaseInfo.getTestSuite() != null) {
            Optional<TestSuite> testSuite = Optional.ofNullable(testSuiteRepository.find(
                    Long.parseLong(testCaseInfo.getTestSuite().getId())));
            if (testSuite.isPresent()) {
                entity.setTestSuite(testSuite.get());
            }
        }
    }

    private void addTestTeam(TestCaseVersion entity, TestCaseInfo testCaseInfo) {
        if (testCaseInfo.getTestTeam() != null) {
            Optional<TestTeam> testTeam = Optional.ofNullable(testTeamRepository.find(
                    Long.parseLong(testCaseInfo.getTestTeam().getId())));
            if (testTeam.isPresent()) {
                entity.setTestTeam(testTeam.get());
            }
        }
    }

}
