package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;


public class DeletePostCommand implements Command<Long> {

    @Inject
    private PostRepository postRepository;

    @Inject
    private UserSessionService userSessionService;

    @Override
    public Response apply(Long input) {
        String userId = getUserId(null);
        if (userId == null) {
            throw new BadRequestException(Responses.badRequest("User session expired"));
        }
        Post post = postRepository.find(input);
        if (post == null) {
            throw new NotFoundException(Responses.notFound());
        }
        //Delete available for creator only
        if (!post.getUser().getExternalId().equals(userId)) {
            return Responses.badCredentials("Delete available for creator only");
        }

        post.delete();
        postRepository.save(post);
        return Responses.noContent();
    }

    private String getUserId(String userId) {
        if (userId != null) {
            return userId;
        } else {
            return userSessionService.getCurrentUser().getUserId();
        }
    }

}
