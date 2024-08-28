package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.inject.Singleton;

@Singleton
public class TestExecutionViewFactory
        extends AbstractDtoViewFactory<TestExecutionInfo>
        implements DtoViewFactory<TestExecutionInfo> {

    private static final DeclaredViews<TestExecutionInfo> DECLARED_VIEWS =
            DeclaredViews.of(TestExecutionView.class);

    @Override
    protected DeclaredViews<TestExecutionInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

    @Override
    public Class<? extends DtoView<TestExecutionInfo>> getDefault() {
        return TestExecutionView.Simple.class;
    }


}
