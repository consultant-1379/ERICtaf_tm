package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;

public enum TechnicalComponentType {

    ENM_FM_Service(getTechnicalComponent(16L, "FM","FM Service")),
    OSS_CN(getTechnicalComponent(11L, "Deployment-DEPL_3PP-ERIC3pp","Deployment"));

    private TechnicalComponentInfo technicalComponentInfo;

    TechnicalComponentType(TechnicalComponentInfo technicalComponentInfo) {
        this.technicalComponentInfo = technicalComponentInfo;
    }

    public static TechnicalComponentInfo getTechnicalComponent(Long id, String name, String featureName) {
        return new TechnicalComponentInfo(id, name, featureName);
    }

    public TechnicalComponentInfo getTechnicalComponentInfo() {
        return technicalComponentInfo;
    }
}
