package com.ericsson.cifwk.tm.fun.common;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;

import static com.ericsson.cifwk.taf.assertions.TafAsserts.assertTrue;

public class HostHelper {

    private final Host host;

    public HostHelper() {
        host = DataHandler.getHostByName(Hosts.TEST_MANAGEMENT_SYSTEM);
    }

    public HostHelper(Host host) {
        this.host = host;
    }

    public Host getHost() {
        return host;
    }

    public User getUserByName(UserType type, final String name) {
        List<User> users = host.getUsers(type);
        Optional<User> user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return name.equals(input.getUsername());
            }
        });
        assertTrue(user.isPresent());
        return user.get();
    }

}
