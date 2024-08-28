package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.TestCaseSubscriptionService;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseSubscriptionInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class TestCaseSubscriptionServiceImpl implements TestCaseSubscriptionService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Override
    @Transactional
    public Response subscribe(String testCaseId, String userId) {
        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null) {
            return Responses.badRequest("Test case " + testCaseId + " does not exist");
        }
        User user = userRepository.findByExternalId(userId);
        if (user == null) {
            return Responses.badRequest("User with id " + userId + " does not exist");
        }
        testCase.addSubscriber(user);
        testCaseRepository.save(testCase);
        TestCaseSubscriptionInfo subscriptionInfo = new TestCaseSubscriptionInfo(testCaseId, userId, true);
        return Responses.created(subscriptionInfo);
    }

    @Override
    @Transactional
    public Response unsubscribe(String testCaseId, String userId) {
        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null) {
            return Responses.badRequest("Test case " + testCaseId + " does not exist");
        }
        User user = userRepository.findByExternalId(userId);
        if (user == null) {
            return Responses.badRequest("User with id " + userId + " does not exist");
        }
        testCase.removeSubscriber(user);
        testCaseRepository.save(testCase);
        TestCaseSubscriptionInfo subscriptionInfo = new TestCaseSubscriptionInfo(testCaseId, userId, false);
        return Responses.ok(subscriptionInfo);
    }

    @Override
    public Response isUserSubscribed(String testCaseId, String userId) {
        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null) {
            return Responses.badRequest("Test case " + testCaseId + " does not exist");
        }
        User user = userRepository.findByExternalId(userId);
        if (user == null) {
            return Responses.badRequest("User with id " + userId + " does not exist");
        }
        boolean subscribed = testCase.getSubscribers().contains(user);
        TestCaseSubscriptionInfo subscriptionInfo = new TestCaseSubscriptionInfo(testCaseId, userId, subscribed);
        return Responses.ok(subscriptionInfo);
    }

    @Override
    @Transactional
    public boolean silentSubscribe(String testCaseId, String userId) {
        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null) {
            return false;
        }
        User user = userRepository.findByExternalId(userId);
        if (user == null) {
            return false;
        }
        boolean isSubscribed = testCase.getSubscribers().contains(user);
        if (isSubscribed) {
            return false;
        }
        testCase.addSubscriber(user);
        testCaseRepository.save(testCase);
        return true;
    }

}
