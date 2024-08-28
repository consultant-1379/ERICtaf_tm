package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildNotification;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class NotificationControllerTest extends BaseControllerLevelTest {

    private static final String NOTIFICATIONS_URL = "/tm-server/api/notifications/";

    private static final GenericType<List<NotificationInfo>> NOTIFICATION_LIST =
            new GenericType<List<NotificationInfo>>() {
            };

    @Test
    public void testGetNotifications() throws Exception {
        app.persistence().persistInTransaction(
                createNotification("text1", 1, 2),
                createNotification("text2", 2, 1),
                createNotification("text3", 3, 1)
        );

        Response response = app.client()
                .path(NOTIFICATIONS_URL)
                .queryParam("timestamp", DateTime.now().plus(Period.hours(60)).getMillis())
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        List<NotificationInfo> infos = response.readEntity(NOTIFICATION_LIST);
        response.close();

        assertThat(infos, hasSize(2));
        assertThat(infos, containsInAnyOrder(
                hasProperty("text", equalTo("text1")),
                hasProperty("text", equalTo("text2"))
        ));
    }

    @Test
    public void testCreateNotification() throws Exception {
        NotificationInfo dto = TestDtoFactory.getNotification(1);
        dto.setId(null);
        Entity<NotificationInfo> body = Entity.entity(dto, MediaType.APPLICATION_JSON);

        EventInput eventInput = app.client()
                .path(NOTIFICATIONS_URL + "events")
                .request()
                .get(EventInput.class);

        Response response = app.client()
                .path(NOTIFICATIONS_URL)
                .queryParam("secret", "test")
                .request()
                .post(body);

        assertStatus(response, Response.Status.CREATED);
        assertThat(eventInput.isClosed(), equalTo(false));

        NotificationInfo info = response.readEntity(NotificationInfo.class);
        response.close();
        InboundEvent inboundEvent = readEventInput(eventInput);

        assertThat(info.getId(), notNullValue());
        assertThat(inboundEvent.getName(), equalTo("notification"));

        NotificationInfo eventInfo = inboundEvent.readData(NotificationInfo.class);

        assertThat(eventInfo.getId(), equalTo(info.getId()));
    }

    private Notification createNotification(String text, int daysFromNow, int daysLength) {
        return buildNotification(daysFromNow, daysLength)
                .withText(text)
                .build();
    }
}
