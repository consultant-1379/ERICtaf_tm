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

import javax.inject.Singleton;

@Singleton
public class ReportViewFactory
        extends AbstractDtoViewFactory
        implements DtoViewFactory {

    private static final DeclaredViews DECLARED_VIEWS =
            DeclaredViews.of(ReportView.class);

    @Override
    public Class getDefault() {
        return ReportView.Simple.class;
    }

    @Override
    protected DeclaredViews getAllByName() {
        return DECLARED_VIEWS;
    }

}
