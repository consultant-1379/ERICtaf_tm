package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;
import com.google.common.base.Objects;


public class IsoInfo implements Identifiable<Long> {

    private Long id;

    @NotNullField("name")
    private String name;

    @NotNullField("version")
    private String version;

    public IsoInfo() {
    }

    public IsoInfo(String name, String version) {
        this(null, name, version);
    }

    public IsoInfo(Long id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IsoInfo isoInfo = (IsoInfo) o;
        return Objects.equal(name, isoInfo.name) &&
                Objects.equal(version, isoInfo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, version);
    }
}
