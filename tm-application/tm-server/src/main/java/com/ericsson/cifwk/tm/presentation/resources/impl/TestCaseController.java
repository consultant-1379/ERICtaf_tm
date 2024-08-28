/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateTestCaseCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTestCaseCommand;
import com.ericsson.cifwk.tm.application.commands.GenerateExcelReportCommand;
import com.ericsson.cifwk.tm.application.commands.ImportTestCasesCommand;
import com.ericsson.cifwk.tm.application.commands.ReviewTestCaseCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTestCaseCommand;
import com.ericsson.cifwk.tm.application.queries.TestCaseQuerySet;
import com.ericsson.cifwk.tm.application.queries.TestCaseVersionQuerySet;
import com.ericsson.cifwk.tm.application.requests.TestCaseReviewRequest;
import com.ericsson.cifwk.tm.application.services.TestCaseSubscriptionService;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;
import com.ericsson.cifwk.tm.presentation.RequestPreconditions;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseSubscriptionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.ericsson.cifwk.tm.presentation.resources.PostResource;
import com.ericsson.cifwk.tm.presentation.resources.TestCaseResource;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Controller
public class TestCaseController implements TestCaseResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTestCaseCommand createTestCaseCommand;

    @Inject
    private UpdateTestCaseCommand updateTestCaseCommand;

    @Inject
    private DeleteTestCaseCommand deleteTestCaseCommand;

    @Inject
    private GenerateExcelReportCommand generateCSVCommand;

    @Inject
    private TestCaseQuerySet testCaseQuerySet;

    @Inject
    private TestCaseVersionQuerySet testCaseVersionQuerySet;

    @Inject
    private TestCaseViewFactory testCaseViewFactory;

    @Inject
    private ImportTestCasesCommand importTestCasesCommand;

    @Inject
    private ReviewTestCaseCommand reviewTestCaseCommand;

    @Context
    private UriInfo uriInfo;

    @Inject
    private Provider<PostController> postControllerProvider;

    @Inject
    private TestCaseSubscriptionService subscriptionService;

    @Override
    public Response hasTest(String testCaseId) {
        return testCaseQuerySet.testCasesExist(testCaseId);
    }

    @Override
    public Response getTestCase(String testCaseId, String version, String view) {
        Class<? extends DtoView<TestCaseInfo>> dtoView = testCaseViewFactory.getByName(view);
        if (version != null && !version.isEmpty()) {
            return testCaseVersionQuerySet.getTestCaseVersion(testCaseId, version, true, dtoView);
        } else {
            return testCaseQuerySet.getTestCase(testCaseId, dtoView);
        }
    }

    @Override
    public Response getTestCases(
            String id,
            String q,
            int page, int perPage,
            String orderBy, String orderMode,
            String view) {
        RequestPreconditions.checkArgument(id == null || q == null, "Can't query both by 'id' and 'q' (choose one)");
        Class<? extends DtoView<TestCaseInfo>> dtoView = testCaseViewFactory.getByName(view);
        if (id != null) {
            Iterable<Long> ids = Sanitization.splitCommaSeparatedIds(id);
            return testCaseQuerySet.getTestCases(ids, dtoView);
        } else {
            Query query = Query.fromQueryString(q);
            query.sortBy(orderBy, orderMode);
            return testCaseQuerySet.getTestCasesByQuery(query, page, perPage, uriInfo, dtoView);
        }
    }

    @Override
    public Response getTestCaseIds(String testCaseIds) {
        Iterable<String> ids = Sanitization.splitCommaSeparated(testCaseIds);
        return testCaseQuerySet.getTestCasesIds(Lists.newArrayList(ids));
    }

    @Override
    public Response getCompletion(Long productId, List<Long> featureIds, List<Long> componentIds, String search, int limit) {
        return testCaseQuerySet.getCompletion(productId, featureIds, componentIds, search, limit);
    }

    @Override
    public Response create(TestCaseInfo testCaseInfo) {
        Preconditions.checkArgument(testCaseInfo.getId() == null, "Given test case has already been created");
        return commandProcessor.process(createTestCaseCommand, testCaseInfo);
    }

    @Override
    public Response update(Long id, TestCaseInfo testCaseInfo) {
        Preconditions.checkArgument(
                id.equals(testCaseInfo.getId()),
                "Test ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateTestCaseCommand, testCaseInfo);
    }

    @Override
    public Response review(Long id, String status, String type, long reviewGroupId, String reviewUserId) {
        Preconditions.checkArgument(status != null, "Test Case  review status has not been set");
        Preconditions.checkArgument(id != null, "Test Case Id has not been set");
        Preconditions.checkArgument(type != null, "Test Case reviews type has not been set");
        TestCaseReviewRequest testCaseReviewRequest = new TestCaseReviewRequest(id, status, type, reviewGroupId, reviewUserId);

        return commandProcessor.process(reviewTestCaseCommand, testCaseReviewRequest);
    }

    @Override
    public Response delete(Long testCaseId) {
        return commandProcessor.process(deleteTestCaseCommand, testCaseId);
    }

    @Override
    public PostResource comments(Long testCaseId) {
        PostController postController = postControllerProvider.get();
        postController.setRelatedResource(PostObjectNameReference.TEST_CASE, testCaseId);
        return postController;
    }

    @Override
    public Response getReport(String query) {
        return commandProcessor.process(generateCSVCommand, query);
    }

    @Override
    public Response updateTestCasesFromReport(FormDataMultiPart formDataMultiPart) {
        List<FormDataBodyPart> parts = formDataMultiPart.getFields("file");
        return commandProcessor.process(importTestCasesCommand, parts);
    }

    @Override
    public Response subscribe(TestCaseSubscriptionInfo subscriptionInfo) {
        return subscriptionService.subscribe(subscriptionInfo.getTestCaseId(), subscriptionInfo.getUserId());
    }

    @Override
    public Response unsubscribe(TestCaseSubscriptionInfo subscriptionInfo) {
        return subscriptionService.unsubscribe(subscriptionInfo.getTestCaseId(), subscriptionInfo.getUserId());
    }

    @Override
    public Response isSubscribed(String testCaseId, String userId) {
        return subscriptionService.isUserSubscribed(testCaseId, userId);
    }
}
