package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited
@Table(name = "PRODUCT_FEATURES")
public class ProductFeature extends AuditedEntity implements NamedWithId<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TechnicalComponent> components = Sets.newHashSet();

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<TechnicalComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<TechnicalComponent> components) {
        this.components = components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductFeature feature = (ProductFeature) o;
        return Objects.equal(id, feature.id) &&
                Objects.equal(name, feature.name) &&
                Objects.equal(product, feature.product);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, product);
    }
}
