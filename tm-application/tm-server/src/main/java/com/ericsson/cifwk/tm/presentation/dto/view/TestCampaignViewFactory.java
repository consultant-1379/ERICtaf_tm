/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;

import javax.inject.Singleton;

@Singleton
public class TestCampaignViewFactory
        extends AbstractDtoViewFactory<TestCampaignInfo>
        implements DtoViewFactory<TestCampaignInfo> {

    private static final DeclaredViews<TestCampaignInfo> DECLARED_VIEWS =
            DeclaredViews.of(TestCampaignView.class);

    @Override
    public Class<? extends DtoView<TestCampaignInfo>> getDefault() {
        return TestCampaignView.Simple.class;
    }

    @Override
    protected DeclaredViews<TestCampaignInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}
