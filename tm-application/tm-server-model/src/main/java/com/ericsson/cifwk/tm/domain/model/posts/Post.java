package com.ericsson.cifwk.tm.domain.model.posts;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.users.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;


@Entity
@Table(name = "POSTS")
public class Post implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "message")
    private String message;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_name_ref")
    private Long objectNameReference;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public void delete() {
        this.setDeleted(true);
        this.setDeletedAt(Calendar.getInstance().getTime());
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getObjectNameReference() {
        return objectNameReference;
    }

    public void setObjectNameReference(Long objectNameReference) {
        this.objectNameReference = objectNameReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (isDeleted != post.isDeleted) return false;
        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (user != null ? !user.equals(post.user) : post.user != null) return false;
        if (message != null ? !message.equals(post.message) : post.message != null) return false;
        if (objectId != null ? !objectId.equals(post.objectId) : post.objectId != null) return false;
        return !(objectNameReference != null ?
                !objectNameReference.equals(post.objectNameReference) : post.objectNameReference != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isDeleted ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        result = 31 * result + (objectNameReference != null ? objectNameReference.hashCode() : 0);
        return result;
    }
}
