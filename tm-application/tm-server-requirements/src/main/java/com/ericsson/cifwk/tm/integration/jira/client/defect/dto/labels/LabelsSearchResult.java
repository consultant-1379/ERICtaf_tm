package com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suggestionListStruct")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabelsSearchResult {

    @XmlElement(name = "token")
    private String search;

    @XmlElement(name = "suggestions")
    private List<Label> labels;

    public String getSearch() {
        return search;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}
