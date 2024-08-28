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

import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.getReferenceTitle;
import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.getUser;
import static com.ericsson.cifwk.tm.application.queries.csv.tools.CsvHelper.removeNewLineChar;
import static com.google.common.base.Strings.nullToEmpty;


public class FilteredTestCaseCSVConverter implements CSVEntryConverter<TestCampaignItemInfo> {

    private static final String HEADING_SEPARATOR = ", ";
    private static final int NUMBER_OF_COLUMNS = 6;
    private static final String EMPTY_FIELD = "";

    @Override
    public String[] convertEntry(TestCampaignItemInfo testCampaignItemInfo) {
        String[] columns = new String[NUMBER_OF_COLUMNS];
        Arrays.fill(columns, EMPTY_FIELD);
        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();

        TestCaseInfo testCaseInfo = testCampaignItemInfo.getTestCase();

        columns[0] = nullToEmpty(testCaseInfo.getTestCaseId());
        columns[1] = nullToEmpty(testCaseInfo.getTitle());
        columns[2] = nullToEmpty(getUser(testCampaignItemInfo.getUser()));
        columns[3] = getReferenceTitle(testCampaignItemInfo.getResult());
        columns[4] = nullToEmpty(removeNewLineChar(testCaseInfo.getComment()));
        columns[5] = joiner.join(testCampaignItemInfo.getDefectIdsForCSV());


        return columns;
    }


}
