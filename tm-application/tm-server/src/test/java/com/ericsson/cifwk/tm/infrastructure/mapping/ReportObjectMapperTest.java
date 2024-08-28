package com.ericsson.cifwk.tm.infrastructure.mapping;


import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportView;
import com.ericsson.cifwk.tm.test.fixture.Faker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by egergle on 28/05/2015.
 */
public class ReportObjectMapperTest {

    @Mock
    private TestCaseService testCaseService;

    @InjectMocks
    private ReportObjectMapper reportObjectMapper;

    private static int perPage = 20;

    private static String TITLE = "title";
    private static String OBJECTIVE = "Objective";
    private static String PRECONDITION = "Precondition";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMapTestCases() throws Exception {
        List<TestCase> testCases = Arrays.asList(
                getTestCaseWithTestAndVerifySteps(),
                getTestCaseWithTestAndVerifySteps(),
                getTestCaseWithTestAndVerifySteps());

        doReturn(new Paginated<>(1, perPage, testCases, testCases.size()))
                .when(testCaseService).customSearch(any(Query.class), eq(1), eq(perPage));

        List<ReportObject> listOfReports = reportObjectMapper.mapTestCases("Test", ReportView.Simple.class);
        Iterator<TestCase> testCaseIterator = testCases.iterator();
        for (ReportObject reportObject : listOfReports) {
            TestCase testCase = testCaseIterator.next();
            TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
            assertEquals(reportObject.getHeaderInfo().size(), 7);

            assertThat(reportObject.getHeaderInfo().get(TITLE),containsString(testCaseVersion.getTitle()));
            assertThat(reportObject.getHeaderInfo().get(OBJECTIVE),containsString(testCaseVersion.getDescription()));
            assertThat(reportObject.getHeaderInfo().get(PRECONDITION),containsString(testCaseVersion.getPrecondition()));

            int childrenSize = reportObject.getChildren().size();
            assertEquals(childrenSize, 3);
            Iterator<TestStep> testStepIterator = testCaseVersion.getTestSteps().iterator();
            reportObject.getChildren().remove(childrenSize -1); // remove the comment reportObject at the end.
            for (ReportObject reportTestStep : reportObject.getChildren()) {
                TestStep testStep = testStepIterator.next();
                assertThat(reportTestStep.getValue(), containsString(testStep.getTitle()));

                assertEquals(reportTestStep.getChildren().size(), 2);
                Iterator<VerifyStep> verifyStepIterator = testStep.getVerifications().iterator();
                for (ReportObject reportVerifyStep : reportTestStep.getChildren()) {
                    VerifyStep verifyStep = verifyStepIterator.next();
                    assertThat(reportVerifyStep.getValue(), containsString(verifyStep.getVerifyStep()));

                }

            }
        }

    }

    private TestCase getTestCaseWithTestAndVerifySteps() {
        return buildTestCase()
                .withId(Faker.autoIncrement())
                .addTestStep(getTestStepWithVerifySteps())
                .addTestStep(getTestStepWithVerifySteps())
                .build();
    }

    private TestStep getTestStepWithVerifySteps() {
        return buildTestStep()
                .withId(Faker.autoIncrement())
                .addVerifyStep(getVerifyStep())
                .addVerifyStep(getVerifyStep())
                .build();
    }

    private VerifyStep getVerifyStep() {
        return buildVerifyStep()
                .withId(Faker.autoIncrement())
                .build();
    }

}