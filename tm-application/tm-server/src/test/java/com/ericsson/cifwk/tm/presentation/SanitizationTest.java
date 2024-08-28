package com.ericsson.cifwk.tm.presentation;

import com.ericsson.cifwk.tm.infrastructure.mapping.Sanitization;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SanitizationTest {

    @Test
    public void testCollectionInput() throws Exception {
        assertThat(Sanitization.normalizeCommaSeparated(asList("CIP-123")), containsInAnyOrder("CIP-123"));
        assertThat(Sanitization.normalizeCommaSeparated(asList("CIP-123", "CIP-456")), containsInAnyOrder("CIP-123", "CIP-456"));
        assertThat(Sanitization.normalizeCommaSeparated(asList("CIP-123", "CIP-456, CIP-789")), containsInAnyOrder("CIP-123", "CIP-456", "CIP-789"));
        assertThat(Sanitization.normalizeCommaSeparated(asList("CIP-123", "CIP-456", "", "    ")), containsInAnyOrder("CIP-123", "CIP-456"));
        assertThat(Sanitization.normalizeCommaSeparated(asList("    CIP-123,,,CIP-456  ")), containsInAnyOrder("CIP-123", "CIP-456"));
    }

}
