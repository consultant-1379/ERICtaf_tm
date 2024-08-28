package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.TestCaseNotificationService;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.integration.email.EmailService;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.netflix.governator.annotations.Configuration;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Set;

public class TestCaseNotificationServiceImpl implements TestCaseNotificationService {

    private String linkTemplate = "{0}/#tm/viewTC/{1}/version/{2}";

    @Configuration("deployment.host")
    private String host;

    @Inject
    private EmailService emailService;

    @Override
    public void notifySubscribers(TestCase testCase, String currentUserId) {
        Set<User> subscribers = testCase.getSubscribers();
        String subjectTemplate = "Test case {0} has been updated";
        String subject = MessageFormat.format(subjectTemplate, testCase.getTestCaseId());
        subscribers.forEach(s -> {
                String emailAddress = s.getExternalEmail();
                emailService.sendEmail(emailAddress, subject, createEditNotificationMessageBody(testCase, currentUserId));
            });
    }

    @Override
    public void notifyReviewers(TestCase testCase, String currentUserId, Set<User> reviewers) {
        String subjectTemplate = "Test case {0} review status has changed";
        String subject = MessageFormat.format(subjectTemplate, testCase.getTestCaseId());
        reviewers.forEach(s -> {
                String emailAddress = s.getExternalEmail();
                emailService.sendEmail(emailAddress, subject, createReviewNotificationMessageBody(testCase, currentUserId));
            });
    }

    @Override
    public void notifyComments(TestCase testCase, String currentUserId, PostInfo postInfo) {
        Set<User> subscribers = testCase.getSubscribers();
        String subjectTemplate = "Test case {0} has new comment";
        String subject = MessageFormat.format(subjectTemplate, testCase.getTestCaseId());
        subscribers.forEach(s -> {
                String emailAddress = s.getExternalEmail();
                emailService.sendEmail(emailAddress, subject, createCommentNotificationMessageBody(testCase, currentUserId, postInfo));
            });
    }

    private String createEditNotificationMessageBody(TestCase testCase, String userId) {
        String testCaseId = testCase.getTestCaseId();
        String version = testCase.getCurrentVersion().getVersion();
        String link = MessageFormat.format(linkTemplate, host, testCaseId, version);
        String messageBodyTemplate = "{0} updated test case {1}: {2}";
        String messageBody = MessageFormat.format(messageBodyTemplate, userId, testCaseId, link);

        return messageBody;
    }

    private String createReviewNotificationMessageBody(TestCase testCase, String userId) {
        String testCaseId = testCase.getTestCaseId();
        String version = testCase.getCurrentVersion().getVersion();
        String link = MessageFormat.format(linkTemplate, host, testCaseId, version);
        TestCaseStatus status = testCase.getCurrentVersion().getTestCaseStatus();
        String statusText = getReviewStatusText(status);
        String messageBodyTemplate = "{0} has {1} test case {2} : {3}";
        String messageBody = MessageFormat.format(messageBodyTemplate, userId, statusText, testCaseId, link);

        return messageBody;
    }

    private String createCommentNotificationMessageBody(TestCase testCase, String userId, PostInfo postInfo) {
        String testCaseId = testCase.getTestCaseId();
        String version = testCase.getCurrentVersion().getVersion();
        String link = MessageFormat.format(linkTemplate, host, testCaseId, version);
        String messageBodyTemplate = "{0} has commented on test case {1}: {2} \n\n {0}:\n" + postInfo.getMessage();
        String messageBody = MessageFormat.format(messageBodyTemplate, userId, testCaseId, link);

        return messageBody;
    }

    private String getReviewStatusText(TestCaseStatus status) {
        String text = "";
        switch (status) {
            case REVIEW:
                text = "requested a review of";
                break;

            case APPROVED:
                text = "approved";
                break;

            case REJECTED:
                text = "rejected";
                break;

            default:
        }
        return text;
    }

}
