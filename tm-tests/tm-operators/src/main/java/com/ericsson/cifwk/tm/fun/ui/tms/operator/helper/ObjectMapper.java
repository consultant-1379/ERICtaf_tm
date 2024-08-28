package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper;

import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.view.TestCaseDetailsRegionViewModel;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Set;

/**
 * Created by erusbob on 18/03/2015.
 */
public class ObjectMapper {

    private static final String FAKE_REF_ID = "1";

    private static final char SEPARATOR = ',';

    public TestCaseInfo mapNewTestCase(TestCaseDetailsRegionViewModel tcDetailsView) {

        TestCaseInfo tcInfo = new TestCaseInfo();


        tcInfo.setTestCaseId(tcDetailsView.getTestCaseId().getText());
        tcInfo.setTitle(tcDetailsView.getTestCaseTitle().getText());
        tcInfo.setDescription(tcDetailsView.getTestCaseDescription().getText());
        tcInfo.setType(new ReferenceDataItem(FAKE_REF_ID, tcDetailsView.getTestCaseType().getText()));

        Set<ReferenceDataItem> referenceDataItems = retrieveRefs(tcDetailsView.getTestCaseComponent().getText());
        tcInfo.setTechnicalComponents(referenceDataItems);

        tcInfo.setPriority(new ReferenceDataItem(FAKE_REF_ID, tcDetailsView.getTestCasePriority().getText()));
        // ~ groups
        tcInfo.setGroups(retrieveRefs(tcDetailsView.getTestCaseGroup().getText()));
        // ~ contexts
        tcInfo.setContexts(retrieveRefs(tcDetailsView.getTestCaseContext().getText()));
        tcInfo.setPrecondition(tcDetailsView.getTestCasePrecondition().getText());
        tcInfo.setExecutionType(new ReferenceDataItem(FAKE_REF_ID, tcDetailsView.getTestCaseExecType().getText()));
        tcInfo.setTestCaseStatus(new ReferenceDataItem(FAKE_REF_ID, tcDetailsView.getTestCaseStatus().getText()));
        return tcInfo;
    }

    private static Set<ReferenceDataItem> retrieveRefs(final String targetValues) {
        Iterable<String> splitTextValues = splitTextValues(targetValues);

        Iterable<ReferenceDataItem> transList = Iterables.transform(splitTextValues, new Function<String, ReferenceDataItem>() {
            @Override
            public ReferenceDataItem apply(String input) {
                return new ReferenceDataItem(FAKE_REF_ID, input);
            }
        });
        return ImmutableSet.copyOf(transList);
    }

    private static Iterable<String> splitTextValues(final String text) {
        return Splitter.on(SEPARATOR)
                .trimResults()
                .omitEmptyStrings()
                .split(text);
    }
}
