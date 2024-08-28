package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.ExcelServiceImpl;
import com.ericsson.cifwk.tm.application.services.impl.ReportingServiceImpl;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.inject.ImplementedBy;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.xlsx4j.exceptions.Xlsx4jException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

@ImplementedBy(ExcelServiceImpl.class)
public interface ExcelService {

    void generateXLSXReport(OutputStream output, List<TestCaseInfo> testCaseInfos) throws Docx4JException,
            JAXBException;

    ExcelObject getXLSXData(InputStream inputStream) throws Docx4JException, Xlsx4jException, IndexOutOfBoundsException;

}
