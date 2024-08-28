package com.ericsson.cifwk.tm.domain.model.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FEATURE_TOGGLES")
public class FeatureToggle {

    @Id
    @Column(name = "feature_name")
    private String featureName;

    @Column(name = "feature_enabled")
    private Boolean featureEnabled;

    @Column(name = "strategy_id")
    private String strategyId;

    @Column(name = "strategy_params")
    private String strategyParams;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Boolean getFeatureEnabled() {
        return featureEnabled;
    }

    public void setFeatureEnabled(Boolean featureEnabled) {
        this.featureEnabled = featureEnabled;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyParams() {
        return strategyParams;
    }

    public void setStrategyParams(String strategyParams) {
        this.strategyParams = strategyParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureToggle that = (FeatureToggle) o;

        if (featureEnabled != null ? !featureEnabled.equals(that.featureEnabled) : that.featureEnabled != null)
            return false;
        if (featureName != null ? !featureName.equals(that.featureName) : that.featureName != null) return false;
        if (strategyId != null ? !strategyId.equals(that.strategyId) : that.strategyId != null) return false;
        if (strategyParams != null ? !strategyParams.equals(that.strategyParams) : that.strategyParams != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = featureName != null ? featureName.hashCode() : 0;
        result = 31 * result + (featureEnabled != null ? featureEnabled.hashCode() : 0);
        result = 31 * result + (strategyId != null ? strategyId.hashCode() : 0);
        result = 31 * result + (strategyParams != null ? strategyParams.hashCode() : 0);
        return result;
    }

}
