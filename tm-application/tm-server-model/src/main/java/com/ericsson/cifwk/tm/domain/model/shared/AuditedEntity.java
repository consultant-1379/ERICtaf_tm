/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.shared;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Audited
@MappedSuperclass
@FilterDef(name = "deletedEntityFilter", defaultCondition = "deleted = 'false'")
@Filter(name = "deletedEntityFilter")
public class AuditedEntity {

    @Column(name = "deleted")
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public void undelete() {
        this.deleted = false;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
