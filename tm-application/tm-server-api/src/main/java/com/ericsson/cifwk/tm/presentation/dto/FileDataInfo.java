package com.ericsson.cifwk.tm.presentation.dto;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

/**
 * Created by egergle on 20/07/2015.
 */
public class FileDataInfo extends FileMetaDataInfo {

    private String product;

    private String fileCategory;

    private Long entityId;

    private FormDataBodyPart formDataBodyPart;

    public FileDataInfo() {
        // no implementation needed
    }

    public FileDataInfo(String product, String fileCategory, Long entityId, String filename) {
        this.product = product;
        this.fileCategory = fileCategory;
        this.entityId = entityId;
        this.setFileName(filename);
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public FormDataBodyPart getFormDataBodyPart() {
        return formDataBodyPart;
    }

    public void setFormDataBodyPart(FormDataBodyPart formDataBodyPart) {
        this.formDataBodyPart = formDataBodyPart;
    }
}
