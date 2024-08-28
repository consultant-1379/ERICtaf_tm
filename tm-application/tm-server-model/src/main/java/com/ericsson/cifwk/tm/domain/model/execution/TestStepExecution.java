package com.ericsson.cifwk.tm.domain.model.execution;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.ReferenceHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
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
@Table(name = "TEST_STEP_EXECUTIONS")
public class TestStepExecution implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_step_id")
    private TestStep testStep;

    @ManyToOne
    @JoinColumn(name = "test_execution_id")
    private TestExecution testExecution;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "result")
    private Integer result;

    @Column(name = "data")
    private String data;

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

    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
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

    public TestStepResult getResult() {
        return ReferenceHelper.parseEnumById(this.result, TestStepResult.class);
    }

    public void setResult(TestStepResult result) {
        if (result == null) {
            this.result = null;
        } else {
            this.result = result.getId();
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
