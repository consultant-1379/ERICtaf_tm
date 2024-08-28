package com.ericsson.cifwk.tm.fun.ui.tms.models.requirements.tree;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.List;

public class DetailsBlockModel extends GenericViewModel {

    public static final String TEST_CASE_TITLE_SELECTOR = ".eaTM-DetailsLine-title";

    @UiComponentMapping("#TMS_Requirements_DetailsBlock")
    private UiComponent detailsBlock;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock .eaTM-RequirementLinkWidget-requirementId")
    private UiComponent requirementId;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock .eaTM-RequirementLinkWidget-label")
    private UiComponent requirementTitle;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock-tableBlock")
    private UiComponent tableBlock;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock-detailsInfo")
    private UiComponent detailsInfoMessage;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock-progressBlock")
    private UiComponent progressBlock;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock-progressBlock")
    private UiComponent hiddenProgressBlock;

    @UiComponentMapping("#TMS_Requirements_DetailsBlock-tableBlock > .eaTM-DetailsLine")
    private List<UiComponent> testCasesLines;

    public UiComponent getDetailsBlock() {
        return detailsBlock;
    }

    public UiComponent getRequirementId() {
        return requirementId;
    }

    public UiComponent getRequirementTitle() {
        return requirementTitle;
    }

    public UiComponent getTableBlock() {
        return tableBlock;
    }

    public UiComponent getDetailsInfoMessage() {
        return detailsInfoMessage;
    }

    public UiComponent getProgressBlock() {
        return progressBlock;
    }

    public List<UiComponent> getTestCasesLines() {
        return testCasesLines;
    }
}
