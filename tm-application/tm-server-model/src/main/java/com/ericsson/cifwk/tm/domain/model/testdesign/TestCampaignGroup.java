/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "TEST_CAMPAIGN_GROUP")
public class TestCampaignGroup extends AuditedEntity implements NamedWithId<Long>, Comparable<TestCampaignGroup> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS",
            joinColumns = @JoinColumn(name = "test_campaign_group_id"),
            inverseJoinColumns = @JoinColumn(name = "test_campaign_id"))
    @Filter(name = "deletedEntityFilter")
    private Set<TestCampaign> testCampaigns = Sets.newHashSet();

    public TestCampaignGroup() {
        //used for object deserialization
    }

    public TestCampaignGroup(String name) {
        this.name = name;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int compareTo(TestCampaignGroup testCampaignGroup) {
        return name.compareTo(testCampaignGroup.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestCampaignGroup scope = (TestCampaignGroup) o;

        return name != null ? name.equals(scope.name) : scope.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Set<TestCampaign> getTestCampaigns() {
        return testCampaigns;
    }

    public void setTestCampaigns(Set<TestCampaign> testCampaigns) {
        this.testCampaigns = testCampaigns;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
