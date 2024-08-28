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

import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import javax.inject.Singleton;

@Singleton
public class RequirementViewFactory
        extends AbstractDtoViewFactory<RequirementInfo>
        implements DtoViewFactory<RequirementInfo> {

    private static final DeclaredViews<RequirementInfo> DECLARED_VIEWS =
            DeclaredViews.of(RequirementView.class);

    @Override
    public Class<? extends DtoView<RequirementInfo>> getDefault() {
        return RequirementView.Simple.class;
    }

    @Override
    protected DeclaredViews<RequirementInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}
