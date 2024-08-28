package com.ericsson.cifwk.tm.test.fixture.builders;


import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.users.User;

public class PostBuilder extends EntityBuilder<Post> {

    public PostBuilder() {
        super(new Post());
    }

    public PostBuilder withUser(User user) {
        entity.setUser(user);
        return this;
    }

    public PostBuilder withMessage(String message) {
        entity.setMessage(message);
        return this;
    }

    public PostBuilder withDeleted(boolean value) {
        if (value) {
            entity.delete();
        } else {
            entity.setDeleted(false);
            entity.setDeletedAt(null);
        }
        return this;
    }


    public PostBuilder withObjectId(long objectId) {
        entity.setObjectId(objectId);
        return this;
    }

    public PostBuilder withObjectNameReference(PostObjectNameReference objNameRef) {
        entity.setObjectNameReference(objNameRef.getValue());
        return this;
    }
}
