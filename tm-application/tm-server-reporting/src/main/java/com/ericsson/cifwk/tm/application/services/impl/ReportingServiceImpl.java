package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.ReportType;
import com.ericsson.cifwk.tm.application.services.ReportingService;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.common.xml.XmlEscapers;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.nullToEmpty;
import static org.docx4j.XmlUtils.unmarshalString;

public class ReportingServiceImpl implements ReportingService {

    private final Logger logger = LoggerFactory.getLogger(ReportingServiceImpl.class);

    private final static String TITLE = "title";
    private final static String DESCRIPTION = "description";

    private final static String NEW_LINE = "\\n";

    private ObjectFactory factory;
    private String numberingTemplate;
    private String h3Template;
    private String h2PTemplate;
    private String styles;
    private ContentAccessor result;
    private int lastIndex;
    private int elementIndex;
    private int testCampaignCount;

    private WordprocessingMLPackage wordMLPackage;

    @Inject
    public void configure() {
        factory = Context.getWmlObjectFactory();
        try {
            numberingTemplate = Resources.toString(Resources.getResource("reporting/numbering.xml"), Charsets.UTF_8);
            h3Template = Resources.toString(Resources.getResource("reporting/h3PD.xml"), Charsets.UTF_8);
            h2PTemplate = Resources.toString(Resources.getResource("reporting/h2P.xml"), Charsets.UTF_8);
            styles = Resources.toString(Resources.getResource("reporting/styles.xml"), Charsets.UTF_8);

        } catch (IOException e) {
            logger.error("Unable to load template resources files. Exception: ", e);
        }
    }

    @Override
    public void generateReport(ReportType outputFormat, OutputStream output, List<ReportObject> reportObjects) {
        logMemory();
        try {

            wordMLPackage = WordprocessingMLPackage.createPackage();

            NumberingDefinitionsPart numberingDefinitionsPart = new NumberingDefinitionsPart();
            numberingDefinitionsPart.setJaxbElement((org.docx4j.wml.Numbering) unmarshalString(numberingTemplate));
            wordMLPackage.getMainDocumentPart().addTargetPart(numberingDefinitionsPart);

            StyleDefinitionsPart styleDefinitionsPart = new StyleDefinitionsPart();
            styleDefinitionsPart.setJaxbElement((org.docx4j.wml.Styles) unmarshalString(styles));
            wordMLPackage.getMainDocumentPart().addTargetPart(styleDefinitionsPart);

            elementIndex = 0;
            lastIndex = 0;

            for (ReportObject reportObject : reportObjects) {
                read(reportObject);
            }

            logMemory();
            writeReport(outputFormat, output);
        } catch (Docx4JException | JAXBException e) {
            logger.error("Exception while generating doc with query", e);
        }
    }

    @Override
    public void generateReportFromTemplate(ReportType outputFormat, OutputStream output,
                                           List<ReportObject> reportObjects,
                                           URI templateUri, String contentToAttachTo) {
        try {
            if (Strings.isNullOrEmpty(contentToAttachTo)) {
                return;
            }

            wordMLPackage = WordprocessingMLPackage.load(new File(templateUri));
            wordMLPackage.getMainDocumentPart().getDocumentSettingsPart().getJaxbElement()
                    .setUpdateFields(new BooleanDefaultTrue());

            List<Integer> indexes = findParagraphIndexes(contentToAttachTo);

            if (!indexes.isEmpty()) {
                lastIndex = indexes.get(indexes.size() - 1);
            }

            elementIndex = 1;

            for (ReportObject reportObject : reportObjects) {
                read(reportObject);
            }

        } catch (Docx4JException | JAXBException e) {
            logger.error("Exception while generating doc with query", e);
        }
        logMemory();
        writeReport(outputFormat, output);
    }

    public void writeReport(ReportType outputFormat, OutputStream output) {
        try {
            if (ReportType.DOCX.equals(outputFormat)) {
                Docx4J.save(wordMLPackage, output, 0);
            } else if (ReportType.PDF.equals(outputFormat)) {
                Docx4J.toPDF(wordMLPackage, output);
            } else if (ReportType.HTML.equals(outputFormat)) {
                Docx4J.toHTML(wordMLPackage, "", "", output);
            }
        } catch (Docx4JException e) {
            logger.error("Exception while generating doc with query", e);
        }
    }

    public void addObjectToIndexedDocumentPart(ContentAccessor item, int index) {
        wordMLPackage.getMainDocumentPart().getContent().add(index, item);
    }

    public void addToIndexedDocumentPart(int index) {
        wordMLPackage.getMainDocumentPart().getContent().add(index, this.result);
    }

    private void logMemory() {
        Runtime runtime = Runtime.getRuntime();

        logger.info("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
        logger.info("Free Memory:" + runtime.freeMemory() / 1024 / 1024);
    }

    public void addHeaders(LinkedHashMap<String, String> variables) throws JAXBException {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            HashMap<String, String> data = new HashMap<>();

            if (entry.getKey().equals(TITLE)) {
                addTitleContent(entry, data);
            } else {
                addNormalContent(entry, data);
            }
        }
    }

    private void addTitleContent(Map.Entry<String, String> entry, HashMap<String, String> data) throws JAXBException {
        data.put(TITLE, entry.getValue());
        this.result = (ContentAccessor) XmlUtils.unmarshallFromTemplate(h2PTemplate, data);
        addToIndexedDocumentPart(lastIndex + addElementIndex());
    }

    private void addNormalContent(Map.Entry<String, String> entry, HashMap<String, String> data) throws JAXBException {
        data.put(TITLE, entry.getKey());
        String value = nullToEmpty(entry.getValue());
        String[] lines = value.split(NEW_LINE);
        if (lines.length > 1) {
            data.put(DESCRIPTION, "");
            this.result = (ContentAccessor) XmlUtils.unmarshallFromTemplate(h3Template, data);
            for (String item : lines) {
                addContentTo(this.result, removeBold(rWithText(item)));
                addContentTo(this.result, rWithBr());
            }
        } else {
            data.put(DESCRIPTION, nullToEmpty(entry.getValue()));
            this.result = (ContentAccessor) XmlUtils.unmarshallFromTemplate(h3Template, data);
        }
        addToIndexedDocumentPart(lastIndex + addElementIndex());
    }

    public void addContentTo(ContentAccessor contentAccessor, R r) throws JAXBException, NullPointerException {
        contentAccessor.getContent().add(r);
    }

    public void addContent(R r) throws JAXBException, NullPointerException {
        addContentTo(this.result, r);
    }

    public String prepareString(String s) {
        return XmlEscapers.xmlContentEscaper().escape(Strings.nullToEmpty(s));
    }

    public R bold(R r) {
        RPr rPr = factory.createRPr();
        rPr.setB(new BooleanDefaultTrue());
        r.setRPr(rPr);

        return r;
    }

    public R removeBold(R r) {
        RPr rPr = factory.createRPr();
        BooleanDefaultTrue booleanDefaultTrue = new BooleanDefaultTrue();
        booleanDefaultTrue.setVal(false);
        rPr.setB(booleanDefaultTrue);
        r.setRPr(rPr);

        return r;
    }

    public R rWithBr() {
        R r = factory.createR();
        r.getContent().add(factory.createBr());

        return r;
    }

    public R rWithText(String name) {
        R r = factory.createR();
        Text text = factory.createText();
        text.setSpace("preserve");
        text.setValue(prepareString(name));
        r.getContent().add(text);

        return r;
    }

    public R rWithTab(R r) {
        r.getContent().add(factory.createRTab());
        return r;
    }

    public void read(ReportObject reportObject) throws JAXBException {
        if (reportObject.getHeaderInfo() != null) {
            addHeaders(reportObject.getHeaderInfo());
        }

        if (reportObject.hasChildren()) {
            for (ReportObject reportChild : reportObject.getChildren()) {
                addReportContent(reportChild);
                read(reportChild);
            }
        }
        if (!reportObject.getFiles().isEmpty()) {
            addFiles(reportObject.getFiles());
        }
    }

    public void addReportContent(ReportObject report) throws JAXBException {
        addContent(rWithBr());
        addContent(bold(rWithText(report.getName())));
        addContent(removeBold(rWithTab(rWithText(report.getValue()))));
        addContent(rWithBr());
    }

    private void addFiles(Map<String, MappedByteBuffer> files) {
        try {
            addContent(rWithBr());
            addContent(rWithText("Files:"));
            for (Map.Entry<String, MappedByteBuffer> file : files.entrySet()) {
                addContent(rWithBr());
                addContent(bold(rWithText(file.getKey())));
                addContent(rWithBr());

                String text = readBuffer(file.getValue());
                addContentWithNewLines(text);
                addContent(rWithBr());
            }
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }

    }

    private String readBuffer(MappedByteBuffer buffer) {
        String bufferContents = "";

        for (int i = 0; i < buffer.limit(); i++) {
            bufferContents += (char) buffer.get();
        }

        return bufferContents;
    }

    private List<Integer> findParagraphIndexes(String value) {
        List<Integer> indices = Lists.newArrayList();
        int index = 0;
        for (Object element : wordMLPackage.getMainDocumentPart().getContent()) {

            if (element instanceof ContentAccessor) {
                P p = (P) element;
                int foundIndex = getInnerParagraphs(index, value, p);
                if (foundIndex >= 0) {
                    indices.add(getInnerParagraphs(index, value, p));
                }
            }
            index++;
        }
        return indices;
    }

    private int getInnerParagraphs(int index, String value, P p) {
        for (Object innerP : p.getContent()) {
            if (innerP instanceof ContentAccessor) {
                R r = (R) innerP;
                int foundIndex = getInnerRuns(index, value, r);
                if (foundIndex >= 0) {
                    return foundIndex;
                }

            }
        }
        return -1;
    }

    private int getInnerRuns(int index, String value, R r) {
        for (Object innerR : r.getContent()) {
            if (innerR instanceof JAXBElement) {
                Object v = ((JAXBElement) innerR).getValue();
                int foundIndex = getInnerText(index, value, v);
                if (foundIndex >= 0) {
                    return foundIndex;
                }

            }
        }
        return -1;
    }

    private int getInnerText(int index, String value, Object v) {
        if (v instanceof Text) {
            Text t = (Text) v;
            return getTextIndex(index, value, t);
        }
        return -1;
    }

    private int getTextIndex(int index, String value, Text t) {
        String text = t.getValue();
        if (text != null && text.trim().equals(value)) {
            return index;
        }
        return -1;
    }

    private int addElementIndex() {
        return this.elementIndex++;
    }

    private void addContentWithNewLines(String text) throws JAXBException {
        String[] newlines = text.split(NEW_LINE);
        if (newlines.length > 1) {
            for (String item : newlines) {
                addContent(rWithBr());
                addContent(removeBold(rWithText(item)));
            }
        } else {
            addContent(removeBold(rWithText(text)));
        }
    }


    /* Method to design Sprint Validation Test Report
     * Here XWPFDocument is used to create Word Document
     * parameter : reportObjects(List of Test Campaigns and its testcases)
     * returnType : XWPFDocument*/

    public XWPFDocument docReport (List<ReportObject> reportObjects, boolean addColumn) {

        logger.info("Creating new report in DOCX format");
        XWPFDocument document = new XWPFDocument();
        createHeaderForDoc( document);
        XWPFParagraph reportHeading = document.createParagraph();
        reportHeading.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun heading = reportHeading.createRun();
        heading.setBold(true);
        heading.setFontSize(14);
        heading.setFontFamily("Arial");
        if (addColumn) {
            heading.setText("SoV Status Report");
        } else {
            heading.setText("ENM Sprint Validation Test Report");
        }
        heading.addBreak();
        heading.addBreak();

        document.createTOC();
        addCustomHeadingStyle(document, "heading 2", 2);
        XWPFParagraph paragraph = document.createParagraph();
        CTP ctP = paragraph.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff.TRUE);
        paragraph.setPageBreak(true);
        XWPFRun introduction = paragraph.createRun();
        introduction.setBold(true);
        introduction.setFontSize(14);
        introduction.setFontFamily("Arial");
        introduction.setText("Introduction:");
        introduction.addBreak();

        XWPFRun paragraphContent = paragraph.createRun();
        paragraphContent.setFontSize(11);
        paragraphContent.setFontFamily("Arial");
        if (addColumn) {
            paragraphContent.setText("This document records the results of the Statement of Verification testing.");
        } else {
            paragraphContent.setText("This document records the results of the Sprint Release Verification testing.");
        }
        testCampaignCount=0;
        for (ReportObject reportObject : reportObjects) {
            XWPFParagraph campaignName = document.createParagraph();
            XWPFRun testCampaign = campaignName.createRun();
            testCampaign.setFontSize(11);
            testCampaign.setFontFamily("Arial");
            testCampaign.setBold(true);
            testCampaign.setText(Integer.toString(testCampaignCount+1) + ". Test Campaign Name: "+reportObject.getTestCampaignList().get(0));
            campaignName.setStyle("heading 2");
            testCampaign.addBreak();

            XWPFTable testCampaignTable = document.createTable();
            XWPFTableRow testCampaignHeadings = testCampaignTable.getRow(0);
                testCampaignHeadings.getCell(0).setText(" Environment ");
                testCampaignHeadings.addNewTableCell().setText(" PS-From ");
                testCampaignHeadings.addNewTableCell().setText(" PS-To ");
                testCampaignHeadings.addNewTableCell().setText(" Guide Revision ");
                testCampaignHeadings.addNewTableCell().setText(" SED Revision ");
                testCampaignHeadings.addNewTableCell().setText(" Other Dependent SW ");
                testCampaignHeadings.addNewTableCell().setText(" Node Type and Version ");
            if (reportObject.getTestCampaignList() != null) {
                XWPFTableRow testCampaignValues = testCampaignTable.createRow();
                int count = 0;
                for(int campaignsNo=1;campaignsNo<reportObject.getTestCampaignList().size();campaignsNo++) {
                    testCampaignValues.getCell(count).setText(reportObject.getTestCampaignList().get(campaignsNo));
                    count++;
                }
            }

            XWPFParagraph breakTestCase = document.createParagraph();
            XWPFRun caseTable = breakTestCase.createRun();
            caseTable.addBreak();
            if (reportObject.hasTestCaseList()) {
                XWPFTable testCaseTable = document.createTable();
                CTTblWidth width = testCaseTable.getCTTbl().addNewTblPr().addNewTblW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(9072));
                XWPFTableRow testCaseHeadings = testCaseTable.getRow(0);
                testCaseHeadings.getCell(0).setText(" Test Case ID ");
                testCaseHeadings.addNewTableCell().setText(" Title ");
                testCaseHeadings.addNewTableCell().setText(" Result ");
                testCaseHeadings.addNewTableCell().setText(" Comment ");
                testCaseHeadings.addNewTableCell().setText(" Defect ID ");
                for(ReportObject TestCaseInfo : reportObject.getTestCaseList()) {
                    XWPFTableRow testCaseValues = testCaseTable.createRow();
                    for(int casesNo=0;casesNo<TestCaseInfo.getTestCaseInfo().size();casesNo++) {
                        testCaseValues.getCell(casesNo).setText(TestCaseInfo.getTestCaseInfo().get(casesNo));
                    }
                }
            }

            XWPFParagraph breakTable = document.createParagraph();
            XWPFRun campaign = breakTable.createRun();
            campaign.addBreak();
            testCampaignCount++;
        }
        logger.info("Sprint Validation Test Report document is created ");
        return document;
    }
    private void  createHeaderForDoc(XWPFDocument document) {

        XWPFParagraph paragraph ;
        XWPFRun run;
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);

        // create header start
        XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
        paragraph = header.createParagraph();
        XmlCursor cursor = paragraph.getCTP().newCursor();
        XWPFTable table = header.insertNewTbl(cursor);

        XWPFRun imageRun = paragraph.createRun();
        String imageFile = "Ericsson.png";
        InputStream stream = null;
        try {
            stream = this.getClass().getClassLoader().getResourceAsStream("reporting/" + imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            imageRun.addPicture(stream,
                    XWPFDocument.PICTURE_TYPE_PNG, imageFile,
                    Units.toEMU(100), Units.toEMU(20));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            XWPFTableRow row = table.getRow(0);
            if (row == null) row = table.createRow();
            int twipsPerInch = 3440;
            table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(1 * twipsPerInch));
            XWPFTableCell cell = row.getCell(0);
            if (cell == null) cell = row.createCell();
            CTTblWidth tableWidth = cell.getCTTc().addNewTcPr().addNewTcW();
             tableWidth.setW(BigInteger.valueOf(4 * twipsPerInch));
             tableWidth.setType(STTblWidth.DXA);

        cell = row.getCell(1);
            if (cell == null) cell = row.createCell();
            CTTblWidth secondTableWidth = cell.getCTTc().addNewTcPr().addNewTcW();
            secondTableWidth.setW(BigInteger.valueOf(4 * twipsPerInch));
            secondTableWidth.setType(STTblWidth.DXA);
            paragraph = cell.getParagraphs().get(0);
            run = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.RIGHT);
            run.setText("Ericsson Internal");
            run.setText("  TEST REPORT");

            cell = row.getCell(2);
            if (cell == null) cell = row.createCell();
            CTTblWidth thirdTableWidth = cell.getCTTc().addNewTcPr().addNewTcW();
              thirdTableWidth.setW(BigInteger.valueOf(4 * twipsPerInch));
              thirdTableWidth.setType(STTblWidth.DXA);
            paragraph = cell.getParagraphs().get(0);
            paragraph.setAlignment(ParagraphAlignment.RIGHT);
            run = paragraph.createRun();
            run.setText("Page ");
            paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
            run = paragraph.createRun();
            run.setText(" of ");
            paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");

        XWPFTable secondTable = header.createTable(2, 2);

        int pad = (int) (.0000000001 * 144);
        secondTable.setCellMargins(pad, pad, pad, pad);

        // Set table width to 6.5 inches in 1440ths of a point
        secondTable.setWidth((int) (4.3 * 2470));
        CTTbl ctTbl = secondTable.getCTTbl();
        CTTblPr ctTblPr = ctTbl.addNewTblPr();
        CTTblLayoutType layoutType = ctTblPr.addNewTblLayout();
        layoutType.setType(STTblLayoutType.FIXED);

        BigInteger value = new BigInteger("6120");
        CTTblGrid grid = ctTbl.addNewTblGrid();
        for (int i = 0; i < 3; i++) {
            CTTblGridCol gridCol = grid.addNewGridCol();
            gridCol.setW(value);
        }

        // Add paragraphs to the cells

        paragraph = header.createParagraph();
        paragraph.setSpacingBetween(1);
        row = secondTable.getRow(0);
        if (row == null) row = secondTable.createRow();
        twipsPerInch = 3440;
        cell = row.getCell(0);
        if (cell == null) cell = row.createCell();
        tableWidth = cell.getCTTc().addNewTcPr().addNewTcW();
        tableWidth.setW(BigInteger.valueOf(4 * twipsPerInch));
        tableWidth.setType(STTblWidth.DXA);
        row = secondTable.getRow(0);
        cell = row.getCell(0);
        XWPFParagraph xwpfParagraph = cell.getParagraphArray(0);
        XWPFRun xwpfRun = xwpfParagraph.createRun();
        xwpfRun.setFontFamily("Arial");
        xwpfRun.setFontSize(8);
        xwpfRun.setText("Prepared(Subject resp) ");
        xwpfRun.addBreak();
        XWPFRun paragraphRun = xwpfParagraph.createRun();
        paragraphRun.setFontFamily("Arial");
        paragraphRun.setFontSize(10);
        paragraphRun.setText("<Signum Id>");

        cell = row.getCell(1);
        xwpfParagraph = cell.getParagraphArray(0);
        xwpfRun = xwpfParagraph.createRun();
        xwpfRun.setFontSize(10);
        xwpfRun.setText("Date: ");
        insertCurrentDateField(xwpfParagraph);

        row = secondTable.getRow(1);
        cell = row.getCell(0);
        xwpfParagraph = cell.getParagraphArray(0);
        xwpfRun = xwpfParagraph.createRun();
        xwpfRun.setFontFamily("Arial");
        xwpfRun.setFontSize(8);
        xwpfRun.setText("Approved (Document resp)");
        xwpfRun.addBreak();
        XWPFRun xwpfParagraphRun = xwpfParagraph.createRun();
        xwpfParagraphRun.setFontFamily("Arial");
        xwpfParagraphRun.setFontSize(10);
        xwpfParagraphRun.setText("<Signum Id>");

        cell = row.getCell(1);
        xwpfParagraph = cell.getParagraphArray(0);
        xwpfRun = xwpfParagraph.createRun();
        xwpfRun.setFontSize(8);
        xwpfRun.setText("Reference");
        xwpfRun.addBreak();
        xwpfRun.setFontSize(10);

    }
    private static void insertCurrentDateField (XWPFParagraph p1){
        p1.getCTP().addNewFldSimple().setInstr("DATE \\@ \"yyyy-MM-dd\"");
    }
    private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = docxDocument.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);

    }
}
