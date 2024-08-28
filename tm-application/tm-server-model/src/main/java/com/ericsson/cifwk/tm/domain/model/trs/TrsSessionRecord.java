package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.google.common.collect.Sets;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "TRS_SESSIONS")
public class TrsSessionRecord implements TrsRecord<Long, String> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "session_id")
    private String executionId;

    @OneToOne
    @JoinColumn(name = "iso_id", referencedColumnName = "id")
    private ISO iso;

    @ManyToOne
    @JoinColumn(name = "trs_job_id", referencedColumnName = "id")
    private TrsJobRecord job;

    @OneToMany(mappedBy = "session", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TrsResultRecord> results = Sets.newHashSet();

    public TrsSessionRecord() {
        // default constructor
    }

    public TrsSessionRecord(String executionId, ISO iso, TrsJobRecord job) {
        this.executionId = executionId;
        this.iso = iso;
        this.job = job;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTrsId() {
        return executionId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public ISO getIso() {
        return iso;
    }

    public void setIso(ISO iso) {
        this.iso = iso;
    }

    public TrsJobRecord getJob() {
        return job;
    }

    public void setJob(TrsJobRecord job) {
        this.job = job;
    }

    public Set<TrsResultRecord> getResults() {
        return results;
    }

    public void setResults(Set<TrsResultRecord> results) {
        this.results = results;
    }

    public void addResults(Set<TrsResultRecord> results) {
        this.results.addAll(results);
    }

    public void addResult(TrsResultRecord result) {
        result.setSession(this);
        this.results.add(result);
    }
}
