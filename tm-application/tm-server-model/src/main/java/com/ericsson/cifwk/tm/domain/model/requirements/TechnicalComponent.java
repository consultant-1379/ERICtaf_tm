/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.NamedWithId;
import com.google.common.base.Objects;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "TECHNICAL_COMPONENTS")
public class TechnicalComponent extends AuditedEntity implements NamedWithId<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_feature_id")
    private ProductFeature feature;

    public TechnicalComponent() {
    }

    public TechnicalComponent(String name, ProductFeature feature) {
        this.name = name;
        this.feature = feature;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductFeature getFeature() {
        return feature;
    }

    public void setFeature(ProductFeature feature) {
        this.feature = feature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TechnicalComponent component = (TechnicalComponent) o;
        return Objects.equal(id, component.id) &&
                Objects.equal(name, component.name) &&
                Objects.equal(feature, component.feature);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, feature);
    }
}
