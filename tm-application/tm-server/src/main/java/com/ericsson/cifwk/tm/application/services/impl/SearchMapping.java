package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.domain.model.shared.search.field.BooleanQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.CompositeQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.EnumQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.LongQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.NameWithIdQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.NamedStringQueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.StringQueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.Map;

@Singleton
public final class SearchMapping {

    private final Map<String, QueryField> testCaseFields;
    private final Map<String, QueryField> testPlanFields;
    private final Map<String, QueryField> testPlanItemFields;
    private final Map<String, QueryField> detailedTestPlanItemFields;
    private final Map<String, QueryField> featureFields;
    private final Map<String, QueryField> productFields;
    private final Map<String, QueryField> userProfileFields;
    private final Map<String, QueryField> reviewGroupFields;
    private final Map<String, QueryField> testCampaignGroupFields;
    private final Map<String, QueryField> dropFields;

    @Inject
    public SearchMapping() {
        testCaseFields = testCaseMapping();
        testPlanFields = testPlanMapping();
        testPlanItemFields = testPlanItemMapping();
        detailedTestPlanItemFields = detailedTestPlanItemMapping();
        featureFields = featureMapping();
        userProfileFields = userProfileMapping();
        productFields = productMapping();
        reviewGroupFields = reviewGroupMapping();
        testCampaignGroupFields = testCampaignGroupMapping();
        dropFields = dropMapping();
    }

    private Map<String, QueryField> testCaseMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("id", new LongQueryField("id"))
                .put("testCaseId", new StringQueryField("testCaseId"))
                .put("title", new StringQueryField("currentVersion.title"))
                .put("description", new StringQueryField("currentVersion.description"))
                .put("type", new StringQueryField("currentVersion.type.name"))
                .put("type.title", new StringQueryField("currentVersion.type.name"))
                .put("priority", new EnumQueryField<>("currentVersion.priority", Priority.class))
                .put("executionType", new NameWithIdQueryField<>("currentVersion.executionType",
                        TestExecutionType.class))
                .put("requirementId", new StringQueryField("currentVersion.requirements.externalId"))
                .put("requirements", new StringQueryField("currentVersion.requirements.externalId"))
                .put("requirementLabel", new StringQueryField("currentVersion.requirements.externalLabel"))
                .put("component", new StringQueryField("currentVersion.technicalComponents.name"))
                .put("group", new StringQueryField("currentVersion.scopes.name"))
                .put("groups.title", new StringQueryField("currentVersion.scopes.name"))
                .put("projectId", new StringQueryField("currentVersion.requirements.project.externalId"))
                .put("projectName", new StringQueryField("currentVersion.requirements.project.name"))
                .put("featureName", new StringQueryField("currentVersion.productFeature.name"))
                .put("productName", new StringQueryField("currentVersion.requirements.project.product.externalId"))
                .put("updatedBy", new StringQueryField("updatedByUser.externalId"))
                .put("reviewGroup", new StringQueryField("currentVersion.reviewGroup.users.externalId"))
                .put("reviewUser", new StringQueryField("currentVersion.reviewUser.externalId"))
                .put("status", new NameWithIdQueryField<>("currentVersion.testCaseStatus",
                        TestCaseStatus.class))
                .build();
    }

    private Map<String, QueryField> testPlanMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("id", new LongQueryField("id"))
                .put("name", new StringQueryField("name"))
                .put("description", new StringQueryField("description"))
                .put("environment", new StringQueryField("environment"))
                .put("productName", new StringQueryField("features.product.externalId"))
                .put("featureName", new StringQueryField("features.name"))
                .put("dropName", new StringQueryField("drop.name"))
                .build();
    }

    private Map<String, QueryField> testPlanItemMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("id", new LongQueryField("id"))
                .put("testPlan.title", new StringQueryField("testPlan.name"))
                .put("testCase.testCaseId", new StringQueryField("testCaseVersion.testCase.testCaseId"))
                .put("testCase.title", new StringQueryField("testCaseVersion.title"))
                .build();
    }

    private Map<String, QueryField> testCampaignGroupMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("id", new LongQueryField("id"))
                .put("name", new StringQueryField("name"))
                .put("product", new StringQueryField("product.externalId"))
                .build();
    }


    private Map<String, QueryField> detailedTestPlanItemMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("testCaseId", new StringQueryField("testCaseVersion.testCase.testCaseId"))
                .put("title", new StringQueryField("testCaseVersion.title"))
                .put("description", new StringQueryField("testCaseVersion.description"))
                .put("type", new StringQueryField("currentVersion.type.name"))
                .put("priority", new EnumQueryField<>("testCaseVersion.priority", Priority.class))
                .put("executionType", new NameWithIdQueryField<>("testCaseVersion.executionType",
                        TestExecutionType.class))
                .put("requirementId", new StringQueryField("testCaseVersion.requirements.externalId"))
                .put("requirementTitle", new StringQueryField("testCaseVersion.requirements.externalTitle"))
                .put("component", new CompositeQueryField(
                        new StringQueryField("testCaseVersion.technicalComponent.name"),
                        new NamedStringQueryField("testCaseVersion.optionalFields", TestField.COMPONENT)
                ))
                .put("group", new StringQueryField("testCaseVersion.scopes.name"))
                .put("projectId", new StringQueryField("testCaseVersion.requirements.project.externalId"))
                .put("projectName", new StringQueryField("testCaseVersion.requirements.project.name"))
                .put("updatedBy", new StringQueryField("testCaseVersion.testCase.updatedByUser.externalId"))
                .put("user", new StringQueryField("user.userName"))
                .put("status", new NameWithIdQueryField<>("testCaseVersion.testCaseStatus",
                        TestCaseStatus.class))
                .build();
    }

    private Map<String, QueryField> featureMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("product", new StringQueryField("feature.product.externalId"))
                .put("feature", new StringQueryField("feature.name"))
                .put("name", new StringQueryField("name"))
                .put("id", new LongQueryField("id"))
                .build();
    }

    private Map<String, QueryField> productMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("product", new StringQueryField("product.externalId"))
                .put("name", new StringQueryField("name"))
                .put("id", new LongQueryField("id"))
                .build();
    }

    private Map<String, QueryField> userProfileMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("administrator", new BooleanQueryField("administrator"))
                .put("product", new StringQueryField("product.name"))
                .put("project", new StringQueryField("project.name"))
                .put("userId", new StringQueryField("user.externalId"))
                .put("userName", new StringQueryField("user.userName"))
                .build();
    }

    private Map<String, QueryField> reviewGroupMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("name", new StringQueryField("name"))
                .build();
    }

    private Map<String, QueryField> dropMapping() {
        return ImmutableMap.<String, QueryField>builder()
                .put("product", new StringQueryField("product.externalId"))
                .put("name", new StringQueryField("name"))
                .put("id", new LongQueryField("id"))
                .build();
    }

    public Map<String, QueryField> getTestCaseFields() {
        return testCaseFields;
    }

    public Map<String, QueryField> getTestPlanFields() {
        return testPlanFields;
    }

    public Map<String, QueryField> getTestPlanItemFields() {
        return testPlanItemFields;
    }

    public Map<String, QueryField> getDetailedTestPlanItemFields() {
        return detailedTestPlanItemFields;
    }

    public Map<String, QueryField> getFeatureFields() {
        return featureFields;
    }

    public Map<String, QueryField> getUserProfileFields() {
        return userProfileFields;
    }

    public Map<String, QueryField> getProductFields() {
        return productFields;
    }

    public Map<String, QueryField> getReviewGroupFields() {
        return reviewGroupFields;
    }

    public Map<String, QueryField> getTestCampaignGroupFields() {
        return testCampaignGroupFields;
    }

    public Map<String, QueryField> getDropFields() {
        return dropFields;
    }
}
