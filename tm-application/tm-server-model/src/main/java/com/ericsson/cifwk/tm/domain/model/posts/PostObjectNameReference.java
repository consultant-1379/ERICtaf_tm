package com.ericsson.cifwk.tm.domain.model.posts;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

public enum PostObjectNameReference {

    TEST_CASE("TESTCASE", 1),
    TEST_PLAN("TESTPLAN", 2),
    UNKNOWN("UNKNOWN", -1);

    private final String key;
    private final long value;

    PostObjectNameReference(String title, long value) {
        this.key = title;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }


    public static PostObjectNameReference mapObjectReferenceByKey(String mappedKey) {
        for (PostObjectNameReference item : PostObjectNameReference.values()) {
            if (equalsIgnoreCase(mappedKey, item.getKey())) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public static PostObjectNameReference mapObjectReferenceByValue(long mappedValue) {
        for (PostObjectNameReference item : PostObjectNameReference.values()) {
            if (mappedValue == item.getValue()) {
                return item;
            }
        }
        return UNKNOWN;
    }

}

