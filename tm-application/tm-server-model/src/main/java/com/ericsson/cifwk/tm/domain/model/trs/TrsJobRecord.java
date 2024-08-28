package com.ericsson.cifwk.tm.domain.model.trs;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.google.common.collect.Lists;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Audited
@Table(name = "TRS_JOBS")
public class TrsJobRecord implements TrsRecord<Long, String> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_id")
    private String jobId;

    @OneToMany(mappedBy = "trsJobRecord", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<TestCampaign> testCampaigns = Lists.newArrayList();

    @OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TrsSessionRecord> sessions;

    public TrsJobRecord() {
        // default constructor
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String getTrsId() {
        return jobId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<TestCampaign> getTestCampaigns() {
        return testCampaigns;
    }

    public void addTestCampaign(TestCampaign testCampaign) {
        testCampaign.setTrsJobRecord(this);
        this.testCampaigns.add(testCampaign);
    }

    public List<TrsSessionRecord> getSessions() {
        return sessions;
    }

    public TrsSessionRecord getSessionByIso(ISO iso) {
        return sessions.stream()
                .filter(s -> s.getIso() == iso)
                .findAny()
                .get();
    }

    public void setSessions(List<TrsSessionRecord> sessions) {
        this.sessions = sessions;
    }

}
