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

import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.infrastructure.ObjectMapperProvider;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Audited
@Table(name = "TEST_FIELDS")
public class TestField extends AuditedEntity implements Identifiable<Long>, Comparable<TestField> {
    public static final String CONTEXT = "CONTEXT";
    public static final String PACKAGE = "PACKAGE";
    public static final String COMPONENT = "COMPONENT";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_case_id")
    private TestCaseVersion testCaseVersion;

    @Column(name = "field", nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType field;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    public TestField() {
    }

    public TestField(String name, String value) {
        this.name = name;
        this.value = value;
        this.field = FieldType.STRING;
    }

    public TestField(String name, Integer value) {
        this.name = name;
        this.value = String.valueOf(value);
        this.field = FieldType.INTEGER;
    }

    public TestField(String name, Date value) {
        this.name = name;
        this.value = new ObjectMapperProvider().iso8601().format(value);
        this.field = FieldType.DATE;
    }

    public TestField(String name, Boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
        this.field = FieldType.BOOLEAN;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestCaseVersion getTestCaseVersion() {
        return testCaseVersion;
    }

    public void setTestCaseVersion(TestCaseVersion testCaseVersion) {
        this.testCaseVersion = testCaseVersion;
    }

    public FieldType getField() {
        return field;
    }

    public void setField(FieldType field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TestField createCopy() {
        TestField copy = new TestField();

        copy.field = field;
        copy.name = name;
        copy.value = value;

        return copy;
    }

    @Override
    public int compareTo(TestField testField) {
        int nameComparison = name.compareTo(testField.name);
        if (nameComparison == 0) {
            return value.compareTo(testField.value);
        }
        return nameComparison;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestField testField = (TestField) o;

        if (id != null ? !id.equals(testField.id) : testField.id != null) {
            return false;
        }
        return value != null ? value.equals(testField.value) : testField.value == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
