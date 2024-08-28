package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;

public class RequirementBuilder extends EntityBuilder<Requirement> {

    public RequirementBuilder() {
        super(new Requirement());
    }

    public RequirementBuilder withExternalId(String externalId) {
        entity.setExternalId(externalId);
        return this;
    }

    public RequirementBuilder withExternalSummary(String externalSummary) {
        entity.setExternalSummary(externalSummary);
        return this;
    }

    public RequirementBuilder withExternalType(String externalType) {
        entity.setExternalType(externalType);
        return this;
    }

    public RequirementBuilder withExternalTitle(String externalTitle) {
        entity.setExternalLabel(externalTitle);
        return this;
    }

    public RequirementBuilder withId(long id) {
        entity.setId(id);
        return this;
    }

    public RequirementBuilder withProject(Project project) {
        entity.setProject(project);
        return this;
    }

    public RequirementBuilder withExternalStatusName(String externalStatusName) {
        entity.setExternalStatusName(externalStatusName);
        return this;
    }

    public RequirementBuilder withDeliveredIn(String deliveredIn) {
        entity.setDeliveredIn(deliveredIn);
        return this;
    }
}
