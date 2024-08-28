package com.ericsson.cifwk.tm.test;


import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirementType;
import com.google.common.collect.Maps;

import java.util.Map;

import static com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirementType.*;

public class TestRequirements {

    public static final String PROJECT1 = "CIP";
    public static final String EPIC1 = "CIP-753";
    public static final String STORY1 = "CIP-4167";
    public static final String STORY2 = "CIP-4168";
    public static final String SUBTASK1 = "CIP-4325";
    public static final String EPIC2 = "CIP-2906";
    public static final String NON_EXISTING = "NON-EXISTING-123";
    public static final String EPIC1_TITLE = "Test Prep AVS ";
    public static final String EPIC1_SUMMARY = "As a Developer i want a mechanism for managing my test cases (AVS)";
    public static final String STORY1_SUMMARY = "As a TAF user I want to import and validate my mindmap from a new TAF Preparation UI";
    public static final String STORY2_SUMMARY = "As a TAF user I want one more user story";
    public static final String SUBTASK1_SUMMARY = "Create taf-mindmap model transformer";
    public static final String EPIC2_SUMMARY = "As a TAF team member I want to implement the new AVS code generation process";
    public static final String PROJECT_ID = "CIP";

    private final Map<String, ExternalRequirement> issues;

    {
        issues = Maps.newHashMap();
        issues.put(EPIC1, requirement(EPIC1, EPIC, EPIC1_TITLE, EPIC1_SUMMARY, null));
        issues.put(STORY1, requirement(STORY1, STORY, STORY1, STORY1_SUMMARY, issues.get(EPIC1)));
        issues.put(STORY2, requirement(STORY2, STORY, STORY2, STORY2_SUMMARY, issues.get(EPIC1)));
        issues.put(SUBTASK1, requirement(SUBTASK1, SUBTASK, SUBTASK1, SUBTASK1_SUMMARY, issues.get(STORY1)));
        issues.put(EPIC2, requirement(EPIC2, EPIC, EPIC2, EPIC2_SUMMARY, null));
    }

    private ExternalRequirement requirement(
            String id,
            ExternalRequirementType type,
            String title,
            String summary,
            ExternalRequirement requirement) {
        return ExternalRequirement.builder(id, type)
                .title(title)
                .summary(summary)
                .parent(requirement)
                .project(PROJECT_ID)
                .build();
    }

    public ExternalRequirement get(String id) {
        return issues.get(id);
    }

}
