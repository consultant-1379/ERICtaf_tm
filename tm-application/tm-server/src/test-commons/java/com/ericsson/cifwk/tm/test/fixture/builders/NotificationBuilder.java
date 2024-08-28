package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationType;

import java.util.Date;

public class NotificationBuilder extends EntityBuilder<Notification> {

    public NotificationBuilder() {
        super(new Notification());
    }

    public NotificationBuilder withType(NotificationType notificationType) {
        entity.setType(notificationType);
        return this;
    }

    public NotificationBuilder withText(String text) {
        entity.setText(text);
        return this;
    }

    public NotificationBuilder withStartDate(Date startDate) {
        entity.setStartDate(startDate);
        return this;
    }

    public NotificationBuilder withEndDate(Date endDate) {
        entity.setEndDate(endDate);
        return this;
    }
}
