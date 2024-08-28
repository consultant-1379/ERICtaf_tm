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

import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import javax.inject.Singleton;

@Singleton
public class DefectViewFactory
        extends AbstractDtoViewFactory<DefectInfo>
        implements DtoViewFactory<DefectInfo> {

    private static final DeclaredViews<DefectInfo> DECLARED_VIEWS =
            DeclaredViews.of(DefectView.class);

    @Override
    public Class<? extends DtoView<DefectInfo>> getDefault() {
        return DefectView.Simple.class;
    }

    @Override
    protected DeclaredViews<DefectInfo> getAllByName() {
        return DECLARED_VIEWS;
    }

}
