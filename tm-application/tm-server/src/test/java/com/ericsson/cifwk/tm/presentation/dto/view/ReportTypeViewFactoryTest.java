package com.ericsson.cifwk.tm.presentation.dto.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by egergle on 15/03/2016.
 */
public class ReportTypeViewFactoryTest {


    @Test
    public void testGetByName() throws Exception {
        ReportTypeViewFactory reportTypeViewFactory = new ReportTypeViewFactory();
        Class view = reportTypeViewFactory.getByName("TestCase");
        assertEquals(view, ReportTypeView.TestCase.class);
    }
}