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
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "VERIFY_STEPS")
public class VerifyStep
        extends AuditedEntity
        implements Identifiable<Long>, Ordered<Integer>, Comparable<VerifyStep> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_step_id")
    private TestStep testStep;

    @Column(name = "verify_step", nullable = false)
    private String verifyStep;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder = 0;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestStep getTestStep() {
        return testStep;
    }

    void setTestStep(TestStep testStep) {
        this.testStep = testStep;
    }

    public String getVerifyStep() {
        return verifyStep;
    }

    public void setVerifyStep(String verify) {
        this.verifyStep = verify;
    }

    @Override
    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    @Override
    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    @Override
    public int compareTo(VerifyStep verifyStep) {
        return sequenceOrder.compareTo(verifyStep.sequenceOrder);
    }

    public VerifyStep createCopy() {
        VerifyStep copy = new VerifyStep();

        copy.verifyStep = verifyStep;
        copy.sequenceOrder = sequenceOrder;

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VerifyStep that = (VerifyStep) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return verifyStep != null ? verifyStep.equals(that.verifyStep) : that.verifyStep == null;

    }
}
