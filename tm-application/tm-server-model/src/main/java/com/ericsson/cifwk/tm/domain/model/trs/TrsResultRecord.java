package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
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
@Table(name = "TRS_RESULTS")
public class TrsResultRecord implements TrsRecord<Long, String> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "trs_result_id")
    private String trsResultId;

    @ManyToOne
    @JoinColumn(name = "test_case_id", referencedColumnName = "id")
    private TestCase testCase;

    @ManyToOne
    @JoinColumn(name = "trs_session_id", referencedColumnName = "id")
    private TrsSessionRecord session;

    public TrsResultRecord() {
        // default constructor
    }

    public TrsResultRecord(String trsResultId, TestCase testCase, TrsSessionRecord session) {
        this.trsResultId = trsResultId;
        this.testCase = testCase;
        this.session = session;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTrsId() {
        return trsResultId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrsResultId() {
        return trsResultId;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public TrsSessionRecord getSession() {
        return session;
    }

    public void setSession(TrsSessionRecord session) {
        this.session = session;
    }
}
