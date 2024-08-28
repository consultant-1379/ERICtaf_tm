package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;

public class TestCaseBuilder extends EntityBuilder<TestCase> {

    public TestCaseBuilder() {
        super(TestCaseFactory.createTestCase());
    }

    public TestCaseBuilder withTestCaseId(String id) {
        entity.setTestCaseId(id);
        return this;
    }

    public TestCaseBuilder withTitle(String title) {
        entity.getCurrentVersion().setTitle(title);
        return this;
    }

    public TestCaseBuilder withProductFeature(ProductFeature feature) {
        entity.getCurrentVersion().setProductFeature(feature);
        return this;
    }

    public TestCaseBuilder withType(TestType testType) {
        entity.getCurrentVersion().setType(testType);
        return this;
    }

    public TestCaseBuilder withDescription(String description) {
        entity.getCurrentVersion().setDescription(description);
        return this;
    }

    public TestCaseBuilder withPriority(Priority priority) {
        entity.getCurrentVersion().setPriority(priority);
        return this;
    }

    public TestCaseBuilder addRequirement(Requirement requirement) {
        entity.getCurrentVersion().addRequirement(requirement);
        return this;
    }

    public TestCaseBuilder withPrecondition(String precondition) {
        entity.getCurrentVersion().setPrecondition(precondition);
        return this;
    }

    public TestCaseBuilder withComment(String comment) {
        entity.getCurrentVersion().setComment(comment);
        return this;
    }

    public TestCaseBuilder addTestStep(TestStep testStep) {
        entity.getCurrentVersion().addTestStep(testStep);
        return this;
    }

    public TestCaseBuilder withId(Long id) {
        entity.setId(id);
        entity.getCurrentVersion().setId(id);
        return this;
    }

    public TestCaseBuilder addTechnicalComponent(TechnicalComponent technicalComponent) {
        entity.getCurrentVersion().addTechnicalComponent(technicalComponent);
        return this;
    }

    public TestCaseBuilder withExecutionType(TestExecutionType executionType) {
        entity.getCurrentVersion().setExecutionType(executionType);
        return this;
    }

    public TestCaseBuilder withTestCaseStatus(TestCaseStatus testCaseStatus) {
        entity.getCurrentVersion().setTestCaseStatus(testCaseStatus);
        return this;
    }

    public TestCaseBuilder withAutomationCandidate(AutomationCandidate automationCandidate) {
        entity.getCurrentVersion().setAutomationCandidate(automationCandidate);
        return this;
    }

    public TestCaseBuilder addScope(Scope scope) {
        entity.getCurrentVersion().addScope(scope);
        return this;
    }

    public TestCaseBuilder withOptionalField(TestField optionalField) {
        entity.getCurrentVersion().addOptionalField(optionalField);
        return this;
    }

    public TestCaseBuilder withTestSuite(TestSuite testSuite) {
        entity.getCurrentVersion().setTestSuite(testSuite);
        return this;
    }

    public TestCaseBuilder withTestTeam(TestTeam testTeam) {
        entity.getCurrentVersion().setTestTeam(testTeam);
        return this;
    }
}
