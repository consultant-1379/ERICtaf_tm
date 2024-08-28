package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.common.Versionable;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Table(name = "DROPS")
public class Drop extends AuditedEntity implements Identifiable<Long>, Versionable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "drops", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<ISO> isos = Sets.newHashSet();

    @Column(name = "default_drop")
    private boolean defaultDrop;

    public Drop() {
    }

    public Drop(Product product, String name) {
        this.product = product;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void addIso(ISO iso) {
        isos.add(iso);
        iso.addDrop(this);
    }

    public void addIsos(List<ISO> isos) {
        for (ISO iso : isos) {
            addIso(iso);
        }
    }

    public Set<ISO> getIsos() {
        return Collections.unmodifiableSet(isos);
    }

    public void setIsos(Set<ISO> isos) {
        this.isos = isos;
    }

    public boolean isDefaultDrop() {
        return defaultDrop;
    }

    public void setDefaultDrop(boolean defaultDrop) {
        this.defaultDrop = defaultDrop;
    }

    @Override
    public DefaultArtifactVersion getArtifactVersion() {
        return new DefaultArtifactVersion(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Drop drop = (Drop) o;
        return Objects.equal(id, drop.id) &&
                Objects.equal(product, drop.product) &&
                Objects.equal(name, drop.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, product, name);
    }
}
