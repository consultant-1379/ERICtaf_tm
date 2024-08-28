/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.admin;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.ui.login.results.AuthenticationResult;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.User;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.GenericFeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.GenericProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsNot.not;

public class AdminTest extends BaseFuncTest {

    @Test
    @TestId(id = "CIP-14526_Func_1", title = "Add user access to other user")
    public void changeUserAccess() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        String username = "taf2";
        User user = uiTestManagementAdminOperator.changeUserAccess(username);

        CustomAsserts.checkTestStep(loginOperator.logout());
        assertThat(user.getSignum(), equalTo(username));
    }

    @Test
    @TestId(id = "CIP-14526_Func_2", title = "Remove user access")
    public void removeAdminUser() {
        createUiOperators();
        AuthenticationResult result = loginOperator.loginAsAdmin();
        CustomAsserts.checkTestStep(result);
        List<User> users = uiTestManagementAdminOperator.removeUserAccess(result.getUsername());
        CustomAsserts.checkTestStep(loginOperator.logout());

        List<String> collect = users.stream().map(item -> item.getSignum()).collect(Collectors.toList());
        assertThat(collect, not(hasItem(result.getUsername())));
    }

    @Test
    @TestId(id = "CIP-23462_Func_1", title = "Create, edit and delete product")
    public void createEditDeleteProduct() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<ProductInfo> productData = getProductData();
        List<ProductInfo> createdProduct = uiTestManagementAdminOperator.createProduct(productData, "Product");
        List<String> productList = createdProduct.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (ProductInfo productInfo : productData) {
            assertThat(productList, hasItem(productInfo.getName()));
        }
        ProductInfo productInfo = changeProductData();
        ProductInfo changedProductInfo = uiTestManagementAdminOperator.editProduct(productData.get(0), productInfo);
        assertThat(changedProductInfo.getName(), equalTo(productInfo.getName()));

        List<ProductInfo> productInfos = uiTestManagementAdminOperator.deleteProduct(createList(productInfo));
        List<String> products = productInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(products, not(hasItem(productInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-23462_Func_2", title = "Create, edit and delete feature")
    public void createEditDeleteFeature() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericProductInfo> featureData = getFeatureData();
        List<GenericProductInfo> createdFeature = uiTestManagementAdminOperator.createEntityWithProductAndName(featureData, "Features");
        List<String> featureList = createdFeature.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericProductInfo genericInfo : featureData) {
            assertThat(featureList, hasItem(genericInfo.getName()));
        }

        GenericProductInfo featureInfo = changeFeatureData();
        GenericProductInfo changedFeatureInfo = uiTestManagementAdminOperator.editEntityWithProductAndName(featureData.get(0), featureInfo);
        assertThat(changedFeatureInfo.getName(), equalTo(featureInfo.getName()));

        List<GenericProductInfo> featureInfos = uiTestManagementAdminOperator.deleteEntityWithProductAndName(createList(featureInfo));
        List<String> features = featureInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(features, not(hasItem(featureInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());

    }

    @Test
    @TestId(id = "CIP-23462_Func_3", title = "Create, edit and delete technical component")
    public void createEditDeleteTechnicalComponent() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericFeatureInfo> componentsInfo = getComponentData();
        List<GenericFeatureInfo> createdTeams = uiTestManagementAdminOperator.createEntityWithProductFeatureAndName(componentsInfo, "Components");
        List<String> teamNames = createdTeams.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericFeatureInfo genericInfo : componentsInfo) {
            assertThat(teamNames, hasItem(genericInfo.getName()));
        }

        GenericFeatureInfo componentData = changeComponentData();
        GenericFeatureInfo genericInfo = uiTestManagementAdminOperator.editEntityWithProductFeatureAndName(componentsInfo.get(0), componentData);
        assertThat(genericInfo.getName(), equalTo(componentData.getName()));

        List<GenericFeatureInfo> componentInfos = uiTestManagementAdminOperator.deleteEntityWithProductFeatureAndName(createList(componentData));
        List<String> components = componentInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(components, not(hasItem(componentData.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-23462_Func_4", title = "Create, edit and delete test type")
    public void createEditDeleteTestTypes() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericProductInfo> testTypeData = getTestTypeData();
        List<GenericProductInfo> createdGroup = uiTestManagementAdminOperator.createEntityWithProductAndName(testTypeData, "Test Types");
        List<String> testTypeList = createdGroup.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericProductInfo genericInfo : testTypeData) {
            assertThat(testTypeList, hasItem(genericInfo.getName()));
        }

        GenericProductInfo testTypeInfo = changeTestTypeData();
        GenericProductInfo changedTestTypeInfo = uiTestManagementAdminOperator.editEntityWithProductAndName(testTypeData.get(0), testTypeInfo);
        assertThat(changedTestTypeInfo.getName(), equalTo(testTypeInfo.getName()));

        List<GenericProductInfo> testTypeInfos = uiTestManagementAdminOperator.deleteEntityWithProductAndName(createList(testTypeInfo));
        List<String> testTypes = testTypeInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(testTypes, not(hasItem(testTypeInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-9543_Func_1", title = "Create, edit and delete group")
    public void createEditDeleteGroup() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericProductInfo> groupData = getGroupData();
        List<GenericProductInfo> createdGroup = uiTestManagementAdminOperator.createEntityWithProductAndName(groupData, "Groups");
        List<String> groupList = createdGroup.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericProductInfo genericInfo : groupData) {
            assertThat(groupList, hasItem(genericInfo.getName()));
        }

        GenericProductInfo groupInfo = changeGroupData();
        GenericProductInfo changedGroupInfo = uiTestManagementAdminOperator.editEntityWithProductAndName(groupData.get(0), groupInfo);
        assertThat(changedGroupInfo.getName(), equalTo(groupInfo.getName()));

        List<GenericProductInfo> groupInfos = uiTestManagementAdminOperator.deleteEntityWithProductAndName(createList(groupInfo));
        List<String> groups = groupInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(groups, not(hasItem(groupInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-23462_Func_5", title = "Create, edit and delete review group")
    public void createEditDeleteReviewGroup() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<ReviewGroupInfo> reviewGroupData = getReviewGroupData();
        List<ReviewGroupInfo> createdReviewGroups = uiTestManagementAdminOperator.createReviewGroup(reviewGroupData, "Review Group");

        List<String> reviewGroupNames = createdReviewGroups.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(reviewGroupNames, hasItem(reviewGroupData.get(0).getName()));

        Optional<ReviewGroupInfo> createdReviewGroup = createdReviewGroups.stream()
                .filter(reviewGroup -> reviewGroup.getName().equals(reviewGroupData.get(0).getName()))
                .findFirst();
        assertThat(createdReviewGroup.isPresent(), equalTo(true));
        assertThat(createdReviewGroup.get().getUsers().isEmpty(), equalTo(false));
        assertThat(createdReviewGroup.get().getUsers().size(), equalTo(1));

        ReviewGroupInfo reviewGroupInfo = changeReviewGroupData();
        ReviewGroupInfo changedReviewGroupInfo = uiTestManagementAdminOperator.editReviewGroup(reviewGroupInfo);
        //expecting two users to be present in review group
        assertThat(changedReviewGroupInfo.getName(), equalTo(reviewGroupInfo.getName()));
        assertThat(changedReviewGroupInfo.getUsers().isEmpty(), equalTo(false));
        assertThat(changedReviewGroupInfo.getUsers().size(), equalTo(2));

        List<ReviewGroupInfo> reviewGroupInfos = uiTestManagementAdminOperator.deleteReviewGroup(createList(reviewGroupInfo));
        List<String> reviewGroups = reviewGroupInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(reviewGroups, not(hasItem(reviewGroupInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-23462_Func_6", title = "Create, edit and delete project")
    public void createEditDeleteProject() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<ProjectInfo> projectData = getProjectData();
        List<ProjectInfo> createdProjects = uiTestManagementAdminOperator.createProject(projectData, "Projects");
        List<String> projectNames = createdProjects.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (ProjectInfo projectInfo : projectData) {
            assertThat(projectNames, hasItem(projectInfo.getName()));
        }

        ProjectInfo projectInfo = changeProjectData();
        ProjectInfo changedProjectInfo = uiTestManagementAdminOperator.editProject(projectData.get(0), projectInfo);
        assertThat(changedProjectInfo.getName(), equalTo(projectInfo.getName()));

        List<ProjectInfo> projectInfos = uiTestManagementAdminOperator.deleteProject(createList(projectInfo));
        List<String> projects = projectInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(projects, not(hasItem(projectInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-14526_Func_3", title = "Create, edit and delete team")
    public void createEditDeleteTeam() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericFeatureInfo> teamData = getTeamData();
        List<GenericFeatureInfo> createdTeams = uiTestManagementAdminOperator.createEntityWithProductFeatureAndName(teamData, "Teams");
        List<String> teamNames = createdTeams.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericFeatureInfo genericInfo : teamData) {
            assertThat(teamNames, hasItem(genericInfo.getName()));
        }

        GenericFeatureInfo teamInfo = changeTeamData();
        GenericFeatureInfo genericInfo = uiTestManagementAdminOperator.editEntityWithProductFeatureAndName(teamData.get(0), teamInfo);
        assertThat(genericInfo.getName(), equalTo(teamInfo.getName()));

        List<GenericFeatureInfo> teamInfos = uiTestManagementAdminOperator.deleteEntityWithProductFeatureAndName(createList(teamInfo));
        List<String> teams = teamInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(teams, not(hasItem(teamInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());

    }

    @Test
    @TestId(id = "CIP-14526_Func_4", title = "Create, edit and delete suites")
    public void createEditDeleteSuite() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericFeatureInfo> suiteData = getSuiteData();
        List<GenericFeatureInfo> createdSuites = uiTestManagementAdminOperator.createEntityWithProductFeatureAndName(suiteData, "Suites");
        List<String> teamNames = createdSuites.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericFeatureInfo genericInfo : suiteData) {
            assertThat(teamNames, hasItem(genericInfo.getName()));
        }

        GenericFeatureInfo suiteInfo = changeSuiteData();
        GenericFeatureInfo genericInfo = uiTestManagementAdminOperator.editEntityWithProductFeatureAndName(suiteData.get(0), suiteInfo);
        assertThat(genericInfo.getName(), equalTo(suiteInfo.getName()));

        List<GenericFeatureInfo> suiteInfos = uiTestManagementAdminOperator.deleteEntityWithProductFeatureAndName(createList(suiteInfo));
        List<String> suites = suiteInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(suites, not(hasItem(suiteInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());

    }

    @Test
    @TestId(id = "CIP-23462_Func_7", title = "Create, edit and delete test campaign group")
    public void createEditDeleteTestCampaignGroup() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericProductInfo> testCamapignData = getTestCampaignData();
        List<GenericProductInfo> createdFeature = uiTestManagementAdminOperator.createEntityWithProductAndName(testCamapignData, "Test Campaign Group");
        List<String> testCampaignList = createdFeature.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericProductInfo genericInfo : testCamapignData) {
            assertThat(testCampaignList, hasItem(genericInfo.getName()));
        }

        GenericProductInfo testCampaignInfo = changeTestCampaignData();
        GenericProductInfo changedTestCampaignInfo = uiTestManagementAdminOperator.editEntityWithProductAndName(testCamapignData.get(0), testCampaignInfo);
        assertThat(changedTestCampaignInfo.getName(), equalTo(testCampaignInfo.getName()));

        List<GenericProductInfo> featureInfos = uiTestManagementAdminOperator.deleteEntityWithProductAndName(createList(testCampaignInfo));
        List<String> testCampaigns = featureInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(testCampaigns, not(hasItem(testCampaignInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-23462_Func_8", title = "Create, edit and delete product drop")
    public void createEditDeleteProductDrop() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.loginAsAdmin());
        List<GenericProductInfo> dropData = getDropData();
        List<GenericProductInfo> createdFeature = uiTestManagementAdminOperator.createEntityWithProductAndName(dropData, "Product Drop");
        List<String> testCampaignList = createdFeature.stream().map(item -> item.getName()).collect(Collectors.toList());
        for (GenericProductInfo genericInfo : dropData) {
            assertThat(testCampaignList, hasItem(genericInfo.getName()));
        }

        GenericProductInfo dropInfo = changeDropData();
        GenericProductInfo changedDropInfo = uiTestManagementAdminOperator.editEntityWithProductAndName(dropData.get(0), dropInfo);
        assertThat(changedDropInfo.getName(), equalTo(dropInfo.getName()));

        List<GenericProductInfo> dropInfos = uiTestManagementAdminOperator.deleteEntityWithProductAndName(createList(dropInfo));
        List<String> drops = dropInfos.stream().map(item -> item.getName()).collect(Collectors.toList());
        assertThat(drops, not(hasItem(dropInfo.getName())));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    private List<GenericFeatureInfo> getComponentData() {
        List<GenericFeatureInfo> components = new ArrayList();
        GenericFeatureInfo componentInfo = new TechnicalComponentInfo();
        componentInfo.setName("new component");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("Other");
        featureInfo.setProduct(productInfo);
        componentInfo.setFeature(featureInfo);
        components.add(componentInfo);

        return components;
    }

    private GenericFeatureInfo changeComponentData() {
        GenericFeatureInfo componentInfo = new TeamInfo();
        componentInfo.setName("even newer component");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("FM");
        featureInfo.setProduct(productInfo);
        componentInfo.setFeature(featureInfo);

        return componentInfo;
    }

    private List<GenericFeatureInfo> getTeamData() {
        List<GenericFeatureInfo> teams = new ArrayList();
        GenericFeatureInfo teamInfo = new TeamInfo();
        teamInfo.setName("Supersonic");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("Other");
        featureInfo.setProduct(productInfo);
        teamInfo.setFeature(featureInfo);
        teams.add(teamInfo);

        return teams;
    }

    private GenericFeatureInfo changeTeamData() {
        GenericFeatureInfo teamInfo = new TeamInfo();
        teamInfo.setName("Dodgers");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("FM");
        featureInfo.setProduct(productInfo);
        teamInfo.setFeature(featureInfo);

        return teamInfo;
    }

    private List<GenericFeatureInfo> getSuiteData() {
        List<GenericFeatureInfo> suites = new ArrayList();
        GenericFeatureInfo suiteInfo = new SuiteInfo();
        suiteInfo.setName("test_suite");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("Other");
        featureInfo.setProduct(productInfo);
        suiteInfo.setFeature(featureInfo);

        suites.add(suiteInfo);
        return suites;
    }

    private GenericFeatureInfo changeSuiteData() {
        GenericFeatureInfo teamInfo = new SuiteInfo();
        teamInfo.setName("test_suite_changed");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName("FM");
        featureInfo.setProduct(productInfo);
        teamInfo.setFeature(featureInfo);

        return teamInfo;
    }

    private List<GenericProductInfo> getFeatureData() {
        List<GenericProductInfo> features = new ArrayList();
        GenericProductInfo featureInfo = new FeatureInfo();
        featureInfo.setName("new_Feature");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        featureInfo.setProduct(productInfo);
        features.add(featureInfo);

        return features;
    }

    private GenericProductInfo changeFeatureData() {
        GenericProductInfo featureInfo = new FeatureInfo();
        featureInfo.setName("updated feature");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        featureInfo.setProduct(productInfo);

        return featureInfo;
    }

    private List<GenericProductInfo> getTestTypeData() {
        List<GenericProductInfo> testTypes = new ArrayList();
        GenericProductInfo testTypeInfo = new TestTypeInfo();
        testTypeInfo.setName("manual");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        testTypeInfo.setProduct(productInfo);
        testTypes.add(testTypeInfo);

        return testTypes;
    }

    private GenericProductInfo changeTestTypeData() {
        GenericProductInfo testTypeInfo = new TestTypeInfo();
        testTypeInfo.setName("changed_test_type");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        testTypeInfo.setProduct(productInfo);

        return testTypeInfo;
    }

    private List<GenericProductInfo> getGroupData() {
        List<GenericProductInfo> groups = new ArrayList();
        GenericProductInfo groupInfo = new GroupInfo();
        groupInfo.setName("new_group");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        groupInfo.setProduct(productInfo);
        groups.add(groupInfo);

        return groups;
    }

    private GenericProductInfo changeGroupData() {
        GenericProductInfo groupInfo = new GroupInfo();
        groupInfo.setName("changed_group");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        groupInfo.setProduct(productInfo);

        return groupInfo;
    }

    private List<ReviewGroupInfo> getReviewGroupData() {
        List<ReviewGroupInfo> reviewGroups = new ArrayList();
        ReviewGroupInfo reviewGroupInfo = new ReviewGroupInfo();
        reviewGroupInfo.setName("TAF Review group");
        reviewGroups.add(reviewGroupInfo);

        Set<UserInfo> users = new HashSet<>();
        UserInfo user = new UserInfo();
        user.setUserId("tafUser tafSurname");
        user.setUserName("tafUser tafSurname");
        users.add(user);
        reviewGroupInfo.setUsers(users);

        return reviewGroups;
    }

    private ReviewGroupInfo changeReviewGroupData() {
        ReviewGroupInfo reviewGroupInfo = new ReviewGroupInfo();
        reviewGroupInfo.setName("TAF_UPDATED_REVIEW_GROUP");

        Set<UserInfo> users = new HashSet<>();
        UserInfo user = new UserInfo();
        user.setUserId("tafUser2 tafSurname2");
        user.setUserName("tafUser2 tafSurname2");
        users.add(user);
        reviewGroupInfo.setUsers(users);
        return reviewGroupInfo;
    }

    private List<ProjectInfo> getProjectData() {
        List<ProjectInfo> projects = new ArrayList<>();
        ProjectInfo project = new ProjectInfo();
        ProductInfo product = new ProductInfo();
        product.setName("ENM");
        product.setExternalId("ENM");

        project.setProduct(product);
        project.setName("New_Project");
        project.setExternalId("Proj");

        projects.add(project);

        return projects;
    }

    private ProjectInfo changeProjectData() {
        ProjectInfo project = new ProjectInfo();
        ProductInfo product = new ProductInfo();
        product.setName("ENM");
        product.setExternalId("ENM");

        project.setProduct(product);
        project.setName("Updated_Project");
        project.setExternalId("Project Updated");

        return project;
    }

    private List<GenericProductInfo> getTestCampaignData() {
        List<GenericProductInfo> testCampaigns = new ArrayList();
        GenericProductInfo testCampaignInfo = new TestCampaignGroupInfo();
        testCampaignInfo.setName("new_test_camapign");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        testCampaignInfo.setProduct(productInfo);
        testCampaigns.add(testCampaignInfo);

        return testCampaigns;
    }

    private GenericProductInfo changeTestCampaignData() {
        GenericProductInfo testCampaignInfo = new TestCampaignGroupInfo();
        testCampaignInfo.setName("changed_test_campaign");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        testCampaignInfo.setProduct(productInfo);

        return testCampaignInfo;
    }

    private List<GenericProductInfo> getDropData() {
        List<GenericProductInfo> drops = new ArrayList();
        GenericProductInfo dropInfo = new DropInfo();
        dropInfo.setName("20.10");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        dropInfo.setProduct(productInfo);
        drops.add(dropInfo);

        return drops;
    }

    private GenericProductInfo changeDropData() {
        GenericProductInfo dropInfo = new DropInfo();
        dropInfo.setName("20.10A");
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("ENM");
        dropInfo.setProduct(productInfo);

        return dropInfo;
    }

    private List<ProductInfo> getProductData() {
        List<ProductInfo> products = new ArrayList();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("New Product");
        productInfo.setExternalId("77");
        products.add(productInfo);

        return products;
    }

    private ProductInfo changeProductData() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("Changed Product");
        productInfo.setExternalId("77");

        return productInfo;
    }

    private List<GenericFeatureInfo> createList(GenericFeatureInfo... teamInfo) {
        List<GenericFeatureInfo> teams = new ArrayList();
        for (GenericFeatureInfo team : teamInfo) {
            teams.add(team);
        }
        return teams;
    }

    private List<GenericProductInfo> createList(GenericProductInfo... groupInfo) {
        List<GenericProductInfo> group = new ArrayList();
        for (GenericProductInfo entity : groupInfo) {
            group.add(entity);
        }
        return group;
    }

    private List<ProductInfo> createList(ProductInfo... productInfo) {
        List<ProductInfo> products = new ArrayList();
        for (ProductInfo product : productInfo) {
            products.add(product);
        }
        return products;
    }

    private List<ProjectInfo> createList(ProjectInfo... projectInfos) {
        List<ProjectInfo> projects = new ArrayList();
        for (ProjectInfo project : projectInfos) {
            projects.add(project);
        }
        return projects;
    }

    private List<ReviewGroupInfo> createList(ReviewGroupInfo... reviewGroupInfos) {
        List<ReviewGroupInfo> reviewGroups = new ArrayList();
        for (ReviewGroupInfo reviewGroup : reviewGroupInfos) {
            reviewGroups.add(reviewGroup);
        }
        return reviewGroups;
    }
}
