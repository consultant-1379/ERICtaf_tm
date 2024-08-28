/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;

public class TeamInfo implements Identifiable<Long>, GenericFeatureInfo {

    private Long id;

    @NotNullField("name")
    private String name;

    @NotNullField("feature")
    private FeatureInfo feature;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public FeatureInfo getFeature() {
        return feature;
    }

    @Override
    public void setFeature(FeatureInfo feature) {
        this.feature = feature;
    }
}
