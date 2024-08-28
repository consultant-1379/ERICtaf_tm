/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.SearchResult;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class AdvancedSearchTest extends BaseFuncTest {

    @Test
    @TestId(id="DURACI-2522_Func_1", title="Execute quick search")
    public void executeQuickSearch() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());

        SearchResult result = tmUiOperator.executeQuickSearch("DURACI");
        CustomAsserts.checkTestStep(result);
        assertThat(result.getTestCases().getItems(), hasSize(2));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-2522_Func_2", title="Execute advanced search with filtering")
    public void executeAdvancedSearch() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());

        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.TYPE, Condition.EQUALS, "functional"));
        criterions.add(new Criterion(Field.PRIORITY, Condition.CONTAINS, "Bl"));
        criterions.add(new Criterion(Field.COMPONENT, Condition.NOT_EQUAL, "YARR"));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.REQUIREMENT_IDS, Condition.CONTAINS, "8"));
        filters.add(new FilterInfo(TestCasesColumn.TITLE, Condition.EQUALS, "Test"));
        filters.add(new FilterInfo(TestCasesColumn.DESCRIPTION, Condition.CONTAINS, "ddd"));

        SearchResult result = tmUiOperator.executeAdvancedSearch(criterions, filters);
        CustomAsserts.checkTestStep(result);
        assertThat(result.getTestCases().getItems(), hasSize(1));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
