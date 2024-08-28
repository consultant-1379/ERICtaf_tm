package com.ericsson.cifwk.tm.application.params;

import com.ericsson.cifwk.tm.domain.model.shared.search.Query;

import java.util.List;

public class TestCampaignCriteria {

    protected TestCampaignCriteria() {
    }

    private Query query;

    private Long productId;

    private Long dropId;

    private List<Long> featureIds;

    private List<Long> componentIds;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getDropId() {
        return dropId;
    }

    public void setDropId(Long dropId) {
        this.dropId = dropId;
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

}
