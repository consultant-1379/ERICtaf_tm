/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.TestCaseReviewRequest;
import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.application.services.TestCaseNotificationService;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroupRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;

public class ReviewTestCaseCommand implements Command<TestCaseReviewRequest> {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private ReviewGroupRepository reviewGroupRepository;

    @Inject
    private TestCaseNotificationService notificationService;

    @Inject
    private UserManagementService userManagementService;

    private Optional<ReviewGroup> reviewGroup = Optional.ofNullable(null);
    private Optional<User> reviewUser = Optional.ofNullable(null);

    @Override
    public Response apply(TestCaseReviewRequest input) {
        TestCase testCase = testCaseRepository.find(input.getTestCaseId());
        TestCaseVersion currentVersion = testCase.getCurrentVersion();
        Optional<TestCaseStatus> testCaseStatus = Optional.ofNullable(currentVersion.getTestCaseStatus());
        validate(currentVersion, testCaseStatus);

        Optional<TestCaseStatus> newTestCaseStatus = TestCaseStatus.getByName(input.getStatus());
        Optional<ReviewType> type = ReviewType.getEnum(input.getType());
        if (newTestCaseStatus.isPresent() && type.isPresent()) {
            currentVersion.setTestCaseStatus(newTestCaseStatus.get());
            userHelper.setUpdateUser(testCase);
            if (!newTestCaseStatus.get().equals(TestCaseStatus.REJECTED) &&
                    !newTestCaseStatus.get().equals(TestCaseStatus.CANCELLED)) {
                addReviewerBasedOnType(currentVersion,
                        newTestCaseStatus.get(),
                        input.getReviewGroupId(),
                        input.getReviewUserId());
            } else {
                currentVersion.setReviewGroup(null);
                currentVersion.setReviewUser(null);
            }
            if (type.get().equals(ReviewType.MAJOR)) {
                currentVersion.setMajorVersion(currentVersion.getMajorVersion() + 1);
                currentVersion.setMinorVersion(0L);
            }
            testCaseRepository.persist(testCase);

        } else {
            return Responses.notFound();
        }
        sendNotifications(testCase);
        TestCaseInfo dto = testCaseMapper.mapEntity(testCase, TestCaseInfo.class, TestCaseView.Detailed.class);
        return Responses.ok(dto);
    }

    private void sendNotifications(TestCase testCase) {
        if (this.reviewGroup.isPresent()) {
            notifyReviewers(testCase, this.reviewGroup.get().getUsers());
        }

        if (this.reviewUser.isPresent()) {
            String currentUserId = userHelper.getCurrentUser().getUserId();
            notificationService.notifyReviewers(testCase, currentUserId, Sets.newHashSet(this.reviewUser.get()));
        }
    }

    private void validate(TestCaseVersion currentVersion, Optional<TestCaseStatus> testCaseStatus) {
        if (!testCaseStatus.isPresent()) {
            throw new BadRequestException(Responses.badRequest("This test case has empty test case status"));
        }

        if (currentVersion.getTestCaseStatus().equals(TestCaseStatus.APPROVED)) {
            throw new BadRequestException(Responses.badRequest("This test case version has already been approved"));
        }
    }

    private void addReviewGroup(TestCaseVersion testCaseVersion, TestCaseStatus testCaseStatus, long reviewGroupId) {
        if (testCaseStatus.equals(TestCaseStatus.REVIEW)) {
            this.reviewGroup = Optional.ofNullable(reviewGroupRepository.find(reviewGroupId));
            if (this.reviewGroup == null) {
                throw new BadRequestException(Responses.badRequest("No review group was found"));
            }
            testCaseVersion.setReviewGroup(this.reviewGroup.get());
            testCaseVersion.setReviewUser(null);
        }
    }

    private void addReviewUser(TestCaseVersion testCaseVersion, TestCaseStatus testCaseStatus, String reviewUserId) {
        if (testCaseStatus.equals(TestCaseStatus.REVIEW)) {
            this.reviewUser = Optional.ofNullable(userManagementService.fetchUser(reviewUserId));
            if (!this.reviewUser.isPresent()) {
                throw new BadRequestException(Responses.badRequest("No review user was found"));
            }
            testCaseVersion.setReviewUser(this.reviewUser.get());
            testCaseVersion.setReviewGroup(null);
        }
    }

    private void notifyReviewers(TestCase testCase, Set<User> reviewers) {
        TestCaseStatus status = testCase.getCurrentVersion().getTestCaseStatus();
        if (status.equals(TestCaseStatus.REVIEW) || status.equals(TestCaseStatus.APPROVED) || status.equals(TestCaseStatus.REJECTED)) {
            String currentUserId = userHelper.getCurrentUser().getUserId();
            notificationService.notifyReviewers(testCase, currentUserId, reviewers);
        }
    }

    private void addReviewerBasedOnType(TestCaseVersion testCaseVersion,
                                        TestCaseStatus testCaseStatus,
                                        long reviewGroupId,
                                        String reviewUserId) {

        if (reviewGroupId != 0 && reviewUserId != null) {
            throw new BadRequestException(Responses.badRequest("Test case cannot have both review group and a single reviewer for review"));
        }
        if (reviewGroupId == 0) {
            addReviewUser(testCaseVersion, testCaseStatus, reviewUserId);
        } else {
            addReviewGroup(testCaseVersion, testCaseStatus, reviewGroupId);
        }

    }
}
