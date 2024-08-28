/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

public final class TestCampaignItemView {

    private TestCampaignItemView() {
    }

    public static final class Simple implements DtoView<TestCampaignItemInfo> {
    }

    public static final class Detailed implements DtoView<TestCampaignItemInfo> {
    }

    public static final class DetailedTestCase implements DtoView<TestCampaignItemInfo> {
    }
}
