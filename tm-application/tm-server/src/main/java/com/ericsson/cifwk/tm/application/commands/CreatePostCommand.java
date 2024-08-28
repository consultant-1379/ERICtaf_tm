package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.services.TestCaseNotificationService;
import com.ericsson.cifwk.tm.application.services.TestCaseSubscriptionService;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.PostMapper;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CreatePostCommand implements Command<Post> {

    @Inject
    private PostRepository postRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private PostMapper postMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private TestCaseSubscriptionService testCaseSubscriptionService;

    @Inject
    private TestCaseNotificationService testCaseNotificationService;

    @Override
    public Response apply(Post post) {
        if (post.getUser() == null) {
            throw new BadRequestException(Responses.badRequest("User session expired"));
        }
        postRepository.persist(post);
        PostInfo dto = postMapper.mapEntity(post, new PostInfo());

        if (post.getObjectNameReference().equals(PostObjectNameReference.TEST_CASE.getValue())) {
            String userId = userHelper.getCurrentUser().getUserId();
            testCaseSubscriptionService.silentSubscribe(dto.getObjectId().toString(), userId);

            Optional<TestCase> testCase = Optional.ofNullable(testCaseRepository.find(dto.getObjectId()));
            if (testCase.isPresent()) {
                testCaseNotificationService.notifyComments(testCase.get(), userId, dto);
            }
        }

        return Responses.created(dto);
    }
}
