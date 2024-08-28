package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationRepository;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildNotification;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class NotificationRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private NotificationRepository notificationRepository;

    @Test
    public void testFindForDate() throws Exception {
        Notification entity1 = createNotification("text1", 0, 2);
        Notification entity2 = createNotification("text2", 1, 2);
        Notification entity3 = createNotification("text3", 2, 2);
        persistence().persistInTransaction(entity1, entity2, entity3);

        List<Notification> after12 = notificationRepository.findForDate(hoursFromNow(12));
        List<Notification> after36 = notificationRepository.findForDate(hoursFromNow(36));
        List<Notification> after60 = notificationRepository.findForDate(hoursFromNow(60));
        
        assertThat(after12, hasSize(1));
        assertThat(after12, contains(
                hasProperty("text", equalTo("text1"))
        ));
        assertThat(after36, hasSize(2));
        assertThat(after36, contains(
                hasProperty("text", equalTo("text2")),
                hasProperty("text", equalTo("text1"))
        ));
        assertThat(after60, hasSize(2));
        assertThat(after60, contains(
                hasProperty("text", equalTo("text3")),
                hasProperty("text", equalTo("text2"))
        ));
    }

    @Test
    public void testDeleteOldNotifications() throws Exception {
        Notification entity1 = createNotification("text1", 0, 2);
        Notification entity2 = createNotification("text2", 1, 2);
        Notification entity3 = createNotification("text3", 2, 2);
        persistence().persistInTransaction(entity1, entity2, entity3);
        List<Notification> beforeDate = notificationRepository.findBeforeDate(hoursFromNow(36));
        assertThat(beforeDate.size(), equalTo(2));
    }

    private Date hoursFromNow(int hours) {
        return DateTime.now().plusHours(hours).toDate();
    }

    private Notification createNotification(String text, int daysFromNow, int daysLength) {
        return buildNotification(daysFromNow, daysLength)
                .withText(text)
                .build();
    }
}
