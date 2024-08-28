package com.ericsson.cifwk.tm.integration.ldap;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.netflix.governator.annotations.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LDAPConfiguration {

    private static final String SEPARATOR = ";";

    @Configuration("ldap.url")
    private String url;

    @Configuration("ldap.systemUsername")
    private String systemUsername;

    @Configuration("ldap.systemPassword")
    private String systemPassword;

    @Configuration("ldap.search.base")
    private String searchBase;

    @Configuration("ldap.realmName")
    private String realmName;

    @Configuration("ldap.principalTemplate")
    private String principalTemplate;

    @Configuration("ldap.roles.VIEWER")
    private String viewerGroup;

    @Configuration("ldap.roles.SUPERADMIN")
    private String superadminGroup;

    @Configuration("ldap.roles.ADMIN")
    private String adminGroup;

    private LinkedHashMap<String, String> searchBases;

    @PostConstruct
    public void validate() {
        Preconditions.checkNotNull(url);
        Preconditions.checkNotNull(systemUsername);
        Preconditions.checkNotNull(systemPassword);
        Preconditions.checkNotNull(searchBase);
        Preconditions.checkNotNull(realmName);
        Preconditions.checkNotNull(principalTemplate);
        Preconditions.checkNotNull(viewerGroup);
        Preconditions.checkNotNull(superadminGroup);
        Preconditions.checkNotNull(adminGroup);

        searchBases = getSearchBasesAndPrincipals();
    }

    public Map<String, String> getGroupRolesMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put(viewerGroup, "VIEWER");
        map.put(superadminGroup, "SUPERADMIN");
        map.put(adminGroup, "ADMIN");
        return map;
    }

    private LinkedHashMap<String, String> getSearchBasesAndPrincipals() {
        LinkedHashMap<String, String> searchBases = new LinkedHashMap();
        if (searchBase.contains(SEPARATOR) && principalTemplate.contains(SEPARATOR)) {
            String[] search = searchBase.split(SEPARATOR);
            List<String> principals = Arrays.asList(principalTemplate.split(SEPARATOR));
            Iterator<String> iterator = principals.iterator();
            for (String searchBaseItem : search) {
                if (iterator.hasNext()) {
                    searchBases.put(searchBaseItem, iterator.next());
                }
            }
        } else {
            searchBases.put(searchBase, principalTemplate);
        }
        return searchBases;
    }

    public String getUrl() {
        return url;
    }

    public String getSystemUsername() {
        return systemUsername;
    }

    public String getSystemPassword() {
        return systemPassword;
    }

    public String getRealmName() {
        return realmName;
    }

    public LinkedHashMap<String, String> getSearchBases() {
        return searchBases;
    }

}
