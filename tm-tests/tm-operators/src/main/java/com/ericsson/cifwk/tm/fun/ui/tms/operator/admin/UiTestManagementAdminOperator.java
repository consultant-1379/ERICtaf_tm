package com.ericsson.cifwk.tm.fun.ui.tms.operator.admin;

import com.ericsson.cds.uisdk.compositecomponents.UiSdkSelectBox;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.tm.fun.ui.common.TableHelper;
import com.ericsson.cifwk.tm.fun.ui.common.operator.UiCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.User;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.AdminRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.GenericProductWidgetViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.GenericWidgetViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.ProductWidgetExternalIdViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.ProductWidgetViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.ReviewGroupWidgetViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.admin.UserAccessViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.DialogViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.TopBarViewModel;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.GenericFeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.GenericProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ericsson.cifwk.tm.fun.ui.common.TimingHelper.sleep;
import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.selectBox.SelectBoxHelper.setSelectBox;

public class UiTestManagementAdminOperator extends UiCommonOperator {

    private static final Logger logger = LoggerFactory.getLogger(UiTestManagementAdminOperator.class);

    private String appUrl;

    @TestStep(id = "start", description = "start TM operator on host {0}")
    public void start(Host host) {
        screenShotDirPath = createScreenshotDir();

        appUrl = getAppUrl(host);
        navigation.setup(appUrl);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @TestStep(id = "ChangeUserAccess", description = "Change user access role of another user to administrator.")
    public User changeUserAccess(String username) {
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab("User Access");

        UserAccessViewModel userAccessViewModel = getBrowserTab().getView(UserAccessViewModel.class);
        UiComponent name = userAccessViewModel.getName();
        name.sendKeys(username);

        UiComponent multiSelectList = userAccessViewModel.getMultiselectList();
        multiSelectList.getDescendantsBySelector(UserAccessViewModel.COMPONENT_ITEM).get(0).click();

        userAccessViewModel.getAddButton().click();

        UiComponent table = userAccessViewModel.getTable();
        getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, username) != null);
        UiComponent user = TableHelper.findTableRow(table, username);

        return getUserData(user);
    }

    @TestStep(id = "RemoveUserAccess", description = "Remove user access role.")
    public List<User> removeUserAccess(String username) {
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab("User Access");
        UserAccessViewModel userAccessViewModel = getBrowserTab().getView(UserAccessViewModel.class);

        UiComponent table = userAccessViewModel.getTable();
        Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(table, username));
        if (row.isPresent()) {
            row.get().getDescendantsBySelector("i").get(0).click();
            clickConfirmDeleteDialog();
        }

        List<User> userAccessTableData = getUserAccessTableData(table);
        return userAccessTableData;
    }


    public List<ProductInfo> createProduct(List<ProductInfo> productInfos, String tabName) {
        List<ProductInfo> productInfoResults = new ArrayList<>();
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab(tabName);
        ProductWidgetViewModel productWidgetView = getBrowserTab().getView(ProductWidgetViewModel.class);
        UiComponent table = productWidgetView.getTable();

        for (ProductInfo productInfo : productInfos) {
            populateProductWidget(productWidgetView, productInfo);
            productWidgetView.getAddButton().click();

            getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, productInfo.getName()) != null);

            UiComponent entity = TableHelper.findTableRow(table, productInfo.getName());
            ProductInfo productData = getProductData(entity);
            productInfoResults.add(productData);
        }
        return productInfoResults;
    }

    public List<ProductInfo> deleteProduct(List<ProductInfo> productInfos) {
        ProductWidgetViewModel productWidgetViewModel = getBrowserTab().getView(ProductWidgetViewModel.class);

        try {
            UiComponent table = productWidgetViewModel.getTable();

            for (ProductInfo productInfo : productInfos) {
                Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(table, productInfo.getName()));
                if (row.isPresent()) {
                    row.get().getDescendantsBySelector("i").get(0).click();
                    clickConfirmDeleteDialog();
                }
            }
            return getProductTableData(table);
        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("deleteEntityWithProductFeatureAndName", e.getMessage());
            return Collections.emptyList();
        }

    }

    public ProductInfo editProduct(ProductInfo oldEntityInfo, ProductInfo newEntityInfo) {
        ProductWidgetViewModel productWidgetViewModel = getBrowserTab().getView(ProductWidgetViewModel.class);

        UiComponent table = productWidgetViewModel.getTable();
        UiComponent tableRow = TableHelper.findTableRow(table, oldEntityInfo.getName());
        tableRow.focus();
        tableRow.click();

        populateProductWidget(productWidgetViewModel, newEntityInfo);
        createScreenshot("updated details should be present", "");
        UiComponent editButton = productWidgetViewModel.getEditButton();
        getBrowserTab().waitUntilComponentIsDisplayed(editButton, WAIT_SHORT_TIMEOUT);
        editButton.focus();
        editButton.click();

        getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, newEntityInfo.getName()) != null);

        UiComponent entity = TableHelper.findTableRow(table, newEntityInfo.getName());
        ProductInfo productData = getProductData(entity);

        return productData;
    }

    public List<ProjectInfo> createProject(List<ProjectInfo> projectInfos, String tabName) {
        List<ProjectInfo> projectInfosResults = new ArrayList();
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab(tabName);
        ProductWidgetExternalIdViewModel productWidgetExternalIdViewModel = getBrowserTab().getView(ProductWidgetExternalIdViewModel.class);

        UiComponent table = productWidgetExternalIdViewModel.getTable();
        for (ProjectInfo projectInfo : projectInfos) {
            populateProductWidgetExternalId(productWidgetExternalIdViewModel, projectInfo);
            productWidgetExternalIdViewModel.getAddButton().click();

            getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, projectInfo.getName()) != null);

            UiComponent entity = TableHelper.findTableRow(table, projectInfo.getName());
            ProjectInfo projectData = getProjectData(entity);
            projectInfosResults.add(projectData);
        }

        return projectInfosResults;
    }

    public ProjectInfo editProject(ProjectInfo oldEntityInfo, ProjectInfo newEntityInfo) {
        ProductWidgetExternalIdViewModel productWidgetExternalIdViewModel = getBrowserTab().getView(ProductWidgetExternalIdViewModel.class);

        UiComponent table = productWidgetExternalIdViewModel.getTable();
        UiComponent tableRow = TableHelper.findTableRow(table, oldEntityInfo.getName());
        tableRow.focus();
        tableRow.click();

        populateProductWidgetExternalId(productWidgetExternalIdViewModel, newEntityInfo);
        UiComponent editButton = productWidgetExternalIdViewModel.getEditButton();
        getBrowserTab().waitUntilComponentIsDisplayed(editButton, WAIT_SHORT_TIMEOUT);
        editButton.focus();
        editButton.click();

        getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, newEntityInfo.getName()) != null);

        UiComponent project = TableHelper.findTableRow(table, newEntityInfo.getName());
        ProjectInfo projectData = getProjectData(project);

        return projectData;
    }

    public List<ProjectInfo> deleteProject(List<ProjectInfo> projectInfos) {
        ProductWidgetExternalIdViewModel productWidgetExternalIdViewModel = getBrowserTab().getView(ProductWidgetExternalIdViewModel.class);

        try {
            UiComponent table = productWidgetExternalIdViewModel.getTable();

            for (ProjectInfo projectInfo : projectInfos) {
                Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(table, projectInfo.getName()));
                if (row.isPresent()) {
                    row.get().getDescendantsBySelector("i").get(0).click();
                    clickConfirmDeleteDialog();
                }
            }
            return getProjectTableData(table);
        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("deleteEntityWithProductFeatureAndName", e.getMessage());
            return Collections.emptyList();
        }

    }

    @TestStep(id = "createGenericAdmin", description = "Create generic widget items")
    public List<GenericFeatureInfo> createEntityWithProductFeatureAndName(List<GenericFeatureInfo> entityInfos, String tabName) {
        List<GenericFeatureInfo> entityInfosResults = new ArrayList();
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab(tabName);
        GenericWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericWidgetViewModel.class);

        UiComponent table = genericWidgetViewModel.getTable();
        for (GenericFeatureInfo entityInfo : entityInfos) {
            populateGenericWidget(genericWidgetViewModel, entityInfo);
            genericWidgetViewModel.getAddButton().click();

            getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, entityInfo.getName()) != null);

            UiComponent entity = TableHelper.findTableRow(table, entityInfo.getName());
            GenericFeatureInfo teamData = getTeamData(entity);
            entityInfosResults.add(teamData);
        }

        return entityInfosResults;
    }

    @TestStep(id = "editGenericAdmin", description = "edit generic widget item")
    public GenericFeatureInfo editEntityWithProductFeatureAndName(GenericFeatureInfo oldEntityInfo, GenericFeatureInfo newEntityInfo) {
        GenericWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericWidgetViewModel.class);

        UiComponent table = genericWidgetViewModel.getTable();
        UiComponent tableRow = TableHelper.findTableRow(table, oldEntityInfo.getName());
        tableRow.focus();
        tableRow.click();

        populateGenericWidget(genericWidgetViewModel, newEntityInfo);
        createScreenshot("updated details should be present", "");
        UiComponent editButton = genericWidgetViewModel.getEditButton();
        getBrowserTab().waitUntilComponentIsDisplayed(editButton, WAIT_SHORT_TIMEOUT);
        editButton.focus();
        editButton.click();

        getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, newEntityInfo.getName()) != null);

        UiComponent user = TableHelper.findTableRow(table, newEntityInfo.getName());
        GenericFeatureInfo teamData = getTeamData(user);

        return teamData;
    }

    @TestStep(id = "deleteGenericAdmin", description = "delete generic widget items")
    public List<GenericFeatureInfo> deleteEntityWithProductFeatureAndName(List<GenericFeatureInfo> entityInfos) {
        GenericWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericWidgetViewModel.class);

        try {
            UiComponent table = genericWidgetViewModel.getTable();

            for (GenericFeatureInfo entityInfo : entityInfos) {
                Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(table, entityInfo.getName()));
                if (row.isPresent()) {
                    row.get().getDescendantsBySelector("i").get(0).click();
                    clickConfirmDeleteDialog();
                }
            }
            return getGenericTableData(table);
        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("deleteEntityWithProductFeatureAndName", e.getMessage());
            return Collections.emptyList();
        }

    }

    @TestStep(id = "navigateToUserAdminPage", description = "Open Administrator page")
    public AdminRegionViewModel navigateToUserAdminPage() {
        TopBarViewModel topBarViewModel = getBrowserTab().getView(TopBarViewModel.class);
        Link userLink = topBarViewModel.getUserLink();
        UiComponent tooltip = topBarViewModel.getToolTip();
        getBrowserTab().waitUntilComponentIsDisplayed(userLink, WAIT_TIMEOUT);
        hideTooltip(tooltip, userLink);
        userLink.click();

        UiComponent adminLink = topBarViewModel.getAdminLink();
        adminLink.focus();
        adminLink.click();

        AdminRegionViewModel adminRegionViewModel = getBrowserTab().getView(AdminRegionViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(adminRegionViewModel.getAdminRegion(), WAIT_LONG_TIMEOUT);
        return adminRegionViewModel;
    }

    public List<ReviewGroupInfo> createReviewGroup(List<ReviewGroupInfo> reviewGroupInfos, String tabName) {
        List<ReviewGroupInfo> reviewGroupResults = new ArrayList<>();
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab(tabName);
        ReviewGroupWidgetViewModel reviewGroupWidgetViewModel = getBrowserTab().getView(ReviewGroupWidgetViewModel.class);

        UiComponent reviewGroupTable = reviewGroupWidgetViewModel.getReviewGroupTable();
        for (ReviewGroupInfo reviewGroupInfo : reviewGroupInfos) {
            populateReviewGroupWidget(reviewGroupWidgetViewModel, reviewGroupInfo);
            reviewGroupWidgetViewModel.getAddButton().click();

            waitForTableUpdate(reviewGroupWidgetViewModel, ReviewGroupWidgetViewModel.REVIEW_GROUP_TABLE);

            String reviewGroupName = reviewGroupInfo.getName();
            getBrowserTab().waitUntil(reviewGroupTable, (d) -> TableHelper.findTableRow(d, reviewGroupName) != null);

            UiComponent reviewGroup = TableHelper.findTableRow(reviewGroupTable, reviewGroupName);
            reviewGroup.click();

            ReviewGroupInfo reviewGroupData = getReviewGroupData(reviewGroup, reviewGroupWidgetViewModel);
            reviewGroupResults.add(reviewGroupData);
        }

        return reviewGroupResults;
    }

    public ReviewGroupInfo editReviewGroup(ReviewGroupInfo newReviewGroupInfo) {
        ReviewGroupWidgetViewModel reviewGroupWidgetViewModel = getBrowserTab().getView(ReviewGroupWidgetViewModel.class);

        UiComponent reviewGroupTable = reviewGroupWidgetViewModel.getReviewGroupTable();

        populateReviewGroupWidget(reviewGroupWidgetViewModel, newReviewGroupInfo);
        UiComponent editButton = reviewGroupWidgetViewModel.getEditButton();
        getBrowserTab().waitUntilComponentIsDisplayed(editButton, WAIT_SHORT_TIMEOUT);
        editButton.focus();
        editButton.click();

        getBrowserTab().waitUntil(reviewGroupTable, (d) -> TableHelper.findTableRow(d, newReviewGroupInfo.getName()) != null);

        UiComponent reviewGroup = TableHelper.findTableRow(reviewGroupTable, newReviewGroupInfo.getName());
        ReviewGroupInfo reviewGroupData = getReviewGroupData(reviewGroup, reviewGroupWidgetViewModel);

        return reviewGroupData;
    }

    public List<ReviewGroupInfo> deleteReviewGroup(List<ReviewGroupInfo> reviewGroupInfos) {
        ReviewGroupWidgetViewModel reviewGroupWidgetViewModel = getBrowserTab().getView(ReviewGroupWidgetViewModel.class);

        UiComponent reviewGroupTable = reviewGroupWidgetViewModel.getReviewGroupTable();
        for (ReviewGroupInfo reviewGroupInfo : reviewGroupInfos) {
            Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(reviewGroupTable, reviewGroupInfo.getName()));
            if (row.isPresent()) {
                row.get().getDescendantsBySelector("i").get(0).click();
                clickConfirmDeleteDialog();
            }
        }

        return getReviewGroupTableData(reviewGroupTable, reviewGroupWidgetViewModel);
    }

    private void populateReviewGroupWidget(ReviewGroupWidgetViewModel reviewGroupWidgetViewModel, ReviewGroupInfo reviewGroupInfo) {
        TextBox userId = reviewGroupWidgetViewModel.getName();
        userId.clear();
        userId.focus();
        Set<UserInfo> users = reviewGroupInfo.getUsers();
        String username = "";
        for (UserInfo user : users) {
            username = user.getUserId();
        }
        userId.sendKeys(username);
        userId.sendKeys(Keys.RETURN);

        TextBox groupName = reviewGroupWidgetViewModel.getGroup();
        groupName.clear();
        groupName.focus();
        groupName.sendKeys(reviewGroupInfo.getName());
    }

    @TestStep(id = "createEntityWithProductAndName", description = "Create/edit/delete group")
    public List<GenericProductInfo> createEntityWithProductAndName(List<GenericProductInfo> groupInfos, String tabName) {
        List<GenericProductInfo> teamInfosResult = new ArrayList();
        AdminRegionViewModel adminRegionViewModel = navigateToUserAdminPage();
        adminRegionViewModel.clickTab(tabName);
        GenericProductWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericProductWidgetViewModel.class);

        UiComponent table = genericWidgetViewModel.getTable();
        for (GenericProductInfo groupInfo : groupInfos) {
            populateGenericProductWidget(genericWidgetViewModel, groupInfo);
            genericWidgetViewModel.getAddButton().click();


            getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, groupInfo.getName()) != null);

            UiComponent user = TableHelper.findTableRow(table, groupInfo.getName());
            GenericProductInfo groupData = getGroupData(user);
            teamInfosResult.add(groupData);
        }

        return teamInfosResult;
    }

    @TestStep(id = "edit Group", description = "edit group item")
    public GenericProductInfo editEntityWithProductAndName(GenericProductInfo oldGroupInfo, GenericProductInfo newGroupInfo) {
        GenericProductWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericProductWidgetViewModel.class);

        UiComponent table = genericWidgetViewModel.getTable();
        UiComponent tableRow = TableHelper.findTableRow(table, oldGroupInfo.getName());
        tableRow.focus();
        tableRow.click();

        populateGenericProductWidget(genericWidgetViewModel, newGroupInfo);
        UiComponent editButton = genericWidgetViewModel.getEditButton();
        getBrowserTab().waitUntilComponentIsDisplayed(editButton, WAIT_SHORT_TIMEOUT);
        editButton.focus();
        editButton.click();

        getBrowserTab().waitUntil(table, (d) -> TableHelper.findTableRow(d, newGroupInfo.getName()) != null);

        UiComponent user = TableHelper.findTableRow(table, newGroupInfo.getName());
        GenericProductInfo groupData = getGroupData(user);

        return groupData;
    }

    @TestStep(id = "delete admin Group", description = "Delete items")
    public List<GenericProductInfo> deleteEntityWithProductAndName(List<GenericProductInfo> groupInfos) {
        GenericProductWidgetViewModel genericWidgetViewModel = getBrowserTab().getView(GenericProductWidgetViewModel.class);

        UiComponent table = genericWidgetViewModel.getTable();
        for (GenericProductInfo groupInfo : groupInfos) {
            Optional<UiComponent> row = Optional.ofNullable(TableHelper.findTableRow(table, groupInfo.getName()));
            if (row.isPresent()) {
                row.get().getDescendantsBySelector("i").get(0).click();
                clickConfirmDeleteDialog();
            }
        }

        return getGroupTableData(table);
    }

    private List<User> getUserAccessTableData(UiComponent table) {
        List<User> users = new ArrayList<>();
        List<UiComponent> rows = table.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            User userData = getUserData(row);
            users.add(userData);
        }
        return users;
    }

    private List<GenericFeatureInfo> getGenericTableData(UiComponent table) {
        List<GenericFeatureInfo> teamInfos = new ArrayList<>();
        List<UiComponent> rows = table.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            TeamInfo teamData = getTeamData(row);
            teamInfos.add(teamData);
        }
        return teamInfos;
    }

    private List<GenericProductInfo> getGroupTableData(UiComponent table) {
        List<GenericProductInfo> groupInfos = new ArrayList<>();
        List<UiComponent> rows = table.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            GenericProductInfo groupData = getGroupData(row);
            groupInfos.add(groupData);
        }
        return groupInfos;
    }

    private List<ProjectInfo> getProjectTableData(UiComponent table) {
        List<ProjectInfo> projectInfos = new ArrayList<>();
        List<UiComponent> rows = table.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            ProjectInfo projectData = getProjectData(row);
            projectInfos.add(projectData);
        }
        return projectInfos;
    }

    private List<ProductInfo> getProductTableData(UiComponent table) {
        List<ProductInfo> productInfos = new ArrayList<>();
        List<UiComponent> rows = table.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            ProductInfo groupData = getProductData(row);
            productInfos.add(groupData);
        }
        return productInfos;
    }

    private List<ReviewGroupInfo> getReviewGroupTableData(UiComponent reviewGroupTable, ReviewGroupWidgetViewModel reviewGroupWidgetViewModel) {
        List<ReviewGroupInfo> reviewGroupInfos = new ArrayList<>();
        List<UiComponent> rows = reviewGroupTable.getDescendantsBySelector("elTablelib-Table-body tr");
        for (UiComponent row : rows) {
            ReviewGroupInfo reviewGroupData = getReviewGroupData(row, reviewGroupWidgetViewModel);
            reviewGroupInfos.add(reviewGroupData);
        }
        return reviewGroupInfos;
    }

    private User getUserData(UiComponent row) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        User user = new User(cells.get(0).getText(), cells.get(1).getText());
        return user;
    }

    private ReviewGroupInfo getReviewGroupData(UiComponent row, ReviewGroupWidgetViewModel reviewGroupWidgetViewModel) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        ReviewGroupInfo reviewGroup = new ReviewGroupInfo();
        reviewGroup.setName(cells.get(0).getText());

        reviewGroup.setUsers(getReviewGroupUserData(reviewGroupWidgetViewModel));

        return reviewGroup;
    }

    private Set<UserInfo> getReviewGroupUserData(ReviewGroupWidgetViewModel reviewGroupWidgetViewModel) {
        Set<UserInfo> reviewGroupUsers = new HashSet<>();
        UiComponent reviewGroupUserTable = reviewGroupWidgetViewModel.getUserTable();
        List<UiComponent> tableRows = reviewGroupUserTable.getDescendantsBySelector(TableHelper.TABLE_ROWS);
        for (UiComponent row : tableRows) {
            List<UiComponent> cells = row.getDescendantsBySelector("td");
            UserInfo user = new UserInfo();
            user.setName(cells.get(0).getText());
            reviewGroupUsers.add(user);
        }

        return reviewGroupUsers;
    }

    private ProjectInfo getProjectData(UiComponent row) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        ProjectInfo project = new ProjectInfo();
        project.setName(cells.get(0).getText());
        project.setExternalId(cells.get(1).getText());
        return project;
    }

    private ProductInfo getProductData(UiComponent row) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        ProductInfo product = new ProductInfo();
        product.setName(cells.get(0).getText());
        product.setExternalId(cells.get(1).getText());
        return product;
    }

    private TeamInfo getTeamData(UiComponent row) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        TeamInfo teamInfo = new TeamInfo();
        teamInfo.setName(cells.get(0).getText());
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName(cells.get(1).getText());
        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setName(cells.get(2).getText());
        featureInfo.setProduct(productInfo);
        teamInfo.setFeature(featureInfo);

        return teamInfo;
    }

    private GroupInfo getGroupData(UiComponent row) {
        List<UiComponent> cells = row.getDescendantsBySelector("td");
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setName(cells.get(0).getText());
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName(cells.get(1).getText());
        groupInfo.setProduct(productInfo);

        return groupInfo;
    }

    private void populateProductWidget(ProductWidgetViewModel productWidget, ProductInfo productInfo) {
        TextBox productName = productWidget.getName();
        productName.clear();
        productName.sendKeys(productInfo.getName());

        TextBox externalId = productWidget.getExternalId();
        externalId.clear();
        externalId.sendKeys(productInfo.getExternalId());
    }

    private void populateProductWidgetExternalId(ProductWidgetExternalIdViewModel productWidgetExternalIdViewModel, ProjectInfo projectInfo) {
        try {
            UiSdkSelectBox productSelect = productWidgetExternalIdViewModel.getProductSelect();
            getBrowserTab().waitUntil(productSelect, (d) -> d.getDescendantsBySelector(".ebSelect-header").get(0).isEnabled());
            setSelectBox(productSelect, getBrowserTab().getGenericView(), projectInfo.getProduct().getName());

        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("select product", e.getMessage());
        }

        TextBox projectName = productWidgetExternalIdViewModel.getName();
        projectName.clear();
        projectName.focus();
        projectName.sendKeys(projectInfo.getName());


        TextBox externalId = productWidgetExternalIdViewModel.getExternalId();
        externalId.clear();
        externalId.focus();
        externalId.sendKeys(projectInfo.getExternalId());
    }

    private void populateGenericWidget(GenericWidgetViewModel genericWidgetViewModel, GenericFeatureInfo teamInfo) {
        try {
            UiSdkSelectBox productSelect = genericWidgetViewModel.getProductSelect();
            getBrowserTab().waitUntil(productSelect, (d) -> d.getDescendantsBySelector(".ebSelect-header").get(0).isEnabled());
            setSelectBox(productSelect, getBrowserTab().getGenericView(), teamInfo.getFeature().getProduct().getName());

            sleep(1); //required to make sure feature options are available after selecting product
            UiSdkSelectBox featureSelect = genericWidgetViewModel.getFeatureSelect();
            getBrowserTab().waitUntil(featureSelect, (d) -> d.getDescendantsBySelector(".ebSelect-header").get(0).isEnabled());
            setSelectBox(featureSelect, getBrowserTab().getGenericView(), teamInfo.getFeature().getName());
        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("select product & feature", e.getMessage());
        }

        TextBox name = genericWidgetViewModel.getName();
        name.clear();
        name.focus();
        name.sendKeys(teamInfo.getName());
    }

    private void populateGenericProductWidget(GenericProductWidgetViewModel genericWidgetViewModel, GenericProductInfo teamInfo) {
        try {
            UiSdkSelectBox productSelect = genericWidgetViewModel.getProductSelect();
            getBrowserTab().waitUntil(productSelect, (d) -> d.getDescendantsBySelector(".ebSelect-header").get(0).isEnabled());
            setSelectBox(productSelect, getBrowserTab().getGenericView(), teamInfo.getProduct().getName());

        } catch (UiComponentNotFoundException e) {
            logger.info(e.getMessage(), e.getStackTrace());
            createScreenshot("genericProductWidget", e.getMessage());
        }

        TextBox name = genericWidgetViewModel.getName();
        name.clear();
        boolean focused = name.hasFocus();
        if (!focused) {
            name.focus();
        }
        name.sendKeys(teamInfo.getName());
    }

    private void clickConfirmDeleteDialog() {
        DialogViewModel dialogViewModel = getBrowserTab().getView(DialogViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(dialogViewModel.getDialog(), WAIT_TIMEOUT);
        dialogViewModel.getRemoveButton().click();
    }


}
