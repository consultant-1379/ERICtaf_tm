package com.ericsson.cifwk.tm.application.services;

import com.google.common.collect.Lists;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 12/05/2015.
 */
public class ReportObject {

    private LinkedHashMap<String, String> headerInfo;
    private List<String> testCampaignList;
    private List<String> testCaseInfo;
    private List<ReportObject> testCaseList = Lists.newArrayList();
    private List<ReportObject> children = Lists.newArrayList();
    private Map<String, MappedByteBuffer> files = new HashMap();
    private String name;
    private String value;

    public void addHeaderInfo(LinkedHashMap<String, String> value) {
        headerInfo = value;
    }

    public void addTestCampaignInfo(List<String> value) {
        testCampaignList = value;
    }

    public List<String> getTestCampaignList() {
        return testCampaignList;
    }

    public void addTestCaseInfo(List<String> value) {
        testCaseInfo = value;
    }

    public List<ReportObject> getTestCaseList() {
        return testCaseList;
    }

    public boolean hasTestCaseList() {
        return !testCaseList.isEmpty();
    }

    public void addTestCasesList(List<ReportObject> value) {
        testCaseList = value;
    }

    public List<String> getTestCaseInfo() {
        return testCaseInfo;
    }

    public void addChild(ReportObject reportObject) {
        this.children.add(reportObject);
    }

    public LinkedHashMap<String, String> getHeaderInfo() {
        return headerInfo;
    }

    public List<ReportObject> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Map<String, MappedByteBuffer> getFiles() {
        return files;
    }

    public void setFiles(Map<String, MappedByteBuffer> files) {
        this.files = files;
    }
}
