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

import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;

import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;

public class UserProfileInfo {

    @NotEmptyField("userId")
    private String userId;

    private String userName;

    private ProductInfo product;

    private ProjectInfo project;

    private boolean administrator;

    @Valid
    private Set<SavedSearchInfo> savedSearch = new HashSet<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public Set<SavedSearchInfo> getSavedSearch() {
        return savedSearch;
    }

    public void setSavedSearch(Set<SavedSearchInfo> savedSearch) {
        this.savedSearch = savedSearch;
    }
}
