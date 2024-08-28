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

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import javax.inject.Singleton;

@Singleton
public class TestCaseViewFactory
        extends AbstractDtoViewFactory<TestCaseInfo>
        implements DtoViewFactory<TestCaseInfo> {

    private static final DeclaredViews<TestCaseInfo> DECLARED_VIEWS =
            DeclaredViews.of(TestCaseView.class);

    @Override
    public Class<? extends DtoView<TestCaseInfo>> getDefault() {
        return TestCaseView.Simple.class;
    }

    @Override
    protected DeclaredViews<TestCaseInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}
