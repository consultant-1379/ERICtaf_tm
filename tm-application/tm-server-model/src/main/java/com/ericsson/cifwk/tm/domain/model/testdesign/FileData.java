package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.files.FileCategory;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 */
@Entity
@Audited
@Table(name = "FILE_STORAGE")
public class FileData extends AuditedEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "tc_id", nullable = false)
    private Long entityId;

    @Column(name = "filename")
    private String filename;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;

    @Column(name = "location")
    private String location;

    @Column(name = "author")
    private String author;

    @Column(name = "created")
    private Date created;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileCategory getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(FileCategory fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
