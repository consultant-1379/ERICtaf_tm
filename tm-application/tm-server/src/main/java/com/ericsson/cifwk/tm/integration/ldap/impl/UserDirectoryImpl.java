/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.ldap.impl;

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.infrastructure.security.RetryIfFails;
import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.integration.ldap.LDAPConfiguration;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Sets;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.ericsson.cifwk.tm.integration.ldap.impl.LDAPAttributes.*;
import static java.lang.String.format;

@Singleton
@Integration
public class UserDirectoryImpl implements UserDirectory {

    private static final String BASE_FILTER = "(&((&(objectCategory=Person)(objectClass=User)))";
    private static final String ROLE_NAMES_DELIMETER = ",";

    private final Logger logger = LoggerFactory.getLogger(UserDirectoryImpl.class);
    private final String[] returnAttributes = LDAPAttributes.getAll();

    @Inject
    private Provider<UserRepository> userRepositoryProvider;

    @Inject
    private UserMapper userMapper;

    @Inject
    private LdapContextFactory ldapContextFactory;

    @Inject
    private LDAPConfiguration configuration;

    private SearchControls searchControls;

    public UserDirectoryImpl() {
        searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(returnAttributes);
    }

    @Override
    public UserInfo findByUsernameOrEmail(String query) {
        return find(query, LDAPSearchType.USER_AND_EMAIL);
    }

    UserInfo find(String query, LDAPSearchType searchType) {
        UserRepository userRepository = userRepositoryProvider.get();
        User user = null;
        if (LDAPSearchType.EMAIL.equals(searchType)) {
            user = userRepository.findByExternalEmail(query);
        } else if (LDAPSearchType.USER.equals(searchType)) {
            user = userRepository.findByExternalId(query);
        } else if (LDAPSearchType.USER_AND_EMAIL.equals(searchType)) {
            user = userRepository.findByExternalIdOrEmail(query);
        }

        if (user == null || !user.hasExternalAttributes()) {
            return findInLDAP(query, searchType);
        } else {
            return userMapper.mapEntity(user, new UserInfo());
        }
    }

    @Override
    public UserInfo findInLDAP(String query, LDAPSearchType searchType) {
        final String filter = getFilter(query, searchType);
        try {
            RetryIfFails<NamingEnumeration<SearchResult>> retry = getNamingEnumerationRetryIfFails(filter);
            NamingEnumeration<SearchResult> result = retry.runAll();
            if (result.hasMore()) {
                SearchResult rs = result.next();
                Attributes attrs = rs.getAttributes();
                UserInfo userInfo = new UserInfo();

                userInfo.setUserId(extract(attrs.get(SAM_ACCOUNT_NAME.getName())));
                userInfo.setEmail(extract(attrs.get(MAIL.getName())));
                userInfo.setName(extract(attrs.get(GIVEN_NAME.getName())));
                userInfo.setSurname(extract(attrs.get(LAST_NAME.getName())));
                userInfo.setUserName(userInfo.getName() + " " + userInfo.getSurname());
                userInfo.addRoles(
                        getRoleNamesForGroups(
                                extractSet(attrs.get(MEMBER_OF.getName()))));
                return userInfo;
            }
        } catch (NamingException e) {
            logger.error("Unable to findOrImport user: ", e);
        }

        return null;
    }

    private RetryIfFails<NamingEnumeration<SearchResult>> getNamingEnumerationRetryIfFails(final String filter) {
        RetryIfFails<NamingEnumeration<SearchResult>> retry;
        retry = new RetryIfFails<NamingEnumeration<SearchResult>>(ldapContextFactory) {
            @Override
            protected NamingEnumeration<SearchResult> run(LdapContext ctx) throws NamingException {
                LinkedHashMap<String, String> searchBasesAndPrincipals = configuration.getSearchBases();
                for (Map.Entry<String, String> searchBase : searchBasesAndPrincipals.entrySet()) {
                    try {
                        NamingEnumeration<SearchResult> search =
                                ctx.search(searchBase.getKey(), filter, searchControls);

                        if (search.hasMore()) {
                            return search;
                        }
                    } catch (NamingException e) {
                        logger.debug("Exception trying to search for name", e);
                    }
                }
                throw new NamingException();
            }
        };
        return retry;
    }

    private String extract(Attribute attribute) {
        try {
            return attribute.get(0).toString();
        } catch (NamingException | NullPointerException e) { //NOSONAR
            return null;
        }
    }

    private Set<String> extractSet(Attribute attribute) {
        try {
            return new HashSet<>(LdapUtils.getAllAttributeValues(attribute));
        } catch (NamingException | NullPointerException e) { //NOSONAR
            return Sets.newHashSet();
        }
    }


    private String getFilter(String searchValue, LDAPSearchType searchBy) {
        String filter = BASE_FILTER;
        if (LDAPSearchType.EMAIL.equals(searchBy)) {
            filter += format("(%s=%s))", MAIL.getName(), searchValue);
        } else if (LDAPSearchType.USER.equals(searchBy)) {
            filter += format("(%s=%s))", SAM_ACCOUNT_NAME.getName(), searchValue);
        } else if (LDAPSearchType.USER_AND_EMAIL.equals(searchBy)) {
            filter += format("(|(%s=%s)(%s=%s)))", SAM_ACCOUNT_NAME.getName(),
                    searchValue, MAIL.getName(), searchValue);
        }
        return filter;
    }

    private Set<String> getRoleNamesForGroups(Set<String> groupNames) {
        Set<String> roleNames = new HashSet<>(groupNames.size());
        Map<String, String> groupRolesMap = configuration.getGroupRolesMap();

        for (Map.Entry<String, String>  groupRoles : groupRolesMap.entrySet()) {
            String strRoleNames = groupRolesMap.get(groupRoles.getKey());
            if (strRoleNames != null) {
                Collections.addAll(roleNames, strRoleNames.split(ROLE_NAMES_DELIMETER));
            }
        }
        return roleNames;
    }

}
