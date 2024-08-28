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

import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Joiner;
import com.googlecode.jcsv.writer.CSVEntryConverter;

import java.util.Arrays;

import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.*;
import static com.ericsson.cifwk.tm.infrastructure.mapping.ReportMapping.mapTitles;
import static com.google.common.base.Strings.nullToEmpty;


public class TestCaseCSVConverter implements CSVEntryConverter<TestCampaignItemInfo> {

    private static final String HEADING_SEPARATOR = ", ";
    private static final int NUMBER_OF_COLUMNS = 21;
    private static final String EMPTY_FIELD = "";

    @Override
    public String[] convertEntry(TestCampaignItemInfo testCampaignItemInfo) {
        String[] columns = new String[NUMBER_OF_COLUMNS];
        Arrays.fill(columns, EMPTY_FIELD);
        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();
        Joiner requirementsJoiner =Joiner.on(" ").skipNulls();

        TestCaseInfo testCaseInfo = testCampaignItemInfo.getTestCase();

        columns[0] = nullToEmpty(testCaseInfo.getTestCaseId());
        columns[1] = nullToEmpty(testCaseInfo.getTitle());
        columns[2] = nullToEmpty(getUser(testCampaignItemInfo.getUser()));
        columns[3] = getReferenceTitle(testCampaignItemInfo.getResult());
        columns[4] = nullToEmpty(removeNewLineChar(testCaseInfo.getComment()));
        columns[5] = joiner.join(testCampaignItemInfo.getDefectIdsForCSV());
        columns[6] = getReferenceTitle(testCaseInfo.getPriority());
        columns[7] = getReferenceTitle(testCaseInfo.getType());
        columns[8] = joiner.join(mapTitles(testCaseInfo.getGroups()));
        columns[9] = nullToEmpty(testCaseInfo.getFeature().getName());
        columns[10] = joiner.join(mapTitles(testCaseInfo.getTechnicalComponents()));
        columns[11] = getReferenceTitle(testCaseInfo.getExecutionType());
        columns[12] = joiner.join(mapTitles(testCaseInfo.getContexts()));
        columns[13] = nullToEmpty(getFormattedTime(testCampaignItemInfo.getExecutionTime()));
        columns[14] = joiner.join(testCampaignItemInfo.getRequirementIds());
        columns[15] = getReferenceTitle(testCaseInfo.getTestTeam());
        columns[16] = getReferenceTitle(testCaseInfo.getTestSuite());
        columns[17] = nullToEmpty(getUser(testCampaignItemInfo.getResultAuthor()));
        columns[18] = nullToEmpty(testCampaignItemInfo.getKpiMeasurement());
        columns[19] = requirementsJoiner.join(testCaseInfo.getRequirementIds());
        columns[20] = Boolean.toString(testCampaignItemInfo.isFileAttached());
        return columns;
    }

}
