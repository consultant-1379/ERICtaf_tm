package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.ReportingServiceImpl;
import com.google.inject.ImplementedBy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.OutputStream;
import java.net.URI;
import java.util.List;

@ImplementedBy(ReportingServiceImpl.class)
public interface ReportingService {
    void generateReport(ReportType outputFormat, OutputStream output, List<ReportObject> reportObjects);

    void generateReportFromTemplate(ReportType outputFormat, OutputStream output,
                                    List<ReportObject> reportObjects, URI templateUri, String attachToElement);

    XWPFDocument docReport(List<ReportObject> reportObjects, boolean addColumn);
}
