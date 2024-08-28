package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TestCaseVersionTest {

    @Test
    public void testCreateCopy() throws Exception {

        //Prepare
        TestCase testCase = new TestCase();
        Scope scope = new Scope();
        //Test Case Version
        TestCaseVersion version = new TestCaseVersion();
        version.setId(123L);
        version.setTestCase(testCase);
        version.setComment("tcvComment");
        version.setDescription("tcvDescription");
        version.setExecutionType(TestExecutionType.AUTOMATED);
        version.setPrecondition("tcvPrecondition");
        version.setPriority(Priority.NORMAL);
        version.addTechnicalComponent(new TechnicalComponent());
        version.setTestCaseStatus(TestCaseStatus.REVIEW);
        version.setTitle("tcvTitle");
        TestType type = new TestType();
        type.setName("High Availability");
        version.setType(type);
        version.addScope(scope);
        //Test Step
        TestStep testStep = new TestStep();
        testStep.setId(1L);
        testStep.setComment("testComment");
        testStep.setData("testData");
        testStep.setSequenceOrder(1);
        version.addTestStep(testStep);
        testStep.setTestCaseVersion(version);
        //TestField
        TestField testField = new TestField("CONTEXT", "tfContext");
        version.addOptionalField(testField);
        testField.setTestCaseVersion(version);
        testField.setId(2L);

        //Copy
        TestCaseVersion copy = version.createCopy();

        //Verify
        assertThat(copy.getId(), nullValue());
        assertThat(copy.getTestCase(), is(testCase));
        assertThat(copy.getComment(), is("tcvComment"));
        assertThat(copy.getDescription(), is("tcvDescription"));
        assertThat(copy.getPrecondition(), is("tcvPrecondition"));
        assertThat(copy.getTitle(), is("tcvTitle"));
        assertThat(copy.getExecutionType(), is(TestExecutionType.AUTOMATED));
        assertThat(copy.getPriority(), is(Priority.NORMAL));
        assertThat(copy.getScopes(), hasItem(scope));
        assertThat(copy.getTechnicalComponents(), is(version.getTechnicalComponents()));
        assertThat(copy.getType().getName(), is("High Availability"));

        assertThat(copy.getTestSteps().size(), is(1));

        TestStep testStepCopy = copy.getTestSteps().iterator().next();
        assertThat(testStepCopy.getComment(), is("testComment"));
        assertThat(testStepCopy.getData(), is("testData"));
        assertThat(testStepCopy.getId(), nullValue());
        assertThat(testStepCopy.getSequenceOrder(), is(1));
        assertThat(testStepCopy.getTestCaseVersion(), equalTo(copy));

        assertThat(copy.getOptionalFields().size(), is(1));

        TestField testFieldCopy = copy.getOptionalFields().iterator().next();

        assertThat(testFieldCopy.getId(), nullValue());
        assertThat(testFieldCopy.getValue(), is("tfContext"));
        assertThat(testFieldCopy.getName(), is("CONTEXT"));
        assertThat(testFieldCopy.getTestCaseVersion(), equalTo(copy));
    }

}
