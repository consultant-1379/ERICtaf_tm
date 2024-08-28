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
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Joiner;
import com.googlecode.jcsv.writer.CSVEntryConverter;

import java.util.Arrays;

import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.getFormattedTime;
import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.getReferenceTitle;
import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.getUser;
import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.removeNewLineChar;
import static com.ericsson.cifwk.tm.infrastructure.mapping.ReportMapping.mapTitles;
import static com.google.common.base.Strings.nullToEmpty;


public class TestCampaignGroupCSVConverter implements CSVEntryConverter<TestCampaignGroupItemInfo> {

    private static final String HEADING_SEPARATOR = ", ";
    private static final int NUMBER_OF_COLUMNS = 23;
    private static final String EMPTY_FIELD = "";

    @Override
    public String[] convertEntry(TestCampaignGroupItemInfo testCampaignGroupItemInfo) {
        String[] columns = new String[NUMBER_OF_COLUMNS];
        Arrays.fill(columns, EMPTY_FIELD);
        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();
        Joiner requirementsJoiner =Joiner.on(" ").skipNulls();

        TestCampaignItemInfo testCampaignItemInfo = testCampaignGroupItemInfo.getTestCampaignItemInfo();
        TestCaseInfo testCaseInfo = testCampaignItemInfo.getTestCase();

        columns[0] = nullToEmpty(testCaseInfo.getTestCaseId());
        columns[1] = nullToEmpty(testCaseInfo.getTitle());
        columns[2] = nullToEmpty(testCampaignGroupItemInfo.getTestCampaignName());
        columns[3] = nullToEmpty(testCampaignGroupItemInfo.getDrop());
        columns[4] = nullToEmpty(getUser(testCampaignItemInfo.getUser()));
        columns[5] = getReferenceTitle(testCampaignItemInfo.getResult());
        columns[6] = nullToEmpty(removeNewLineChar(testCaseInfo.getComment()));
        columns[7] = joiner.join(testCampaignItemInfo.getDefectIdsForCSV());
        columns[8] = getReferenceTitle(testCaseInfo.getPriority());
        columns[9] = getReferenceTitle(testCaseInfo.getType());
        columns[10] = joiner.join(mapTitles(testCaseInfo.getGroups()));
        columns[11] = nullToEmpty(testCaseInfo.getFeature().getName());
        columns[12] = joiner.join(mapTitles(testCaseInfo.getTechnicalComponents()));
        columns[13] = getReferenceTitle(testCaseInfo.getExecutionType());
        columns[14] = joiner.join(mapTitles(testCaseInfo.getContexts()));
        columns[15] = nullToEmpty(getFormattedTime(testCampaignItemInfo.getExecutionTime()));
        columns[16] = joiner.join(testCampaignItemInfo.getRequirementIds());
        columns[17] = getReferenceTitle(testCaseInfo.getTestTeam());
        columns[18] = getReferenceTitle(testCaseInfo.getTestSuite());
        columns[19] = nullToEmpty(getUser(testCampaignItemInfo.getResultAuthor()));
        columns[20] = nullToEmpty(testCampaignItemInfo.getKpiMeasurement());
        columns[21] = requirementsJoiner.join(testCaseInfo.getRequirementIds());
        columns[22] = nullToEmpty(testCampaignGroupItemInfo.getEnvironment());
        return columns;
    }

}
