/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.validation;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TestCaseValidation {

    private final String versionRegex = "^(\\d+)\\.?(\\d+)$";

    void validate(TestCaseVersion entity)
            throws ServiceValidationException {
        Product validProduct = validateRequirements(entity);
        validateTechnicalComponents(entity, validProduct, entity.getProductFeature());
        validateScopes(entity, validProduct);
    }

    public void validateRequirementsTypes(TestCaseVersion entity)
            throws ServiceValidationException {
        try {
            Set<Requirement> requirements = entity.getRequirements();
            for (Requirement requirement : requirements) {
                ServiceValidation.check(ValidRequirementTestCaseTypes.isValid(requirement.getExternalType()),
                        "Requirement " + requirement.getExternalId() +
                                " is an invalid requirement type for creating/updating a test case");
            }
        } catch (NullPointerException e) {
            throw new ServiceValidationException(e.getMessage());
        }

    }

    public void validateRequirements(List<Requirement> requirements)
            throws ServiceValidationException {
        ServiceValidation.check(!requirements.isEmpty(),
                "Requirements were not found");
    }

    public Product validateRequirements(TestCaseVersion entity)
            throws ServiceValidationException {
        Set<Product> requirementProducts;
        try {
            requirementProducts = getProductsFromRequirements(entity.getRequirements());
        } catch (NullPointerException e) {
            throw new ServiceValidationException(e.getMessage());
        }
        ServiceValidation.check(!requirementProducts.isEmpty(),
                "Test case is not associated with a product through requirements");
        ServiceValidation.check(requirementProducts.size() == 1,
                "Test case is associated with more than one product through requirements");
        return requirementProducts.iterator().next();
    }

    public void validateTechnicalComponents(TestCaseVersion entity, Product validProduct, ProductFeature feature)
            throws ServiceValidationException {

        Set<TechnicalComponent> components = entity.getTechnicalComponents();

        ServiceValidation.check(!components.isEmpty() || components != null,
                "Component are empty");

        for (TechnicalComponent component : components) {
            ServiceValidation.check(component != null,
                    "Component doesn't exist or is not connected to the selected feature " + feature.getName());

            ProductFeature componentFeature = component.getFeature();
            ServiceValidation.check(componentFeature != null, "Feature doesn't exist ");

            Product product = componentFeature.getProduct();
            ServiceValidation.check(product == null || product.equals(validProduct),
                    "Requirement and component products are not the same");

            ServiceValidation.check(componentFeature == null || componentFeature.equals(feature),
                    "Component is not connected to the correct feature specified");
        }
    }

    public void validateScopes(TestCaseVersion entity, Product validProduct)
            throws ServiceValidationException {
        Set<Scope> scopes = entity.getScopes();

        // Check enabled scopes
        boolean allEnabled = Iterables.all(scopes, new Predicate<Scope>() {
            @Override
            public boolean apply(Scope input) {
                return input.isEnabled();
            }
        });
        ServiceValidation.check(allEnabled, "Some linked scopes are disabled");

        // Validate scope products
        Set<Product> scopeProducts = getProductsFromScopes(scopes);
        if (scopeProducts.isEmpty()) {
            return;
        }
        ServiceValidation.check(scopeProducts.size() == 1,
                "Test case is associated with more than one product through groups");

        Product scopeProduct = scopeProducts.iterator().next();
        ServiceValidation.check(scopeProduct.equals(validProduct),
                "Requirement and group products are not the same");
    }

    public void validateFeature(TestCaseVersion entity, Product validProduct)
            throws ServiceValidationException {

        ProductFeature productFeature = entity.getProductFeature();

        ServiceValidation.check(productFeature != null,
                "Feature specified was not found");

        Product product = productFeature.getProduct();
        ServiceValidation.check(product.getId().equals(validProduct.getId()),
                "Feature is not connected to the correct product");
    }

    public void validateTextLength(String text, int size, String identifier)
            throws ServiceValidationException {

        ServiceValidation.check(text.length() < size,
                identifier + " has characters greater than " + size);
    }

    private Set<Product> getProductsFromRequirements(Set<Requirement> requirements) {
        return getProducts(requirements, new Function<Requirement, Product>() {
            @Nullable
            @Override
            public Product apply(Requirement input) {
                Project project = input.getProject();
                Preconditions.checkNotNull(project,
                        "Requirement is not connected to project: %s", input.getExternalId());
                return project.getProduct();
            }
        });
    }

    private Set<Product> getProductsFromScopes(Set<Scope> scopes) {
        return getProducts(scopes, new Function<Scope, Product>() {
            @Nullable
            @Override
            public Product apply(Scope input) {
                return input.getProduct();
            }
        });
    }

    private <T> Set<Product> getProducts(Set<T> entities, Function<T, Product> mapper) {
        return FluentIterable.from(entities)
                .transform(mapper)
                .filter(Predicates.notNull())
                .toSet();
    }

    public void validateProduct(Product product)
            throws ServiceValidationException {
        ServiceValidation.check(product != null,
                "Could not find product from requirement specified");
    }

    public void validateRequirementsWithProductFeature(ProductFeature productFeature, Product product)
            throws ServiceValidationException {
        ServiceValidation.check(productFeature.getProduct() == product,
                "Requirements do not match the correct product");
    }

    public void validateProductFeature(ProductFeature productFeature) throws ServiceValidationException {
        Optional<ProductFeature> feature = Optional.ofNullable(productFeature);
        ServiceValidation.check(feature.isPresent(),
                "Feature id is not found for test case");
    }

    public void validateProductFeatureWithProduct(ProductFeature productFeature, Product product)
            throws ServiceValidationException {
        ServiceValidation.check(product.getFeatures().contains(productFeature),
                "Feature does not match the correct product");
    }

    public void validateOptional(Optional optional, String message)
            throws ServiceValidationException {
        ServiceValidation.check(optional.isPresent(),
                message);
    }

    public void validateTestType(TestCaseInfo testCaseInfo)
            throws ServiceValidationException {

        ServiceValidation.check(testCaseInfo.getType() != null,
                "Test Type is not found");

    }

    public void validateTestCaseStatus(TestCaseVersion entity)
            throws ServiceValidationException {

        Optional<TestCaseStatus> testCaseStatus = Optional.ofNullable(entity.getTestCaseStatus());

        ServiceValidation.check(testCaseStatus.isPresent(),
                "Test case status was not found");

        ServiceValidation.check(entity.getTestCaseStatus().equals(TestCaseStatus.PRELIMINARY) ||
                entity.getTestCaseStatus().equals(TestCaseStatus.REJECTED),

                "Test case cannot be updated while in review or approved status");
    }

    public boolean validateVersionFormat(String version) {
        return version.matches(versionRegex);
    }

}
