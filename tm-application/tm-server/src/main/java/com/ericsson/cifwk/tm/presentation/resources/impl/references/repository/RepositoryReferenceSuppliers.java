/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references.repository;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.filter.StringParamFilter;
import com.google.common.collect.ImmutableSet;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import java.util.List;

public final class RepositoryReferenceSuppliers {

    private RepositoryReferenceSuppliers() {
    }

    public static RepositoryReferenceSupplier groups(ScopeRepository scopeRepository) {
        return new RepositoryReferenceSupplier<>(
                scopeRepository,
                ImmutableSet.of(filterEnabledGlobalOrByProduct()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier testCampaignGroups(TestCampaignGroupRepository testCampaignGroupRepository) {
        return new RepositoryReferenceSupplier<>(
                testCampaignGroupRepository,
                ImmutableSet.of(filterByProduct()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier components(TechnicalComponentRepository componentRepository) {
        return new RepositoryReferenceSupplier<>(
                componentRepository,
                ImmutableSet.of(filterByProductInComponents(), filterByFeatureInComponents()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier productFeature(ProductFeatureRepository productFeatureRepository) {
        return new RepositoryReferenceSupplier<>(
                productFeatureRepository,
                ImmutableSet.of(filterByProduct()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier projects(ProjectRepository projectRepository) {
        return new RepositoryReferenceSupplier<>(projectRepository);
    }

    public static RepositoryReferenceSupplier products(ProductRepository productRepository) {
        return new RepositoryReferenceSupplier<>(productRepository);
    }

    public static RepositoryReferenceSupplier types(TestTypeRepository testTypeRepository) {
        return new RepositoryReferenceSupplier<>(
                testTypeRepository,
                ImmutableSet.of(filterByProduct()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier teams(TestTeamRepository testTeamRepository) {
        return new RepositoryReferenceSupplier<>(
                testTeamRepository,
                ImmutableSet.of(filterByFeature(), filterByProductInComponents()),
                getSearchConfigurator()
        );
    }

    public static RepositoryReferenceSupplier suites(TestSuiteRepository testSuiteRepository) {
        return new RepositoryReferenceSupplier<>(
                testSuiteRepository,
                ImmutableSet.of(filterByFeature(), filterByProductInComponents()),
                getSearchConfigurator()
        );
    }

    private static SearchConfigurator getSearchConfigurator() {
        return new SearchConfigurator() {
            @Override
            public void configure(Search search) {
                search.addSortAsc("name");
            }
        };
    }

    private static StringParamFilter filterByFeature() {
        return new StringParamFilter("featureId", new FilterSupplier<String>() {
            @Override
            public Filter supply(List<String> values) {
                return Filter.in("feature.name", values);
            }
        });
    }

    private static StringParamFilter filterByProduct() {
        return new StringParamFilter("productId", new FilterSupplier<String>() {
            @Override
            public Filter supply(List<String> values) {
                return Filter.in("product.externalId", values);
            }
        });
    }

    private static StringParamFilter filterByProductInComponents() {
        return new StringParamFilter("productId", new FilterSupplier<String>() {
            @Override
            public Filter supply(List<String> values) {
                return Filter.in("feature.product.externalId", values);
            }
        });
    }

    private static StringParamFilter filterByFeatureInComponents() {
        return new StringParamFilter("featureId", new FilterSupplier<String>() {
            @Override
            public Filter supply(List<String> values) {
                return Filter.in("feature.name", values);
            }
        });
    }

    private static StringParamFilter filterEnabledGlobalOrByProduct() {
        return new StringParamFilter("productId", new FilterSupplier<String>() {
            @Override
            public Filter supply(List<String> values) {
                return Filter.and(
                        Filter.equal("enabled", true),
                        Filter.or(
                                Filter.in("product.externalId", values),
                                Filter.isNull("product") // Global items
                        )
                );
            }
        });
    }
}
