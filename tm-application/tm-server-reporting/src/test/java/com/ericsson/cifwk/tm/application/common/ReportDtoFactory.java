/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.common;

import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Lists;

import java.util.LinkedHashMap;
import java.util.List;

public class ReportDtoFactory {
    public static List<LinkedHashMap> createReportData() {
        String title = "test title";
        String description = "test description";
        String precondition = "test precondition";


        LinkedHashMap<String, String> headers = new LinkedHashMap();
        headers.put("title", title);
        headers.put("description", description);
        headers.put("precondition", precondition);

        List<LinkedHashMap> listofReports = Lists.newArrayList();
        listofReports.add(headers);

        return listofReports;
    }
}
