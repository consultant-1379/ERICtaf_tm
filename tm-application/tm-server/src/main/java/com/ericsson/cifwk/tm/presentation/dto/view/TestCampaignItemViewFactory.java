package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

import javax.inject.Singleton;

@Singleton
public class TestCampaignItemViewFactory
        extends AbstractDtoViewFactory<TestCampaignItemInfo>
        implements DtoViewFactory<TestCampaignItemInfo> {

    private static final DeclaredViews<TestCampaignItemInfo> DECLARED_VIEWS =
            DeclaredViews.of(TestCampaignItemView.class);

    @Override
    public Class<? extends DtoView<TestCampaignItemInfo>> getDefault() {
        return TestCampaignItemView.Simple.class;
    }

    @Override
    protected DeclaredViews<TestCampaignItemInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}
