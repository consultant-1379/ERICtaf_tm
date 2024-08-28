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

import com.ericsson.cifwk.tm.presentation.validation.ExistingUser;
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;

import javax.validation.Valid;
import java.util.Date;

public class NotificationInfo {

    private Long id;

    @Valid
    @NotNullField("type")
    private ReferenceDataItem type;

    @NotEmptyField("text")
    private String text;

    @NotNullField("startDate")
    private Date startDate;

    @NotNullField("endDate")
    private Date endDate;

    @ExistingUser
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReferenceDataItem getType() {
        return type;
    }

    public void setType(ReferenceDataItem type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
