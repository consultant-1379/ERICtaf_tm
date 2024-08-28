package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.common.TestDtoFactory;
import com.ericsson.cifwk.tm.application.services.ExcelObject;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.xlsx4j.exceptions.Xlsx4jException;
import org.xlsx4j.sml.Row;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ericsson.cifwk.tm.application.services.ReportingConstants.EXECUTION_TYPE;
import static com.google.common.base.Strings.nullToEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by egergle on 21/03/2016.
 */
public class ExcelServiceImplTest {

    @InjectMocks
    private ExcelServiceImpl service;

    private Joiner joiner;

    private InputStream excelReport;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        joiner = Joiner.on(",").skipNulls();
        excelReport = getClass().getClassLoader().getResourceAsStream("reporting/testReport.xlsx");
    }


    @Test
    public void testGenerateXLSXReport() throws Exception {

        TestCaseInfo testCase1 = TestDtoFactory.getTestCase(1);
        TestCaseInfo testCase2 = TestDtoFactory.getTestCase(2);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        ArrayList<TestCaseInfo> testCaseInfos = Lists.newArrayList(testCase1, testCase2);
        service.generateXLSXReport(output, testCaseInfos);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output.toByteArray());

        SpreadsheetMLPackage pkg = (SpreadsheetMLPackage) SpreadsheetMLPackage.load(byteArrayInputStream);
        WorksheetPart worksheet = pkg.getWorkbookPart().getWorksheet(0);

        Iterator<Row> iterator = worksheet.getContents().getSheetData().getRow().iterator();
        iterator.next();//skip header row
        int index = 0;
        while (iterator.hasNext()) {
            Row row = iterator.next();
            assertEquals(row.getC().get(0).getIs().getT().getValue(), testCaseInfos.get(index).getTestCaseId());
            assertEquals(row.getC().get(1).getIs().getT().getValue(),
                    testCaseInfos.get(index).getFeature().getName());
            assertEquals(row.getC().get(2).getIs().getT().getValue(),
                    joiner.join(testCaseInfos.get(index).getRequirementIds()));
            assertEquals(row.getC().get(3).getIs().getT().getValue(), testCaseInfos.get(index).getTitle());
            assertEquals(row.getC().get(4).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getDescription()));
            assertEquals(row.getC().get(5).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getPrecondition()));
            assertEquals(row.getC().get(6).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getComponentTitle()));
            assertEquals(row.getC().get(7).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getType().getTitle()));
            assertEquals(row.getC().get(8).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getPriority().getTitle()));
            assertEquals(row.getC().get(9).getIs().getT().getValue(),
                    nullToEmpty(join(testCaseInfos.get(index).getGroups())));
            assertEquals(row.getC().get(10).getIs().getT().getValue(),
                    nullToEmpty(join(testCaseInfos.get(index).getContexts())));
            assertEquals(row.getC().get(11).getIs().getT().getValue(),
                    nullToEmpty(testCaseInfos.get(index).getExecutionType().getTitle()));

            index++;

        }
    }

    @Test
    public void testJoinReferenceDataItem() throws Exception {
        String expectedResult = "REST,CLI";
        ReferenceDataItem item1 = new ReferenceDataItem();
        item1.setTitle("REST");
        ReferenceDataItem item2 = new ReferenceDataItem();
        item2.setTitle("CLI");
        List<ReferenceDataItem> list = Lists.newArrayList(item1, item2);

        String result = service.joinReferenceDataItem(list);
        assertEquals(result, expectedResult);

    }

    @Test
    public void testGetXLSXData() throws URISyntaxException, Docx4JException, Xlsx4jException {
        ExcelObject xlsxData = service.getXLSXData(excelReport);
        List<LinkedHashMap<String, String>> rows = xlsxData.getRows();

        Iterator<LinkedHashMap<String, String>> iterator = rows.iterator();
        while (iterator.hasNext()) {
            LinkedHashMap<String, String> next = iterator.next();
            for (Map.Entry<String, String> item : next.entrySet()) {
                assertNotNull(item.getValue());
            }
        }
    }

    private String join (Set<ReferenceDataItem> refDataItems) {
        List<ReferenceDataItem> list = Lists.newArrayList(refDataItems);
        return service.joinReferenceDataItem(list);
    }
}