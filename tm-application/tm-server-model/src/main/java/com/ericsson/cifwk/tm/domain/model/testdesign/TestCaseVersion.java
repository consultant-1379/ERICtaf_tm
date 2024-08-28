/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.domain.model.shared.ReferenceHelper;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Audited
@Table(name = "TEST_CASE_VERSIONS")
public class TestCaseVersion extends AuditedEntity implements Identifiable<Long>, Comparable<TestCaseVersion> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "major_version")
    private Long majorVersion = 0L;

    @Column(name = "minor_version")
    private Long minorVersion = 1L;

    @OneToMany(mappedBy = "testCaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @Filter(name = "deletedEntityFilter")
    private Set<TestField> optionalFields = Sets.newHashSet();

    @ManyToOne
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "test_type_id",  nullable = false)
    private TestType type;

    @Column(name = "execution_type")
    private Integer executionType;

    @Column(name = "automation_candidate", nullable = false)
    private Integer automationCandidate;

    @Column(name = "description")
    private String description;

    @Column(name = "test_case_status")
    private Integer testCaseStatus;

    @Column(name = "priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "precondition")
    private String precondition;

    @Column(name = "comment")
    private String comment;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SCOPE_TEST_CASES",
            joinColumns = @JoinColumn(name = "test_case_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    @Filter(name = "deletedEntityFilter")
    private Set<Scope> scopes = Sets.newHashSet();

    @ManyToMany
    @JoinTable(name = "REQUIREMENT_TEST_CASES",
            joinColumns = @JoinColumn(name = "test_case_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "requirement_id", referencedColumnName = "id"))
    private Set<Requirement> requirements = Sets.newHashSet();

    @OneToMany(mappedBy = "testCaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Filter(name = "deletedEntityFilter")
    private Set<TestStep> testSteps = Sets.newLinkedHashSet();

    @OneToMany(mappedBy = "to")
    @Filter(name = "deletedEntityFilter")
    private Set<TestLink> incomingLinks = Sets.newHashSet();

    @OneToMany(mappedBy = "from")
    @Filter(name = "deletedEntityFilter")
    private Set<TestLink> outgoingLinks = Sets.newHashSet();

    @ManyToMany
    @JoinTable(
            name = "TEST_CASE_VERSIONS_TECHNICAL_COMPONENTS",
            joinColumns = {@JoinColumn(name = "test_case_version_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "technical_component_id", referencedColumnName = "id")})
    private Set<TechnicalComponent> technicalComponents = Sets.newHashSet();

    @ManyToOne
    @JoinColumn(name = "product_feature_id")
    private ProductFeature productFeature;

    @ManyToOne
    @JoinColumn(name = "review_group_id")
    private ReviewGroup reviewGroup;

    @Column(name = "intrusive")
    private boolean intrusive;

    @Column(name = "intrusive_comment")
    private String intrusiveComment;

    @ManyToOne
    @JoinColumn(name = "test_suite_id")
    private TestSuite testSuite;

    @ManyToOne
    @JoinColumn(name = "test_team_id")
    private TestTeam testTeam;

    @ManyToOne
    @JoinColumn(name = "review_user_id")
    private User reviewUser;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(Long majorVersion) {
        this.majorVersion = majorVersion;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public Set<TestField> getOptionalFields() {
        return Collections.unmodifiableSet(optionalFields);
    }

    public void addOptionalField(TestField optionalField) {
        optionalField.setTestCaseVersion(this);
        optionalFields.add(optionalField);
    }

    public void clearOptionalFields() {
        optionalFields.clear();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public Set<Scope> getScopes() {
        return Collections.unmodifiableSet(scopes);
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
    }

    public void addScopes(List<Scope> scopes) {
        this.scopes.addAll(scopes);
    }

    public void clearScopes() {
        scopes.clear();
    }

    public Set<Requirement> getRequirements() {
        return ImmutableSet.copyOf(requirements);
    }

    public void addRequirement(Requirement requirement) {
        if (!requirement.getTestCaseVersions().contains(this)) {
            requirements.add(requirement);
            requirement.addTestCase(this);
        }
    }

    public void addRequirements(Collection<Requirement> requirements) {
        for (Requirement requirement : requirements) {
            addRequirement(requirement);
        }
    }

    public void removeRequirement(Requirement requirement) {
        if (requirements.contains(requirement)) {
            requirements.remove(requirement);
            requirement.removeTestCase(this);
        }
    }

    public Set<TestStep> getTestSteps() {
        return testSteps;
    }

    public void clearTestSteps() {
        testSteps.clear();
    }

    public void addTestStep(TestStep testStep) {
        testSteps.add(testStep);
        testStep.setTestCaseVersion(this);
    }

    public Set<TestLink> getIncomingLinks() {
        return Collections.unmodifiableSet(incomingLinks);
    }

    public void addIncomingLink(TestLink incomingLink) {
        this.incomingLinks.add(incomingLink);
    }

    public void clearIncomingLinks() {
        this.incomingLinks.clear();
    }

    public Set<TestLink> getOutgoingLinks() {
        return Collections.unmodifiableSet(outgoingLinks);
    }

    public void addOutgoingLink(TestLink outgoingLink) {
        this.outgoingLinks.add(outgoingLink);
    }

    public void clearOutgoingLinks() {
        this.outgoingLinks.clear();
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TestCaseStatus getTestCaseStatus() {
        return ReferenceHelper.parseEnumById(testCaseStatus, TestCaseStatus.class);
    }

    public void setTestCaseStatus(TestCaseStatus testCaseStatus) {
        if (testCaseStatus == null) {
            this.testCaseStatus = null;
        } else {
            this.testCaseStatus = testCaseStatus.getId();
        }
    }

    public TestExecutionType getExecutionType() {
        return ReferenceHelper.parseEnumById(executionType, TestExecutionType.class);
    }

    public void setExecutionType(TestExecutionType executionType) {
        if (executionType == null) {
            this.executionType = null;
        } else {
            this.executionType = executionType.getId();
        }
    }

    public AutomationCandidate getAutomationCandidate() {
        return ReferenceHelper.parseEnumById(automationCandidate, AutomationCandidate.class);
    }

    public void setAutomationCandidate(AutomationCandidate automationCandidate) {
        if (automationCandidate == null) {
            this.automationCandidate = AutomationCandidate.NO.getId();
        } else {
            this.automationCandidate = automationCandidate.getId();
        }
    }

    public Map<String, TestField> getOptionalFieldsAsMap() {
        ImmutableMap.Builder<String, TestField> builder = ImmutableMap.builder();
        for (TestField testField : getOptionalFields()) {
            builder.put(testField.getName(), testField);
        }
        return builder.build();
    }

    public TestCaseVersion createCopy() {
        TestCaseVersion copy = new TestCaseVersion();
        copy.testCase = testCase;
        copy.comment = comment;
        copy.description = description;
        copy.executionType = executionType;
        copy.precondition = precondition;
        copy.priority = priority;
        copy.technicalComponents.addAll(technicalComponents);
        copy.testCaseStatus = testCaseStatus;
        copy.title = title;
        copy.type = type;
        copy.automationCandidate = automationCandidate;

        copy.incomingLinks.addAll(incomingLinks);
        copy.outgoingLinks.addAll(outgoingLinks);
        copy.requirements.addAll(requirements);
        copy.scopes.addAll(scopes);

        for (TestField optionalField : optionalFields) {
            TestField optionalFieldCopy = optionalField.createCopy();
            optionalFieldCopy.setTestCaseVersion(copy);
            copy.optionalFields.add(optionalFieldCopy);
        }

        for (TestStep testStep : testSteps) {
            TestStep testStepCopy = testStep.createCopy();
            testStepCopy.setTestCaseVersion(copy);
            copy.testSteps.add(testStepCopy);
        }
        copy.setProductFeature(productFeature);

        return copy;
    }

    public ProductFeature getProductFeature() {
        return productFeature;
    }

    public void setProductFeature(ProductFeature productFeature) {
        this.productFeature = productFeature;
    }

    public Set<TechnicalComponent> getTechnicalComponents() {
        return Collections.unmodifiableSet(technicalComponents);
    }

    public void setTechnicalComponents(Set<TechnicalComponent> components) {
        technicalComponents = components;
    }

    public void addTechnicalComponent(TechnicalComponent component) {
        technicalComponents.add(component);
    }

    public boolean isIntrusive() {
        return intrusive;
    }

    public void setIntrusive(boolean intrusive) {
        this.intrusive = intrusive;
    }

    public String getIntrusiveComment() {
        return intrusiveComment;
    }

    public void setIntrusiveComment(String intrusiveComment) {
        this.intrusiveComment = intrusiveComment;
    }

    @Override
    public int compareTo(TestCaseVersion that) {
        return this.majorVersion.compareTo(that.majorVersion);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestCaseVersion) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public TestTeam getTestTeam() {
        return testTeam;
    }

    public void setTestTeam(TestTeam testTeam) {
        this.testTeam = testTeam;
    }

    public Long getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(Long minorVersion) {
        this.minorVersion = minorVersion;
    }

    public ReviewGroup getReviewGroup() {
        return reviewGroup;
    }

    public void setReviewGroup(ReviewGroup reviewGroup) {
        this.reviewGroup = reviewGroup;
    }

    public String getVersion() {
        return majorVersion + "." + minorVersion;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(User reviewUser) {
        this.reviewUser = reviewUser;
    }
}
