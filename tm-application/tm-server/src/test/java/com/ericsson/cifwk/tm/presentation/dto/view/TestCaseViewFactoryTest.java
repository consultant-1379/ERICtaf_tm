package com.ericsson.cifwk.tm.presentation.dto.view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCaseViewFactoryTest {

    private TestCaseViewFactory testCaseViewFactory;

    @Before
    public void setUp() {
        testCaseViewFactory = new TestCaseViewFactory();
    }

    @Test
    public void testGetDefault() throws Exception {
        assertEquals(testCaseViewFactory.getDefault(), TestCaseView.Simple.class);
    }

    @Test
    public void testGetAllByName() throws Exception {
        assertEquals(TestCaseView.Simple.class, testCaseViewFactory.getByName("simple"));
        assertEquals(TestCaseView.Simple.class, testCaseViewFactory.getByName("Simple"));
        assertEquals(TestCaseView.Simple.class, testCaseViewFactory.getByName("SIMPLE"));
        assertEquals(TestCaseView.Detailed.class, testCaseViewFactory.getByName("detailed"));
        assertEquals(TestCaseView.Detailed.class, testCaseViewFactory.getByName("Detailed"));
        assertEquals(TestCaseView.Detailed.class, testCaseViewFactory.getByName("DETAILED"));
        assertEquals(TestCaseView.Simple.class, testCaseViewFactory.getByName("other"));
    }

}
