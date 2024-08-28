/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.notifications;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import org.joda.time.DateTime;

@Operator
public class NotificationFixturesOperatorImpl extends RestLoginCommonOperator implements NotificationFixturesOperator {

    private static final String SECRET = "test";

    @Override
    @TestStep(id = "start", description = "Start notification fixtures operator on host {0}")
    public void start(Host host) {
        super.start(host);
    }

    @Override
    public Result<NotificationInfo> create(ReferenceDataItem type, String text) {
        NotificationInfo dto = new NotificationInfo();
        dto.setType(type);
        dto.setText(text);
        dto.setStartDate(DateTime.now().minusDays(1).toDate());
        dto.setEndDate(DateTime.now().plusDays(1).toDate());

        String requestUrl = "/tm-server/api/notifications";
        HttpResponse response = newRequest()
                .queryParam("secret", SECRET)
                .body(json.toJson(dto))
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        NotificationInfo result = json.fromJson(response.getBody(), NotificationInfo.class);
        return Result.success(result);
    }

}
