package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.common.Versionable;
import com.google.common.collect.Sets;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Audited
@Table(name = "ISOS")
public class ISO implements Identifiable<Long>, Versionable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private String version;

    @ManyToMany
    @JoinTable(
            name = "ISOS_DROPS",
            joinColumns = {@JoinColumn(name = "iso_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "drop_id", referencedColumnName = "id")})
    private Set<Drop> drops = Sets.newHashSet();

    public ISO() {
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<Drop> getDrops() {
        return drops;
    }

    public void setDrops(Set<Drop> drops) {
        this.drops = drops;
    }

    public void addDrop(Drop d) {
        drops.add(d);
    }

    @Override
    public DefaultArtifactVersion getArtifactVersion() {
        return new DefaultArtifactVersion(version);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ISO iso = (ISO) o;
        return Objects.equals(id, iso.id) &&
                Objects.equals(name, iso.name) &&
                Objects.equals(version, iso.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, version);
    }
}
