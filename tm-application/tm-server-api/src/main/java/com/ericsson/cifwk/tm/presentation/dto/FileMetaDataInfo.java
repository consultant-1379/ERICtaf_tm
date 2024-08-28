package com.ericsson.cifwk.tm.presentation.dto;

import java.util.Date;

/**
 * Created by egergle on 20/07/2015.
 */
public class FileMetaDataInfo {

    private Long id;

    private String fileName;

    private String url;

    private String type;

    private String author;

    private Date created;

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
