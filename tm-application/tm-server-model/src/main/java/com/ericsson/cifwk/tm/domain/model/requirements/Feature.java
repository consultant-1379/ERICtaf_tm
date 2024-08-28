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

import com.ericsson.cifwk.tm.common.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "FEATURES")
public class Feature implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId;

    @ManyToMany(mappedBy = "features")
    private Set<Requirement> requirements;

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

    public Set<Requirement> getRequirements() {
        return Collections.unmodifiableSet(requirements);
    }

    public void addRequirement(Requirement requirement) {
        this.requirements.add(requirement);
    }

    public void clearRequirements() {
        this.requirements.clear();
    }
}
