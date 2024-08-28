/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.notifcations;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.NotificationType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class NotificationsTest extends BaseFuncTest {

    @Test
    @TestId(id = "DURACI-3200_Func_1", title = "Show and hide notification")
    public void showNotification() {
        createUiOperators();

        CustomAsserts.checkTestStep(notificationFixturesOperator.login());

        ReferenceDataItem notificationType = NotificationType.WARNING.getReferenceDataItem();
        final String cakeText = "The cake is a lie!";
        Result<NotificationInfo> created = notificationFixturesOperator.create(notificationType, cakeText);
        CustomAsserts.checkTestStep(created);
        assertEquals(cakeText, created.getValue().getText());

        CustomAsserts.checkTestStep(loginOperator.login());

        Result<List<NotificationInfo>> notifications = tmUiOperator.getNotifications();
        CustomAsserts.checkTestStep(notifications);

        Optional<NotificationInfo> createdInList =
                Iterables.tryFind(notifications.getValue(), new Predicate<NotificationInfo>() {
                    @Override
                    public boolean apply(NotificationInfo input) {
                        return cakeText.equals(input.getText());
                    }
                });
        assertTrue(createdInList.isPresent());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
