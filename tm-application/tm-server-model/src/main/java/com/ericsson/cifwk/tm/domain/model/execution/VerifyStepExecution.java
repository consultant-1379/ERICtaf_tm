package com.ericsson.cifwk.tm.domain.model.execution;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Audited
@Table(name = "VERIFY_STEP_EXECUTIONS")
public class VerifyStepExecution implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "verify_step_id")
    private VerifyStep verifyStep;

    @ManyToOne
    @JoinColumn(name = "test_execution_id")
    private TestExecution testExecution;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "actual_result")
    private String actualResult;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VerifyStep getVerifyStep() {
        return verifyStep;
    }

    public void setVerifyStep(VerifyStep verifyStep) {
        this.verifyStep = verifyStep;
    }

    public TestExecution getTestExecution() {
        return testExecution;
    }

    public void setTestExecution(TestExecution testExecution) {
        this.testExecution = testExecution;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }
}
