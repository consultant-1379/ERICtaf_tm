package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;

public enum FeatureType {

    ENM_OTHER(getFeature(1L, "Other")),
    OSS_CN(getFeature(11L, "Deployment")),
    ASSURE_OTHER(getFeature(4L, "Other")),
    TAF(getFeature(18L, "TAF"));

    private FeatureInfo featureInfo;

    FeatureType(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public static FeatureInfo getFeature(Long id, String name) {
        return new FeatureInfo(id, name);
    }

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }
}
