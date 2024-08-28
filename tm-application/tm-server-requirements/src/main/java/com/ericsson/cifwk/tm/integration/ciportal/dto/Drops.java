package com.ericsson.cifwk.tm.integration.ciportal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Drops {

    @JsonProperty("Drops")
    private List<String> rawDrops;

    public Drops() {
    }

    /**
     * Drops are returned as "Product:CiDrop" and require additional formatting
     *
     * @return list of drops
     */
    public List<String> getRawDrops() {
        return rawDrops;
    }

    public void setRawDrops(List<String> rawDrops) {
        this.rawDrops = rawDrops;
    }

}
