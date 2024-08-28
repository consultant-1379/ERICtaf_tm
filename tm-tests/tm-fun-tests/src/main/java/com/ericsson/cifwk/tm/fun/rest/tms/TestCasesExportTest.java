/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.rest.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.ExportResult;
import org.testng.annotations.Test;

import java.util.List;

public class TestCasesExportTest extends BaseFuncTest {

    @Test
    @TestId(id="DURACI-2523_Func_1", title="Execute Test Case export by search")
    public void executeTestCaseExport() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        String searchValue = "DURACI";

        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.ANY, Condition.CONTAINS, searchValue));

        CustomAsserts.checkTestStep(tmRestOperator.executeExportAction(criterions));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-2523_Func_2", title="Execute all Test Cases export")
    public void executeAllTestCaseExport() {
        createRestOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<Criterion> criterions = Lists.newArrayList();

        ExportResult exportResult = tmRestOperator.executeExportAction(criterions);
        CustomAsserts.checkTestStep(exportResult);

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
