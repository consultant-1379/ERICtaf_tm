/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.users;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "USERS")
public class User implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId;

    @Column(name = "external_email")
    private String externalEmail;

    @Column(name = "external_name")
    private String externalName;

    @Column(name = "external_surname")
    private String externalSurname;

    @Column(name = "external_username")
    private String userName;

    public User() {
    }

    public User(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalEmail() {
        return externalEmail;
    }

    @VisibleForTesting
    public void setExternalEmail(String externalEmail) {
        this.externalEmail = externalEmail;
    }

    public String getExternalName() {
        return externalName;
    }

    @VisibleForTesting
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getExternalSurname() {
        return externalSurname;
    }

    @VisibleForTesting
    public void setExternalSurname(String externalSurname) {
        this.externalSurname = externalSurname;
    }

    @VisibleForTesting
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        if (userName == null) {
            userName = this.getExternalName() + " " + this.getExternalSurname();
        }
        return userName;
    }

    public boolean hasExternalAttributes() {
        return externalEmail != null &&
                externalName != null &&
                externalSurname != null;
    }

    public void setExternalAttributes(String externalEmail, String externalName, String externalSurname) {
        this.externalEmail = externalEmail;
        this.externalName = externalName;
        this.externalSurname = externalSurname;
        this.userName = externalName + " " + externalSurname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (externalId != null ? !externalId.equals(user.externalId) : user.externalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return externalId != null ? externalId.hashCode() : 0;
    }
}
