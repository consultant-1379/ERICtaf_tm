package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.ExcelObject;
import com.ericsson.cifwk.tm.application.services.ExcelService;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.xlsx4j.exceptions.Xlsx4jException;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTRElt;
import org.xlsx4j.sml.CTRst;
import org.xlsx4j.sml.CTXstringWhitespace;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_CASE_ID;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.FEATURE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.REQUIREMENTS_IDS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP_SPLITTER;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TITLE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.DESCRIPTION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRECONDITION;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.COMPONENT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.PRIORITY;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.GROUPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.CONTEXT;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.EXECUTION_TYPE;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.STATUS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEPS;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_STEP;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.VERIFY_STEP;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.END_OF_DATA;
import static com.ericsson.cifwk.tm.application.services.ReportingConstants.TEST_DATA;

import static com.ericsson.cifwk.tm.infrastructure.mapping.ReportMapping.getTitle;

/**
 * Created by egergle on 18/03/2016.
 */
public class ExcelServiceImpl implements ExcelService {

    private final String delimiter = ",";

    private SharedStrings sharedStrings = null;

    public static final String ILLEGAL_XML_CHAR = "[^"
            + "\u0009\r\n"
            + "\u0020-\uD7FF"
            + "\uE000-\uFFFD"
            + "\ud800\udc00-\udbff\udfff"
            + "]";

    @Override
    public void generateXLSXReport(OutputStream output, List<TestCaseInfo> testCaseInfos) throws Docx4JException,
            JAXBException {

        Joiner joiner = Joiner.on(delimiter).skipNulls();

        SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();

        WorksheetPart sheet = pkg.createWorksheetPart(new PartName("/xl/worksheets/sheet1.xml"), "Test Cases", 1);

        SheetData sheetData = sheet.getContents().getSheetData();
        Row header = Context.getsmlObjectFactory().createRow();

        createCell(header, TEST_CASE_ID);
        createCell(header, FEATURE);
        createCell(header, REQUIREMENTS_IDS);
        createCell(header, TITLE);
        createCell(header, DESCRIPTION);
        createCell(header, PRECONDITION);
        createCell(header, COMPONENT);
        createCell(header, TYPE);
        createCell(header, PRIORITY);
        createCell(header, GROUPS);
        createCell(header, CONTEXT);
        createCell(header, EXECUTION_TYPE);
        createCell(header, STATUS);
        createCell(header, TEST_STEPS);

        sheetData.getRow().add(header);

        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            Row row = Context.getsmlObjectFactory().createRow();

            createCell(row, testCaseInfo.getTestCaseId());
            createCell(row, testCaseInfo.getFeature().getName());
            createCell(row, joiner.join(testCaseInfo.getRequirementIds()));
            createCell(row, testCaseInfo.getTitle());
            createCell(row, testCaseInfo.getDescription());
            createCell(row, testCaseInfo.getPrecondition());
            createCell(row, testCaseInfo.getComponentTitle());
            createCell(row, testCaseInfo.getType());
            createCell(row, testCaseInfo.getPriority());
            createCell(row, testCaseInfo.getGroups());
            createCell(row, testCaseInfo.getContexts());
            createCell(row, testCaseInfo.getExecutionType());
            createCell(row, testCaseInfo.getTestCaseStatus());

            String testSteps = getTestSteps(testCaseInfo);
            createCell(row, testSteps);
            sheetData.getRow().add(row);
        }
        Save saver = new Save(pkg);
        saver.save(output);
    }

    @Override
    public ExcelObject getXLSXData(InputStream inputStream) throws Docx4JException, Xlsx4jException,
            IndexOutOfBoundsException {

        SpreadsheetMLPackage pkg = (SpreadsheetMLPackage) SpreadsheetMLPackage.load(inputStream);
        WorksheetPart worksheet = pkg.getWorkbookPart().getWorksheet(0);

        sharedStrings = (SharedStrings) pkg.getParts().get(new PartName("/xl/sharedStrings.xml"));

        Iterator<Row> iterator = worksheet.getContents().getSheetData().getRow().iterator();
        if (iterator.hasNext()) {
            iterator.next();//skip header row
        }
        ExcelObject excelObject = new ExcelObject();

        while (iterator.hasNext()) {
            int cellNum = 0;
            Row row = iterator.next();
            LinkedHashMap<String, String> columns = excelObject.createColumns();

            columns.put(TEST_CASE_ID, getRowValue(row, cellNum++));
            columns.put(FEATURE, getRowValue(row, cellNum++));
            columns.put(REQUIREMENTS_IDS, getRowValue(row, cellNum++));
            columns.put(TITLE, getRowValue(row, cellNum++));
            columns.put(DESCRIPTION, getRowValue(row, cellNum++));
            columns.put(PRECONDITION, getRowValue(row, cellNum++));
            columns.put(COMPONENT, getRowValue(row, cellNum++));
            columns.put(TYPE, getRowValue(row, cellNum++));
            columns.put(PRIORITY, getRowValue(row, cellNum++));
            columns.put(GROUPS, getRowValue(row, cellNum++));
            columns.put(CONTEXT, getRowValue(row, cellNum++));
            columns.put(EXECUTION_TYPE, getRowValue(row, cellNum++));
            columns.put(STATUS, getRowValue(row, cellNum++));
            columns.put(TEST_STEPS, getRowValue(row, cellNum++));

            excelObject.addRow(columns);

        }
        return excelObject;
    }

    private String getRowValue(Row row, int index) throws Docx4JException {
        Optional<String> v = Optional.fromNullable(row.getC().get(index).getV());
        Optional<CTRst> is = Optional.fromNullable(row.getC().get(index).getIs());
        if (is.isPresent()) {
            return Strings.emptyToNull(is.get().getT().getValue());
        } else if (v.isPresent()) {
            int valueIndex = Integer.parseInt(v.get());
            CTRst ctRst = sharedStrings.getContents().getSi().get(valueIndex);
            if (ctRst.getR().isEmpty()) {
                return Strings.emptyToNull(ctRst.getT().getValue());
            } else {
                String result = "";
                for (CTRElt data : ctRst.getR()) {
                    result += data.getT().getValue();
                }
                return result;
            }

        }
        return "";
    }

    private String getTestSteps(TestCaseInfo testCaseInfo) {
        String testSteps = "";
        for (TestStepInfo testStepInfo : testCaseInfo.getTestSteps()) {
            testSteps += TEST_STEP + testStepInfo.getName() + END_OF_DATA + "\n";
            String testData = testStepInfo.getData();
            if (testData != null && !testData.isEmpty()) {
                testSteps += TEST_DATA + testData + END_OF_DATA + "\n";
            }
            for (VerifyStepInfo verify : testStepInfo.getVerifies()) {
                testSteps += VERIFY_STEP + verify.getName() + END_OF_DATA + "\n";
            }
            testSteps += TEST_STEP_SPLITTER;

        }
        return testSteps;
    }

    private Cell createCell(Row row, Object content) {

        Cell cell = Context.getsmlObjectFactory().createCell();

        CTXstringWhitespace ctx = Context.getsmlObjectFactory().createCTXstringWhitespace();

        if (content == null) {
            setCtxValue(ctx, "");
        } else if (content instanceof String) {
            setCtxValue(ctx, content.toString());
        } else if (content instanceof ReferenceData) {
            ReferenceData refData = (ReferenceData) content;
            setCtxValue(ctx, joinReferenceDataItem(refData.getItems()));
        } else if (content instanceof ReferenceDataItem) {
            ReferenceDataItem refDataItem = (ReferenceDataItem) content;
            setCtxValue(ctx, getTitle(refDataItem));
        } else if (content instanceof Set) {
            Set<ReferenceDataItem> refDataItem = (Set<ReferenceDataItem>) content;
            List<ReferenceDataItem> list = Lists.newArrayList(refDataItem);
            setCtxValue(ctx, joinReferenceDataItem(list));
        }

        CTRst ctrst = new CTRst();
        ctrst.setT(ctx);

        cell.setT(STCellType.INLINE_STR);
        cell.setIs(ctrst);

        row.getC().add(cell);
        return cell;

    }

    public String joinReferenceDataItem(List<ReferenceDataItem> list) {
        String expandList = "";
        String delim = "";
        for (ReferenceDataItem value : list) {
            expandList += delim + value.getTitle();
            delim = delimiter;
        }

        return expandList;
    }

    private void setCtxValue(CTXstringWhitespace ctx, String value) {
        ctx.setValue(value.replaceAll(ILLEGAL_XML_CHAR, ""));
    }
}

