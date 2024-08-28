package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 *
 */
@Entity
@Audited
@Table(name = "TEST_CASES")
public class TestCase extends AuditedEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "tc_id", nullable = false)
    private String testCaseId = UUID.randomUUID().toString();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "current_version_id", nullable = false)
    private TestCaseVersion currentVersion;

    @OneToMany(mappedBy = "testCase", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @OrderBy(value = "major_version")
    @Filter(name = "deletedEntityFilter")
    private final List<TestCaseVersion> versions = Lists.newArrayList();

    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    private User updatedByUser;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "TEST_CASES_SUBSCRIBERS",
            joinColumns = @JoinColumn(name = "test_case_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> subscribers = Sets.newHashSet();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCaseVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(TestCaseVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public List<TestCaseVersion> getVersions() {
        return versions;
    }

    public TestCaseVersion addNewMajorVersion() {
        TestCaseVersion newVersion;
        if (currentVersion == null) {
            newVersion = new TestCaseVersion();
            addNewVersion(newVersion);
        } else {
            newVersion = currentVersion.createCopy();
            Long majorVersion = currentVersion.getMajorVersion();
            newVersion.setMajorVersion(majorVersion + 1);
            newVersion.setMinorVersion(0L);
            addNewVersion(newVersion);
        }
        return newVersion;
    }

    public TestCaseVersion addNewMinorVersion() {
        TestCaseVersion newVersion;
        if (currentVersion == null) {
            newVersion = new TestCaseVersion();
            addNewVersion(newVersion);
        } else {
            newVersion = currentVersion.createCopy();
            Long majorVersion = currentVersion.getMajorVersion();
            Long minorVersion = currentVersion.getMinorVersion();
            newVersion.setMajorVersion(majorVersion);
            newVersion.setMinorVersion(minorVersion + 1);
            addNewVersion(newVersion);
        }
        return newVersion;
    }

    private void addNewVersion(TestCaseVersion testCaseVersion) {
        Preconditions.checkNotNull(testCaseVersion);

        currentVersion = testCaseVersion;
        currentVersion.setTestCase(this);
        versions.add(currentVersion);
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(User user) {
        subscribers.add(user);
    }

    public void removeSubscriber(User user) {
        subscribers.remove(user);
    }

    @Override
    public void delete() {
        super.delete();
        this.currentVersion.delete();
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestCase testCase = (TestCase) o;
        return Objects.equals(id, testCase.id) &&
                Objects.equals(testCaseId, testCase.testCaseId) &&
                Objects.equals(currentVersion, testCase.currentVersion) &&
                Objects.equals(versions, testCase.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testCaseId, currentVersion, versions);
    }
}
