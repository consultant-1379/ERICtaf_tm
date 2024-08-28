package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper;

import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.NotificationType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.ReferenceSerializer;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestExecutionResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestStepResultType;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonHelper {

    private JsonHelper() {
    }

    private static Gson json = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .registerTypeAdapter(NotificationType.class, new ReferenceSerializer())
            .registerTypeAdapter(TestExecutionResultType.class, new ReferenceSerializer())
            .registerTypeAdapter(TestStepResultType.class, new ReferenceSerializer())
            .create();

    public static TestCaseInfo toTestCaseInfo(InputStream testCaseJson) {
        Reader ioReader = new InputStreamReader(testCaseJson);
        return json.fromJson(ioReader, TestCaseInfo.class);
    }

    public static String fromTestCaseInfo(TestCaseInfo testCaseInfo) {
        return json.toJson(testCaseInfo);
    }

    public static TestExecutionInfo toTestExecutionInfo(InputStream testExecutionJson) {
        Reader ioReader = new InputStreamReader(testExecutionJson);
        return json.fromJson(ioReader, TestExecutionInfo.class);
    }

    public static String fromTestExecutionInfo(TestExecutionInfo testExecutionInfo) {
        return json.toJson(testExecutionInfo);
    }

    public static TestCampaignInfo toTestPlanInfo(InputStream testPlanJson) {
        Reader ioReader = new InputStreamReader(testPlanJson);
        return json.fromJson(ioReader, TestCampaignInfo.class);
    }

}
