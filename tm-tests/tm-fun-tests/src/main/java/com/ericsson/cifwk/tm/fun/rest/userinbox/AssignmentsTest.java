package com.ericsson.cifwk.tm.fun.rest.userinbox;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.AssignmentsColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class AssignmentsTest extends BaseFuncTest {

    @Test
    @TestId(id = "DURACI-3155_Func_1", title = "Get user assignment inbox")
    public void getAssignments() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.loginWithUser(getTafUser()));

        List<FilterInfo> filters = Lists.newArrayList();
        Result<Paginated<TestCampaignItemInfo>> assignmentsPageResult = tmRestOperator.getCurrentUserAssignments(filters);
        CustomAsserts.checkTestStep(assignmentsPageResult);

        assertThat(assignmentsPageResult.getValue().getItems(), hasSize(3));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-3155_Func_2", title = "Get filtered user assignment inbox")
    public void getFilteredAssignments() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.loginWithUser(getTafUser()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(AssignmentsColumn.TEST_CAMPAIGN, Condition.CONTAINS, "test plan"));
        filters.add(new FilterInfo(AssignmentsColumn.TEST_CASE_ID, Condition.CONTAINS, "b"));
        filters.add(new FilterInfo(AssignmentsColumn.TEST_CASE_TITLE, Condition.NOT_EQUAL, "Test"));

        Result<Paginated<TestCampaignItemInfo>> assignmentsPageResult = tmRestOperator.getCurrentUserAssignments(filters);
        CustomAsserts.checkTestStep(assignmentsPageResult);

        List<TestCampaignItemInfo> items = assignmentsPageResult.getValue().getItems();
        assertThat(items, hasSize(1));

        String expectedTestCaseId = "ddc198c3-d228-49e0-83b8-3278a23ba246";
        assertThat(items.get(0).getTestCase().getTestCaseId(), equalTo(expectedTestCaseId));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    private User getTafUser() {
        for (User user : host.getUsers(UserType.WEB)) {
            if ("taf".equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }
}
