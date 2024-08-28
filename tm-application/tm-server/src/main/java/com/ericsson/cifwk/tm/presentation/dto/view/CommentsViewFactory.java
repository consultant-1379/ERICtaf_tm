package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;

import javax.inject.Singleton;

@Singleton
public class CommentsViewFactory
        extends AbstractDtoViewFactory<CommentsThreadInfo>
        implements DtoViewFactory<CommentsThreadInfo> {

    private static final DeclaredViews<CommentsThreadInfo> DECLARED_VIEWS =
            DeclaredViews.of(CommentsView.class);

    @Override
    public Class<? extends DtoView<CommentsThreadInfo>> getDefault() {
        return CommentsView.Simple.class;
    }

    @Override
    protected DeclaredViews<CommentsThreadInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}

