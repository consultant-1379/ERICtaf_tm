package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;

import java.util.Date;

public class PostInfo implements Identifiable<Long> {

    private Long id;
    private UserInfo user;
    private boolean isDeleted;
    private Date createdAt;
    private Date deletedAt;
    private String message;
    private Long objectId;
    private ReferenceDataItem objectNameReference;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void setObjectNameReference(ReferenceDataItem objectNameRef) {
        this.objectNameReference = objectNameRef;
    }

    public ReferenceDataItem getObjectNameReference() {
        return objectNameReference;
    }

}
