/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.requirements.ExternalRequirementType;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Context;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.resources.ReferenceResource;
import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.RepositoryReferenceSuppliers;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Map;

@Controller
public class ReferenceController implements ReferenceResource {

    private final Map<String, ReferenceSupplier> referenceSuppliers;

    @javax.ws.rs.core.Context
    private UriInfo uriInfo;

    @Inject
    public ReferenceController(
            ScopeRepository scopeRepository,
            TechnicalComponentRepository componentRepository,
            ProductRepository productRepository,
            ProductFeatureRepository productFeatureRepository,
            ProjectRepository projectRepository,
            TestTypeRepository testTypeRepository,
            TestSuiteRepository testSuiteRepository,
            TestTeamRepository testTeamRepository,
            TestCampaignGroupRepository testCampaignGroupRepository) {
        referenceSuppliers = ImmutableMap.<String, ReferenceSupplier>builder()
                .put("project", RepositoryReferenceSuppliers.projects(projectRepository))
                .put("product", RepositoryReferenceSuppliers.products(productRepository))
                .put("feature", RepositoryReferenceSuppliers.productFeature(productFeatureRepository))
                .put("priority", new EnumReferenceSupplier<>(Priority.class))
                .put("jiraIssueType", new EnumReferenceSupplier<>(ExternalRequirementType.class))
                .put("context", new EnumReferenceSupplier<>(Context.class))
                .put("group", RepositoryReferenceSuppliers.groups(scopeRepository))
                .put("type", RepositoryReferenceSuppliers.types(testTypeRepository))
                .put("suite", RepositoryReferenceSuppliers.suites(testSuiteRepository))
                .put("team", RepositoryReferenceSuppliers.teams(testTeamRepository))
                .put("executionType", new EnumReferenceSupplier<>(TestExecutionType.class))
                .put("executionResult", new EnumReferenceSupplier<>(TestExecutionResult.class))
                .put("testCaseStatus", new EnumReferenceSupplier<>(TestCaseStatus.class))
                .put("component", RepositoryReferenceSuppliers.components(componentRepository))
                .put("campaignGroup", RepositoryReferenceSuppliers.testCampaignGroups(testCampaignGroupRepository))
                .build();
    }

    @Override
    public Response getReferences(List<String> referenceDataIds) {
        List<ReferenceData> referenceDataList = Lists.newArrayList();
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String referenceDataId : referenceDataIds) {
            ReferenceSupplier supplier = referenceSuppliers.get(referenceDataId);
            if (supplier == null) {
                String message = String.format("Reference data '%s' is not supported", referenceDataId);
                return Responses.badRequest(message);
            }
            List<ReferenceDataItem> referenceDataItems = supplier.get(queryParameters);
            ReferenceData referenceData = new ReferenceData(referenceDataId, referenceDataItems);
            referenceDataList.add(referenceData);
        }
        return Responses.ok(referenceDataList);
    }
}
