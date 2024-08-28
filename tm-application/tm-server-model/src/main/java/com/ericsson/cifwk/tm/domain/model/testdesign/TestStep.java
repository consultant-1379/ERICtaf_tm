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

import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.Ordered;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "TEST_STEPS")
public class TestStep
        extends AuditedEntity
        implements Identifiable<Long>, Ordered<Integer>, Comparable<TestStep> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_case_id")
    private TestCaseVersion testCaseVersion;

    @Column(name = "execute", nullable = false)
    private String title;

    @OneToMany(mappedBy = "testStep", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Filter(name = "deletedEntityFilter")
    private Set<VerifyStep> verifications = Sets.newLinkedHashSet();

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder = 0;

    @Column(name = "comment")
    private String comment;

    @Column(name = "data")
    private String data;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCaseVersion getTestCaseVersion() {
        return testCaseVersion;
    }

    void setTestCaseVersion(TestCaseVersion testCaseVersion) {
        this.testCaseVersion = testCaseVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    @Override
    public void setSequenceOrder(Integer order) {
        this.sequenceOrder = order;
    }

    public Set<VerifyStep> getVerifications() {
        return verifications;
    }

    public void addVerification(VerifyStep verifyStep) {
        verifications.add(verifyStep);
        verifyStep.setTestStep(this);
    }

    public void clearVerifications() {
        verifications.clear();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TestStep createCopy() {
        TestStep copy = new TestStep();
        copy.title = title;
        copy.sequenceOrder = sequenceOrder;
        copy.comment = comment;
        copy.data = data;

        for (VerifyStep verification : verifications) {
            VerifyStep verifyCopy = verification.createCopy();
            verifyCopy.setTestStep(copy);
            copy.addVerification(verifyCopy);
        }

        return copy;
    }

    @Override
    public int compareTo(TestStep testStep) {
        return sequenceOrder.compareTo(testStep.sequenceOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestStep testStep = (TestStep) o;

        if (id != null ? !id.equals(testStep.id) : testStep.id != null) {
            return false;
        }
        return title != null ? title.equals(testStep.title) : testStep.title == null;

    }
}
