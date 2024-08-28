package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;


public class TechnicalComponentInfo implements Identifiable<Long>, GenericFeatureInfo {

    private Long id;

    @NotNullField("name")
    private String name;

    private String featureName;

    private FeatureInfo feature;

    public TechnicalComponentInfo() {
    }

    public TechnicalComponentInfo(Long id, String name, String featureName) {
        this.id = id;
        this.name = name;
        this.featureName = featureName;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public FeatureInfo getFeature() {
        return feature;
    }

    public void setFeature(FeatureInfo feature) {
        this.feature = feature;
    }
}
