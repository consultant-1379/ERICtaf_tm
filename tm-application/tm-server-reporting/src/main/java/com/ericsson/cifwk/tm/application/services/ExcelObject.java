package com.ericsson.cifwk.tm.application.services;

import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by egergle on 30/03/2016.
 */
public class ExcelObject {

    private List<LinkedHashMap<String, String>> rows = Lists.newArrayList();

    public LinkedHashMap<String, String> createColumns() {
        return new LinkedHashMap();
    }

    public List<LinkedHashMap<String, String>> getRows() {
        return rows;
    }

    public void addRow(LinkedHashMap<String, String> row) {
        this.rows.add(row);
    }
}