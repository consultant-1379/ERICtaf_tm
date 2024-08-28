/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.validation;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import org.junit.Before;
import org.junit.Test;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;

public class TestCaseValidationTest {

    private TestCaseValidation validation;
    private Product defaultProduct;
    private Project defaultProject;
    private ProductFeature defaultProductFeature;
    private Requirement defaultRequirement;
    private TechnicalComponent defaultComponent;
    private Scope defaultScope;

    @Before
    public void setUp() throws Exception {
        validation = new TestCaseValidation();
        defaultProduct = buildProduct().build();
        defaultProject = buildProject(defaultProduct).build();
        defaultProductFeature = buildProductFeature(defaultProduct).build();
        defaultRequirement = buildRequirement().withProject(defaultProject).build();
        defaultComponent = buildTechnicalComponent().withFeature(defaultProductFeature).build();
        defaultScope = buildScope().withEnabled(true).withProduct(defaultProduct).build();
    }

    @Test
    public void testMinimalTestCase() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addTechnicalComponent(defaultComponent)
                .build()
                .getCurrentVersion();
        testCaseVersion.setProductFeature(defaultProductFeature);
        validation.validate(testCaseVersion);
    }

    @Test
    public void testOrphanComponent() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addTechnicalComponent(buildTechnicalComponent().withFeature(defaultProductFeature).build())
                .build()
                .getCurrentVersion();
        testCaseVersion.setProductFeature(defaultProductFeature);
        validation.validate(testCaseVersion);
    }

    @Test
    public void testCorrectTestCase() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addTechnicalComponent(defaultComponent)
                .addScope(defaultScope)
                .addScope(buildScope().withEnabled(true).build())
                .build()
                .getCurrentVersion();
        testCaseVersion.setProductFeature(defaultProductFeature);
        validation.validate(testCaseVersion);
    }

    @Test
    public void testGlobalScopeOnly() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addTechnicalComponent(defaultComponent)
                .addScope(buildScope().withEnabled(true).build())
                .build()
                .getCurrentVersion();
        testCaseVersion.setProductFeature(defaultProductFeature);
        validation.validate(testCaseVersion);
    }

    @Test(expected = ServiceValidationException.class)
    public void testMultipleProductRequirements() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addRequirement(getRequirementWithOtherProduct())
                .build()
                .getCurrentVersion();
        validation.validateRequirements(testCaseVersion);
    }

    @Test(expected = ServiceValidationException.class)
    public void testInvalidRequirements() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(buildRequirement().build())
                .build()
                .getCurrentVersion();
        validation.validateRequirements(testCaseVersion);
    }

    @Test(expected = ServiceValidationException.class)
    public void testDifferentProductTechnicalComponent() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addTechnicalComponent(getTechnicalComponentWithOtherProduct())
                .build()
                .getCurrentVersion();
        validation.validateTechnicalComponents(testCaseVersion, defaultProduct, defaultProductFeature);
    }

    @Test(expected = ServiceValidationException.class)
    public void testDifferentProductScope() throws Exception {
        TestCaseVersion testCaseVersion = buildTestCase()
                .addRequirement(defaultRequirement)
                .addScope(getScopeWithOtherProduct())
                .build()
                .getCurrentVersion();
        validation.validateScopes(testCaseVersion, defaultProduct);
    }

    private Requirement getRequirementWithOtherProduct() {
        Product product = buildProduct().build();
        Project project = buildProject(product).build();
        return buildRequirement().withProject(project).build();
    }

    private TechnicalComponent getTechnicalComponentWithOtherProduct() {
        Product product = buildProduct().build();
        ProductFeature productFeature = buildProductFeature(product).build();
        return buildTechnicalComponent().withFeature(productFeature).build();
    }

    private Scope getScopeWithOtherProduct() {
        Product product = buildProduct().build();
        return buildScope().withProduct(product).build();
    }
}
