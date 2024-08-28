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
import com.ericsson.cifwk.tm.application.services.TestCaseSilentService;
import com.ericsson.cifwk.tm.application.services.helper.TestCaseServiceHelper;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.application.services.validation.TestCaseValidation;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.responses.BadRequest;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

@Service
public class TestCaseSilentServiceImpl implements TestCaseSilentService {

    @Inject
    private TestCaseServiceHelper testCaseServiceHelper;

    @Inject
    private TechnicalComponentRepository componentRepository;

    @Inject
    private ScopeRepository scopeRepository;

    @Inject
    private TestTypeRepository testTypeRepository;

    @Inject
    private RequirementService requirementService;

    @Inject
    private TestCaseValidation validation;

    @Override
    public List<BadRequest> populate(TestCaseVersion entity, TestCaseInfo testCaseInfo) {
        List<BadRequest> badRequests = Lists.newArrayList();

        // Requirement look-up
        Set<String> requirementIds = Sanitization.normalizeCommaSeparated(testCaseInfo.getRequirementIds());
        List<Requirement> requirements = requirementService.findAllOrImport(requirementIds);
        testCaseServiceHelper.diffRequirements(entity, requirements);

        Optional<Product> requirementProduct = Optional.ofNullable(null);
        try {
            validation.validateRequirements(requirements);
            requirementProduct = Optional.ofNullable(validation.validateRequirements(entity));
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }

        try {
            validation.validateRequirementsTypes(entity);
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }

        try {
            validation.validateTextLength(entity.getTitle(), 255, "Title");
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }

        if (requirementProduct.isPresent()) {
            // Component look-up
            Product product = requirementProduct.get();
            int componentCount = componentRepository.countForProduct(product.getId());
            entity.setTechnicalComponents(Sets.newHashSet());
            if (componentCount > 0) {
                Set<TechnicalComponent> technicalComponents =
                        testCaseServiceHelper.findTechnicalComponents(testCaseInfo, entity.getProductFeature().getId());
                entity.setTechnicalComponents(technicalComponents);
                try {
                    validation.validateTechnicalComponents(entity, product, entity.getProductFeature());
                } catch (ServiceValidationException e) {
                    addBadRequest(testCaseInfo, badRequests, e);
                }
            }

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

            if (hasNullScopes) {
                addBadRequest(testCaseInfo, badRequests, "Some groups don't exist");
            }

            entity.clearScopes();
            entity.addScopes(asList(scopes));

            try {
                validation.validateScopes(entity, product);
            } catch (ServiceValidationException e) {
                addBadRequest(testCaseInfo, badRequests, e);
            }

            try {
                validation.validateProductFeatureWithProduct(entity.getProductFeature(), product);
            } catch (ServiceValidationException e) {
                addBadRequest(testCaseInfo, badRequests, e);
            }

            try {
                validation.validateFeature(entity, product);
            } catch (ServiceValidationException e) {
                addBadRequest(testCaseInfo, badRequests, e);
            }

            try {
                validation.validateTestType(testCaseInfo);
            } catch (ServiceValidationException e) {
                addBadRequest(testCaseInfo, badRequests, e);
            }

            Optional<TestType> testType = testTypeRepository.findByNameAndProductId(
                    testCaseInfo.getType().getTitle(),
                    requirementProduct.get().getId());
            if (!testType.isPresent()) {
                addBadRequest(testCaseInfo, badRequests, "Test type does not have the correct value");
            } else {
                entity.setType(testType.get());
            }
        }

        try {
            validation.validateOptional(requirementProduct, "Product was not found from requirement specified");
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }
        try {
            validation.validateProductFeature(entity.getProductFeature());
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }

        Optional<Priority> priority = Optional.ofNullable(entity.getPriority());

        if (!priority.isPresent()) {
            addBadRequest(testCaseInfo, badRequests, "Priority does not have the correct value");
        }

        try {
            validation.validateTestCaseStatus(entity);
        } catch (ServiceValidationException e) {
            addBadRequest(testCaseInfo, badRequests, e);
        }

        return badRequests;
    }

    private void addBadRequest(TestCaseInfo testCaseInfo, List<BadRequest> badRequests, ServiceValidationException e) {
        badRequests.add(new BadRequest(
                testCaseInfo.getTestCaseId() + " - " + e.getMessage(),
                e.getDeveloperMessage()));
    }

    private void addBadRequest(TestCaseInfo testCaseInfo, List<BadRequest> badRequests, String message) {
        badRequests.add(new BadRequest(
                testCaseInfo.getTestCaseId() + " - " + message, ""));
    }
}
