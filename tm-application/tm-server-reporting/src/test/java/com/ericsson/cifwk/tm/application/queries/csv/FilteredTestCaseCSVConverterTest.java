package com.ericsson.cifwk.tm.application.queries.csv;

import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Set;

/**
 * Created by egergle on 17/07/2015.
 */
public class FilteredTestCaseCSVConverterTest extends TestCase {

    private static final String HEADING_SEPARATOR = ", ";

    @Test
    public void testConvertEntry() throws Exception {
        FilteredTestCaseCSVConverter filteredTestCaseCSVConverter = new FilteredTestCaseCSVConverter();
        TestCampaignItemInfo testPlanItemInfo = getTestplanItem();
        String[] data = filteredTestCaseCSVConverter.convertEntry(testPlanItemInfo);

        Joiner joiner = Joiner.on(HEADING_SEPARATOR).skipNulls();

        TestCaseInfo testCaseInfo = testPlanItemInfo.getTestCase();
        assertEquals(testCaseInfo.getTestCaseId(), data[0]);
        assertEquals(testCaseInfo.getTitle(), data[1]);
        assertEquals(testPlanItemInfo.getResult().getTitle(), data[3]);
        assertEquals(testCaseInfo.getComment(), data[4]);
        assertEquals(joiner.join(testPlanItemInfo.getDefectIdsForCSV()), data[5]);

    }

    private TestCampaignItemInfo getTestplanItem() {
        TestCampaignItemInfo testPlanItemInfo = new TestCampaignItemInfo();
        TestCaseInfo testcase = new TestCaseInfo();
        testcase.setTestCaseId("TestCase1");
        testcase.setTitle("TestTitle");
        testcase.setComment("Test");
        testPlanItemInfo.setTestCase(testcase);

        testPlanItemInfo.setId(1L);
        ReferenceDataItem result = new ReferenceDataItem("1","Pass");
        testPlanItemInfo.setResult(result);

        DefectInfo defectInfo = new DefectInfo();
        defectInfo.setId(1L);
        defectInfo.setExternalTitle("test");
        defectInfo.setExternalId("test");

        Set<DefectInfo> defectInfos = Sets.newHashSet();
        defectInfos.add(defectInfo);
        testPlanItemInfo.setDefects(defectInfos);


        testPlanItemInfo.setTestCase(testcase);
        return testPlanItemInfo;
    }
}