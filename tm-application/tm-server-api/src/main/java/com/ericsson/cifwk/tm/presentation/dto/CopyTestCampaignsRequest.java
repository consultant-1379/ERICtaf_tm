package com.ericsson.cifwk.tm.presentation.dto;

import com.google.common.collect.Lists;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CopyTestCampaignsRequest {

    private String query;

    @NotNull
    private Long productId;

    @NotNull
    private Long fromDropId;

    @NotNull
    private List<Long> featureIds;

    private List<Long> componentIds = Lists.newArrayList();

    @NotNull
    private Long copyToDropId;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getFromDropId() {
        return fromDropId;
    }

    public void setFromDropId(Long fromDropId) {
        this.fromDropId = fromDropId;
    }

    public List<Long> getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(List<Long> featureIds) {
        this.featureIds = featureIds;
    }

    public List<Long> getComponentIds() {
        return componentIds;
    }

    public void setComponentIds(List<Long> componentIds) {
        this.componentIds = componentIds;
    }

    public Long getCopyToDropId() {
        return copyToDropId;
    }

    public void setCopyToDropId(Long copyToDropId) {
        this.copyToDropId = copyToDropId;
    }
}
