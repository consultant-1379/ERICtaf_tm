package com.ericsson.cifwk.tm.application.queries.csv.tools;

import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Created by egergle on 16/12/2015.
 */
public class CsvHelper {

    private static final String TIME_SPLITTER = ":";

    private CsvHelper() {
    }

    public static String getReferenceTitle(ReferenceDataItem referenceDataItem) {
        if (referenceDataItem != null) {
            return nullToEmpty(referenceDataItem.getTitle());
        } else {
            return "";
        }
    }

    public static String removeNewLineChar(String value) {
        if (value == null) {
            return null;
        } else {
            return value.replaceAll("\\n", " ");
        }
    }

    public static String getUser(UserInfo value) {
        if (value == null) {
            return null;
        } else {
            return value.getUserName();
        }
    }

    public static String getUserId(UserInfo value) {
        if (value == null) {
            return null;
        } else {
            return value.getUserId();
        }
    }

    public static String getFormattedTime(String value) {
        if (value == null) {
            return null;
        } else {
            return formatTime(value);
        }
    }

    public static String formatTime(String value) {
        String[] time = value.split(TIME_SPLITTER);
        if (time.length == 3) {
            String hours = time[0] + "h";
            String min = time[1] + "m";
            String seconds = time[2] + "s";
            return hours + " " + min + " " + seconds;
        } else if(time.length == 2) {
            String min = time[0] + "m";
            String seconds = time[1] + "s";
            return min + " " + seconds;
        } else {
            return time[0] + "s" ;
        }
    }
}
