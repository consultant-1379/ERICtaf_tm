package com.ericsson.cifwk.tm.application.queries.csv;


import com.ericsson.cifwk.tm.application.common.TestDtoFactory;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestCaseCSVCreatorTest {

    private TestCaseCSVCreator csvCreator;

    @Before
    public void setUp() throws Exception {
        csvCreator = new TestCaseCSVCreator();
    }


    @Test
    public void testCreateFilteredCSV() throws Exception {
        TestCampaignInfo dto = TestDtoFactory.getTestPlan(1);
        List<String> filter = Lists.newArrayList();
        filter.add("testCaseId132");
        StreamingOutput stream = csvCreator.createCSV(dto, filter);
        String csv = outputToString(stream);

        URL expectedUrl = Resources.getResource("csv/filteredTestplan.csv");
        String expected = Resources.toString(expectedUrl, Charsets.UTF_8);

        assertThat(csv, equalTo(expected));
    }

    @Test
    public void testCreateCSV() throws Exception {
        TestCampaignInfo dto = TestDtoFactory.getTestPlan(1);
        StreamingOutput stream = csvCreator.createCSV(dto);
        String csv = outputToString(stream);

        URL expectedUrl = Resources.getResource("csv/testplan.csv");
        String expected = Resources.toString(expectedUrl, Charsets.UTF_8);

        assertThat(csv, equalTo(expected));
    }

    private String outputToString(StreamingOutput stream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        stream.write(output);
        return output.toString();
    }
}
