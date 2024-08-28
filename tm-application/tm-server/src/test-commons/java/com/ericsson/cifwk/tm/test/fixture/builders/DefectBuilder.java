package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;

public class DefectBuilder extends EntityBuilder<Defect> {

    public DefectBuilder() {
        super(new Defect());
    }

    public DefectBuilder withExternalId(String externalId) {
        entity.setExternalId(externalId);
        return this;
    }

    public DefectBuilder withExternalTitle(String externalTitle) {
        entity.setExternalTitle(externalTitle);
        return this;
    }

    public DefectBuilder withExternalSummary(String externalSummary) {
        entity.setExternalSummary(externalSummary);
        return this;
    }

    public DefectBuilder withExternalStatusName(String externalStatusName) {
        entity.setExternalStatusName(externalStatusName);
        return this;
    }

    public DefectBuilder withProject(Project project) {
        entity.setProject(project);
        return this;
    }
}
