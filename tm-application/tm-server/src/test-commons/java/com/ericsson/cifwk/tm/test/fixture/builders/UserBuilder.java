package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.users.User;

public class UserBuilder extends EntityBuilder<User> {

    public UserBuilder() {
        super(new User());
    }

    public UserBuilder withExternalId(String externalId) {
        entity.setExternalId(externalId);
        return this;
    }

    public UserBuilder withExternalEmail(String userEmail) {
        entity.setExternalEmail(userEmail);
        return this;
    }

    public UserBuilder withExternalName(String externalName) {
        entity.setExternalName(externalName);
        return this;
    }

    public UserBuilder withExternalSurname(String externalSurname) {
        entity.setExternalSurname(externalSurname);
        return this;
    }

    public UserBuilder withUserName(String userName) {
        entity.setUserName(userName);
        return this;
    }
}
