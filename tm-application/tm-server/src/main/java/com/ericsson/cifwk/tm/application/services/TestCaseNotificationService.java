package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.TestCaseNotificationServiceImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.google.inject.ImplementedBy;

import java.util.Set;

@ImplementedBy(TestCaseNotificationServiceImpl.class)
public interface TestCaseNotificationService {

    void notifySubscribers(TestCase testCase, String currentUserId);

    void notifyReviewers(TestCase testCase, String currentUserId, Set<User> reviewers);

    void notifyComments(TestCase testCase, String currentUserId, PostInfo postInfo);
}
