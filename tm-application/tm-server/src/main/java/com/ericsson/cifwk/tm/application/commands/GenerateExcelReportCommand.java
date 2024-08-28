package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.services.ExcelService;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.Math.ceil;


/**
 * @author egergle
 */
public class GenerateExcelReportCommand implements Command<String> {

    public static final double PER_PAGE = 20.0;

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestCaseVersionMapper testCaseVersionMapper;

    @Inject
    private ExcelService excelService;

    private final Logger logger = LoggerFactory.getLogger(GenerateExcelReportCommand.class);

    @Override
    public Response apply(String queryString) {

        final String query = queryString;
        StreamingOutput streamingOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException {
                try {
                    List<TestCaseInfo> testCases = getTestCases(query);
                    excelService.generateXLSXReport(output, testCases);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        };


        return Response.ok(streamingOutput)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = report.xlsx")
                .build();
    }

    private List<TestCaseInfo> getTestCases(String queryString) {
        Query query = Query.fromQueryString(queryString);
        Paginated<TestCase> paginated = testCaseService.customSearch(query, 1, (int) PER_PAGE);

        int pages = (int) ceil(paginated.getTotal() / PER_PAGE);
        List<TestCaseInfo> testCases = Lists.newArrayList();
        for (int i = 1; i <= pages; i++) {
            for (TestCase testCase : paginated.getResults()) {
                TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

                TestCaseInfo testCaseInfo = testCaseVersionMapper.mapEntity(testCaseVersion,
                        TestCaseInfo.class,
                        TestCaseView.Detailed.class);

                testCases.add(testCaseInfo);
            }
            if (i < pages) {
                paginated = testCaseService.customSearch(query, i + 1, (int) PER_PAGE);
            }
        }

        return testCases;
    }


}
