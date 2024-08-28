/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testexecutions.view;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class CreateDefectViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestExecution_CreateDefect")
    private UiComponent createDefectContent;

    @UiComponentMapping("#TMS_TestExecution_CreateDefect-fixVersion")
    private UiComponent createDefectFixVersion;


    public UiComponent getCreateDefectContent() {
        return createDefectContent;
    }
}
