/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries.csv;

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.util.CSVUtil;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class TestCaseCSVCreator {

    public StreamingOutput createCSV(TestCampaignInfo testCampaignInfo) {

        final TestCampaignInfo info = testCampaignInfo;

        final String[] headers = {"Test Case ID", "Title", "Assigned", "Result", "Comments", "Defect ID's",
                "Priority", "Type", "Group", "Feature", "Component", "Execution Type", "Context",
                "Execution Time", "Improvement Story ID(s)", "Team", "Suite", "Execution Author", "KPI Measurement", "Requirement ID(s)","File Attached"};

        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                Writer out = new BufferedWriter(new OutputStreamWriter(os));
                CSVStrategy strategy = CSVStrategy.UK_DEFAULT;
                setupHeaders(out, headers, strategy);
                for (TestCampaignItemInfo testCampaignItemInfo : info.getTestCampaignItems()) {
                    CSVWriter<TestCampaignItemInfo> csvWriter = new CSVWriterBuilder<TestCampaignItemInfo>(out)
                            .strategy(strategy)
                            .entryConverter(new TestCaseCSVConverter()).build();
                    csvWriter.write(testCampaignItemInfo);
                    out.flush();
                }
                out.close();
            }
        };
    }

    public StreamingOutput createCSV(List<TestCampaignGroupItemInfo> testCampaignGroupItemInfos) {

        final String[] headers = {"Test Case ID", "Title", "Test Campaign", "Drop", "Assigned", "Result", "Comments",
                "Defect ID's","Priority", "Type", "Group", "Feature", "Component", "Execution Type", "Context",
                "Execution Time", "Improvement Story ID(s)", "Team", "Suite", "Execution Author", "KPI Measurement",
                "Requirement ID(s)", "Environment"};

        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                Writer out = new BufferedWriter(new OutputStreamWriter(os));
                CSVStrategy strategy = CSVStrategy.UK_DEFAULT;
                setupHeaders(out, headers, strategy);

                for (TestCampaignGroupItemInfo testCampaignGroupItemInfo : testCampaignGroupItemInfos) {
                    CSVWriter<TestCampaignGroupItemInfo> csvWriter = new CSVWriterBuilder<TestCampaignGroupItemInfo>(out)
                            .strategy(strategy)
                            .entryConverter(new TestCampaignGroupCSVConverter()).build();
                    csvWriter.write(testCampaignGroupItemInfo);
                    out.flush();
                }
                out.close();
            }
        };
    }

    public StreamingOutput createCSV(TestCampaignInfo testCampaignInfo, List<String> testCasesIds) {

        final TestCampaignInfo info = testCampaignInfo;
        final String[] headers = {"Test Case ID", "Title", "Assigned", "Result", "Comments", "Defect ID's"};
        final List filter = testCasesIds;

        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                Writer out = new BufferedWriter(new OutputStreamWriter(os));
                CSVStrategy strategy = CSVStrategy.UK_DEFAULT;
                setupHeaders(out, headers, strategy);
                for (TestCampaignItemInfo testCampaignItemInfo : info.getTestCampaignItems()) {
                    if (filter.contains(testCampaignItemInfo.getTestCase().getTestCaseId())) {
                        CSVWriter<TestCampaignItemInfo> csvWriter = new CSVWriterBuilder<TestCampaignItemInfo>(out)
                                .strategy(strategy)
                                .entryConverter(new FilteredTestCaseCSVConverter()).build();
                        csvWriter.write(testCampaignItemInfo);
                        out.flush();
                    }
                }
                out.close();
            }
        };
    }

    private void setupHeaders(Writer out, String[] headers, CSVStrategy strategy) throws IOException {
        out.write(CSVUtil.implode(headers, Character.toString(strategy.getDelimiter())));
        out.write(System.getProperty("line.separator"));
    }

}
