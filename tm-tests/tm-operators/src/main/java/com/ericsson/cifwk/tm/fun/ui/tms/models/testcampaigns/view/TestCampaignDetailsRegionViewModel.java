/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.view;

import com.beust.jcommander.internal.Sets;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestCampaignDetailsRegionViewModel extends GenericViewModel {

    @UiComponentMapping("#TMS_TestCampaignDetails_ContentRegion")
    private UiComponent testCampaignDetailsRegion;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-name")
    private UiComponent name;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-product")
    private UiComponent product;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-drop")
    private UiComponent drop;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-feature")
    private UiComponent feature;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-components")
    private UiComponent components;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-description")
    private UiComponent description;

    @UiComponentMapping("#TMS_TestCampaignDetails_TestCampaignDetails-environment")
    private UiComponent environment;

    @UiComponentMapping(".eaTM-TestCampaignDetailsContentRegion-testCasesTable")
    private UiComponent testCasesTable;

    public UiComponent getTestCampaignDetailsRegion() {
        return testCampaignDetailsRegion;
    }

    public String getNameText() {
        return name.getText();
    }

    public String getProductText() {
        return product.getText();
    }

    public String getDropText() {
        return drop.getText();
    }

    public String getFeatureText() {
        return feature.getText();
    }

    public Set<String> getComponentsText() {
        Set<String> componentNames = new HashSet<>();
        String componentText = components.getText();

        if (!Strings.isNullOrEmpty(componentText)) {
            String[] nameArray = components.getText().split(",");
            for (int i = 0; i < nameArray.length; i++) {
                nameArray[i] = nameArray[i].trim();
            }
            componentNames = new HashSet<>(Arrays.asList(nameArray));
        }
        return componentNames;
    }

    public ProductInfo getProduct() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName(getProductText());
        return productInfo;
    }

    public DropInfo getDrop() {
        DropInfo dropInfo = new DropInfo();
        dropInfo.setName(getDropText());
        return dropInfo;
    }

    public Set<FeatureInfo> getFeatures() {
        String[] features = getFeatureText().split(",");
        Set<FeatureInfo> featureInfos = Sets.newHashSet();

        for (String featureItem : features) {
            FeatureInfo featureInfo = new FeatureInfo();
            featureInfo.setName(featureItem);
            featureInfos.add(featureInfo);
        }
        return featureInfos;
    }

    public Set<TechnicalComponentInfo> getComponents() {
        Set<TechnicalComponentInfo> componentInfos = Sets.newHashSet();
        for (String componentName : getComponentsText()) {
            TechnicalComponentInfo technicalComponentInfo = new TechnicalComponentInfo();
            technicalComponentInfo.setName(componentName);
            componentInfos.add(technicalComponentInfo);
        }
        return componentInfos;
    }

    public String getDescriptionText() {
        return description.getText();
    }

    public String getEnvironmentText() {
        return environment.getText();
    }

    public UiComponent getTestCasesTable() {
        return testCasesTable;
    }
}
