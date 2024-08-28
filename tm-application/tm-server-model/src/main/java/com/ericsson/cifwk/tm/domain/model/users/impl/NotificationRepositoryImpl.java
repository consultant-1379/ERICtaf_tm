/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.users.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NotificationRepositoryImpl
        extends BaseRepositoryImpl<Notification, Long> implements NotificationRepository {

    @Override
    public List<Notification> findForTimestamp(long timestamp) {
        return findForDate(new Date(timestamp));
    }

    @Override
    public List<Notification> findForDate(Date date) {
        Search search = new Search(Notification.class);
        search.addFilterLessOrEqual("startDate", date);
        search.addFilterGreaterOrEqual("endDate", date);
        search.addSortDesc("startDate");
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Notification> findBeforeDate(Date date) {
        Search search = new Search(Notification.class);
        search.addFilterLessThan("startDate", date);
        search.addSortDesc("startDate");
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
