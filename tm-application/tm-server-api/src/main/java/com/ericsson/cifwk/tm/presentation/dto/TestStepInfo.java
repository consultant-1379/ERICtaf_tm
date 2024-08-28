/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestStepInfo implements Identifiable<Long> {

    private Long id;

    @NotEmptyField("testStepName")
    private String name;

    private String comment;

    private String data;
    @Valid
    private List<VerifyStepInfo> verifies = new ArrayList<>();

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

    public List<VerifyStepInfo> getVerifies() {
        return Collections.unmodifiableList(verifies);
    }

    public void addVerify(VerifyStepInfo verify) {
        this.verifies.add(verify);
    }

    public void clearVerifies() {
        this.verifies.clear();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestStepInfo that = (TestStepInfo) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = id != null ? id.hashCode() : 0;
        result1 = 31 * result1 + (name != null ? name.hashCode() : 0);
        result1 = 31 * result1 + (comment != null ? comment.hashCode() : 0);
        result1 = 31 * result1 + (data != null ? data.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "TestStepInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", data='" + data + '\'' +
                ", verifies=" + verifies +
                '}';
    }
}
