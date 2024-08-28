package com.ericsson.cifwk.tm.fun.rest.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static com.ericsson.cifwk.tm.fun.common.tms.CommentsTestCaseTestHelper.commentsContains;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class CommentsTestCaseTest extends BaseFuncTest {
    private static final String TEST_CASE_PREDEFINED_COMMENTS_ID = "TestCase_for_comments_with_comments_02";

    @Test
    @TestId(id = "DURACI-2974_Func_1", title = "AC: 1. Any authorized user can create comments for each test case")
    public void createComment() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult testCaseResult = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

        CustomAsserts.checkTestStep(testCaseResult);

        String testCaseId = testCaseResult.getSavedTestCaseInfo().getTestCaseId();
        CustomAsserts.checkTestStep(loginOperator.logout());

        CustomAsserts.checkTestStep(loginOperator.login());

        TestCaseInfo testCaseInfo = showAssociatedComments(testCaseId);
        PostInfo newComment = new PostInfo();
        newComment.setMessage(generateUniqueCommentMessage());
        Result<PostInfo> result = tmRestOperator.createAssociatedComment(testCaseInfo, newComment);
        CustomAsserts.checkTestStep(result);

        PostInfo createdComment = result.getValue();
        assertEquals(newComment.getMessage(), createdComment.getMessage());

        CustomAsserts.checkTestStep(tmRestOperator.hideAssociatedComments());
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "DURACI-2974_Func_2", title = "AC: 2. Only author can delete a comment")
    public void deleteComment() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult testCaseResult = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

        CustomAsserts.checkTestStep(testCaseResult);

        String testCaseId = testCaseResult.getSavedTestCaseInfo().getTestCaseId();
        CustomAsserts.checkTestStep(loginOperator.logout());

        CustomAsserts.checkTestStep(loginOperator.login());
        TestCaseInfo testCaseInfo = showAssociatedComments(testCaseId);

        PostInfo newComment = new PostInfo();
        newComment.setMessage(generateUniqueCommentMessage());

        Result<PostInfo> resultCreation = tmRestOperator.createAssociatedComment(testCaseInfo, newComment);
        CustomAsserts.checkTestStep(resultCreation);
        PostInfo commentForDeletion = resultCreation.getValue();

        Result<PostInfo> resultDeletion = tmRestOperator.deleteAssociatedComment(testCaseInfo, commentForDeletion);
        CustomAsserts.checkTestStep(resultDeletion);

        CustomAsserts.checkTestStep(loginOperator.logout());
        CustomAsserts.checkTestStep(loginOperator.login());
        testCaseInfo = showTestCase(testCaseId);
        Result<List<PostInfo>> resultComments = tmRestOperator.getAssociatedComments(testCaseInfo);
        CustomAsserts.checkTestStep(resultComments);

        List<PostInfo> comments = resultComments.getValue();
        assertFalse(commentsContains(comments, commentForDeletion));

        CustomAsserts.checkTestStep(loginOperator.logout());
    }


    @Test
    @TestId(id = "DURACI-2974_Func_2.1", title = "AC: 2.1 Delete action is restricted for non-authors")
    public void tryDeleteNotOwnComment() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        InputStream testCaseJson = getFullTestCaseJson();
        CreateEditTestCaseResult testCaseResult = tmRestOperator.createTestCase(testCaseJson, ProductType.ENM.getProductInfo());

        CustomAsserts.checkTestStep(testCaseResult);

        String testCaseId = testCaseResult.getSavedTestCaseInfo().getTestCaseId();
        CustomAsserts.checkTestStep(loginOperator.logout());

        CustomAsserts.checkTestStep(loginOperator.login());
        TestCaseInfo testCaseInfo = showAssociatedComments(testCaseId);

        PostInfo newComment = new PostInfo();
        newComment.setMessage(generateUniqueCommentMessage());
        Result<PostInfo> resultCreation = tmRestOperator.createAssociatedComment(testCaseInfo, newComment);
        CustomAsserts.checkTestStep(resultCreation);

        CustomAsserts.checkTestStep(tmRestOperator.hideAssociatedComments());
        CustomAsserts.checkTestStep(loginOperator.logout());

        //Delete comment by any other user
        CustomAsserts.checkTestStep(loginOperator.login());
        showAssociatedComments(testCaseId);
        CustomAsserts.checkTestStep(tmRestOperator.checkAssociatedCommentDeleteIsRestricted(testCaseInfo, resultCreation.getValue()));

        CustomAsserts.checkTestStep(loginOperator.logout());

    }

    @Test
    @TestId(id = "DURACI-2974_AdditionalFunc_1", title = "Check comments count label")
    public void getCommentsCount() {
        createRestOperators();

        CustomAsserts.checkTestStep(loginOperator.login());

        TestCaseInfo testCaseInfo = showTestCase(TEST_CASE_PREDEFINED_COMMENTS_ID);
        Result<String> result = tmRestOperator.checkCommentsCount(testCaseInfo);
        CustomAsserts.checkTestStep(result);
        String label = result.getValue();
        if (label.indexOf("(") > -1 && label.indexOf(")") > label.indexOf("(")) {
            label = label.substring(label.indexOf("(") + 1, label.indexOf(")"));
        }
        assertEquals("2", label);
        CustomAsserts.checkTestStep(loginOperator.logout());
    }


    private TestCaseInfo showTestCase(String testCaseId) {
        CustomAsserts.checkTestStep(tmRestOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, testCaseId));

        TestCasesResult searchResult = tmRestOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(searchResult);

        assertEquals(1, searchResult.getTestCases().size());
        assertEquals(true, searchResult.getTestCases().iterator().hasNext());

        TestCaseInfo testCaseInfo = searchResult.getTestCases().iterator().next();
        assertEquals(testCaseId, testCaseInfo.getTestCaseId());

        Result<TestCaseInfo> testCasesResult = tmRestOperator.viewTestCase(testCaseId);
        CustomAsserts.checkTestStep(testCasesResult);

        return testCaseInfo;
    }

    private TestCaseInfo showAssociatedComments(String testCaseId) {
        TestCaseInfo testCaseInfo = showTestCase(testCaseId);
        CustomAsserts.checkTestStep(tmRestOperator.getAssociatedComments(testCaseInfo));
        return testCaseInfo;
    }

    private String generateUniqueCommentMessage() {
        return ("Test message for comments system "
                + new Date().toString())
                + " (" + Math.random() + ")";
    }
}

