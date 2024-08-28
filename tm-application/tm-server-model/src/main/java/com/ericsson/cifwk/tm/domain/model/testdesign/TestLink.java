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
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "TEST_LINKS")
public class TestLink extends AuditedEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_id")
    private TestCaseVersion from;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_id")
    private TestCaseVersion to;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkType type;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCaseVersion getFrom() {
        return from;
    }

    public void setFrom(TestCaseVersion from) {
        this.from = from;
    }

    public TestCaseVersion getTo() {
        return to;
    }

    public void setTo(TestCaseVersion to) {
        this.to = to;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }
}
