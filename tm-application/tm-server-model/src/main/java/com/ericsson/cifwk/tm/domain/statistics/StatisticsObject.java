package com.ericsson.cifwk.tm.domain.statistics;

public class StatisticsObject {

    private String label;

    private int value;

    public StatisticsObject() {
        //needed for deserialization with jackson
    }

    public StatisticsObject(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
