package com.ericsson.cifwk.tm.application.services;

/**
 * Created by egergle on 30/03/2016.
 */
public class ReportingConstants {

    public static final String TEST_STEP = "$$$TestStep$$$";
    public static final String VERIFY_STEP = "$$$VerifyStep$$$";
    public static final String TEST_DATA = "$$$TestData$$$";
    public static final String END_OF_DATA = "$$$";
    public static final String DELIMITER = ",";
    public static final String TEST_STEP_SPLITTER = "*%]";

    public static final String TEST_CASE_ID = "TestCaseId";
    public static final String FEATURE = "Feature";
    public static final String REQUIREMENTS_IDS = "Requirement ID(s)";
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final String PRECONDITION = "Pre condition";
    public static final String COMPONENT = "Component";
    public static final String TYPE = "Type";
    public static final String PRIORITY = "Priority";
    public static final String GROUPS = "Groups";
    public static final String CONTEXT = "Context";
    public static final String EXECUTION_TYPE = "ExecutionType";
    public static final String STATUS = "Status";
    public static final String TEST_STEPS = "TestSteps";

    public static final String TEST_STEP_REGEX = "(?is)\\$\\$\\$TestStep\\$\\$\\$(.*?)\\$\\$\\$";
    public static final String VERIFY_STEP_REGEX = "(?is)\\$\\$\\$VerifyStep\\$\\$\\$(.*?)\\$\\$\\$\\s+";
    public static final String TEST_DATA_REGEX = "(?is)\\$\\$\\$TestData\\$\\$\\$(.*?)\\$\\$\\$";

    private ReportingConstants() {

    }

}
