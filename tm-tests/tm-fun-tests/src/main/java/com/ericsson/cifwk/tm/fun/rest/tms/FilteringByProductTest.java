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

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.common.DynamicUser;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FilteringByProductTest extends BaseFuncTest {

    @Test
    @TestId(id="DURACI-2257_Func_1", title="Change a root product")
    public void changeSelectedProduct() {
        createRestOperators();
        User user = hostHelper.getUserByName(UserType.WEB, "taf2");
        CustomAsserts.checkTestStep(loginOperator.loginWithUser(user));

        ProductInfo productInfo = ProductType.ENM.getProductInfo();
        Result<ProductInfo> result = tmRestOperator.selectUserProfileProduct(productInfo);

        CustomAsserts.checkTestStep(result);
        assertThat(result.getValue(), notNullValue());
        assertThat(result.getValue().getExternalId(), equalTo(productInfo.getExternalId()));
        assertThat(result.getValue().getName(), equalTo(productInfo.getName()));

        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ASSURE.getProductInfo()));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="DURACI-2257_Func_2", title="Check that selected product is persisted")
    public void productIsPersisted() {
        createRestOperators();

        User user = DynamicUser.create();
        CustomAsserts.checkTestStep(loginOperator.loginWithUser(user));

        ProductInfo productInfo = ProductType.ENM.getProductInfo();
        Result<ProductInfo> result = tmRestOperator.selectUserProfileProduct(productInfo);

        CustomAsserts.checkTestStep(result);
        CustomAsserts.checkTestStep(loginOperator.logout());
        CustomAsserts.checkTestStep(loginOperator.loginWithUser(user));

        Result<ProductInfo> productResult = tmRestOperator.getSelectedProject();

        CustomAsserts.checkTestStep(productResult);
        assertThat(productResult.getValue(), notNullValue());
        assertThat(productResult.getValue().getExternalId(), equalTo(productInfo.getExternalId()));
        assertThat(productResult.getValue().getName(), equalTo(productInfo.getName()));

        // move back to other product and logout
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ASSURE.getProductInfo()));
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
