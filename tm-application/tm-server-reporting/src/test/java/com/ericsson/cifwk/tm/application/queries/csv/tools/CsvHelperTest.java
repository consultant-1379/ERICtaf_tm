package com.ericsson.cifwk.tm.application.queries.csv.tools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by egergle on 21/03/2016.
 */
public class CsvHelperTest {

    @Test
    public void testFormatTime() throws Exception {
        String result = CsvHelper.formatTime("12:45");
        assertEquals(result, "12m 45s");

        result = CsvHelper.formatTime("15:34:21");
        assertEquals(result, "15h 34m 21s");

        result = CsvHelper.formatTime("1245");
        assertNotEquals(result, "12m 45s");

    }
}