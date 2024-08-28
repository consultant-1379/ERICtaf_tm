package com.ericsson.cifwk.tm.domain.model.management;
/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.users.User;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "TEST_PLAN_ITEMS")
public class TestCampaignItem extends AuditedEntity implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_case_id", nullable = false)
    private TestCaseVersion testCaseVersion;

    @ManyToOne
    @JoinColumn(name = "test_plan_id", nullable = false)
    private TestCampaign testCampaign;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

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

    public void setTestCaseVersion(TestCaseVersion testCaseVersion) {
        this.testCaseVersion = testCaseVersion;
    }

    public TestCampaign getTestCampaign() {
        return testCampaign;
    }

    public void setTestCampaign(TestCampaign testCampaign) {
        this.testCampaign = testCampaign;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
