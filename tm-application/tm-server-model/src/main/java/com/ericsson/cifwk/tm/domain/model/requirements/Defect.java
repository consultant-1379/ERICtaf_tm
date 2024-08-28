/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "DEFECTS")
public class Defect extends AuditedEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId;

    @Column(name = "external_title")
    private String externalTitle;

    @Column(name = "external_summary")
    private String externalSummary;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "external_statusname")
    private String externalStatusName;

    @Column(name = "external_deliveredIn")
    private String deliveredIn;

    public Defect() {
    }

    public Defect(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalTitle() {
        return externalTitle;
    }

    public void setExternalTitle(String externalTitle) {
        this.externalTitle = externalTitle;
    }

    public String getExternalSummary() {
        return externalSummary;
    }

    public void setExternalSummary(String externalSummary) {
        this.externalSummary = externalSummary;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getExternalStatusName() {
        return externalStatusName;
    }

    public void setExternalStatusName(String externalStatusName) {
        this.externalStatusName = externalStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Defect defect = (Defect) o;

        if (externalId != null ? !externalId.equals(defect.externalId) : defect.externalId != null) return false;
        if (id != null ? !id.equals(defect.id) : defect.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Defect{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", externalTitle='" + externalTitle + '\'' +
                ", externalSummary='" + externalSummary + '\'' +
                ", project=" + project +
                ", statusName=" + externalStatusName +
                '}';
    }

    public String getDeliveredIn() {
        return deliveredIn;
    }

    public void setDeliveredIn(String deliveredIn) {
        this.deliveredIn = deliveredIn;
    }
}
