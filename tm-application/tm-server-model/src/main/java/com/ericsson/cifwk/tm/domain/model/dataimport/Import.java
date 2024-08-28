/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.dataimport;

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.Sets;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "IMPORTS")
public class Import implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "IMPORT_TEST_CASES",
            joinColumns = @JoinColumn(name = "import_id"),
            inverseJoinColumns = @JoinColumn(name = "test_case_id"))
    private Set<TestCaseVersion> testCaseVersions = Sets.newHashSet();

    @ManyToOne(optional = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    @Column(name = "result", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportResult result;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportType type;

    @Transient
    private String message;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<TestCaseVersion> getTestCaseVersions() {
        return Collections.unmodifiableSet(testCaseVersions);
    }

    public void addTestCase(TestCaseVersion testCaseVersion) {
        testCaseVersions.add(testCaseVersion);
    }

    public void clearTestCases() {
        testCaseVersions.clear();
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public ImportResult getResult() {
        return result;
    }

    public void setResult(ImportResult result) {
        this.result = result;
    }

    public ImportType getType() {
        return type;
    }

    public void setType(ImportType importType) {
        this.type = importType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
