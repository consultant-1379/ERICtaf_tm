/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.google.common.collect.Sets;
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
import java.util.Set;

@Entity
@Audited
@Table(name = "PRODUCTS")
public class Product extends AuditedEntity implements NamedWithId<Long> {

    public static final String DEFAULT_EXTERNAL_ID = "default";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Project> projects = Sets.newHashSet();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Drop> drops = Sets.newHashSet();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ProductFeature> features;

    @Column(name = "has_drops")
    private boolean dropCapable;

    @Column(name = "trs_recordable")
    private boolean trsRecordable;

    public Product() {
    }

    public Product(String externalId) {
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        project.setProduct(this);
        projects.add(project);
    }

    public Set<Drop> getDrops() {
        return drops;
    }

    public void addDrop(Drop newDrop) {
        drops.add(newDrop);
        newDrop.setProduct(this);
    }

    public void addDrops(List<Drop> dropList) {
        for (Drop newDrop : dropList) {
            addDrop(newDrop);
        }
    }

    public Set<ProductFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<ProductFeature> features) {
        this.features = features;
    }

    public boolean isDropCabable() {
        return dropCapable;
    }

    public void setDropCapable(boolean dropCapable) {
        this.dropCapable = dropCapable;
    }

    public boolean isTrsRecordable() {
        return trsRecordable;
    }

    public void setTrsRecordable(boolean trsRecordable) {
        this.trsRecordable = trsRecordable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (externalId != null ? !externalId.equals(product.externalId) : product.externalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return externalId != null ? externalId.hashCode() : 0;
    }
}
