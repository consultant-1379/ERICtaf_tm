package com.ericsson.cifwk.tm.application.params;

import com.ericsson.cifwk.tm.domain.model.shared.search.Query;

import java.util.List;

public class TestCampaignCriteriaBuilder {

    private TestCampaignCriteria criteria = new TestCampaignCriteria();

    public TestCampaignCriteriaBuilder withQuery(Query query) {
        criteria.setQuery(query);
        return this;
    }

    public TestCampaignCriteriaBuilder withProduct(Long productId) {
        criteria.setProductId(productId);
        return this;
    }

    public TestCampaignCriteriaBuilder withDrop(Long dropId) {
        criteria.setDropId(dropId);
        return this;
    }

    public TestCampaignCriteriaBuilder withFeatures(List<Long> featureIds) {
        criteria.setFeatureIds(featureIds);
        return this;
    }

    public TestCampaignCriteriaBuilder withComponents(List<Long> componentIds) {
        criteria.setComponentIds(componentIds);
        return this;
    }

    public TestCampaignCriteria build() {
        return criteria;
    }
}
