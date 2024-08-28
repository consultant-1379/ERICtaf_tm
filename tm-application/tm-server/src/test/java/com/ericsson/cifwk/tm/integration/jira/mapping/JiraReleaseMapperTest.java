package com.ericsson.cifwk.tm.integration.jira.mapping;

import com.ericsson.cifwk.tm.integration.jira.client.JiraRelease;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRelease;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class JiraReleaseMapperTest {

    private JiraReleaseMapper mapper;
    private JiraRelease jiraRelease;


    @Before
    public void setUp() {
        mapper = new JiraReleaseMapper();
        jiraRelease = mock(JiraRelease.class, RETURNS_DEEP_STUBS);
    }

    @Test
    public void testMap_PopulatedProperties() {
        when(jiraRelease.getId()).thenReturn("releaseId");
        when(jiraRelease.getName()).thenReturn("nameRelease");
        when(jiraRelease.getDescription()).thenReturn("descriptionRelease");

        ExternalRelease externalRelease = mapper.map(jiraRelease);

        assertThat(externalRelease.getId(), equalTo("releaseId"));
        assertThat(externalRelease.getName(), equalTo("nameRelease"));
        assertThat(externalRelease.getDescription(), equalTo("descriptionRelease"));
    }

    @Test
    public void testMap_PopulatedMandatory() {
        when(jiraRelease.getId()).thenReturn("releaseId");
        when(jiraRelease.getName()).thenReturn("nameRelease");

        ExternalRelease externalRelease = mapper.map(jiraRelease);

        assertThat(externalRelease.getId(), equalTo("releaseId"));
        assertThat(externalRelease.getName(), equalTo("nameRelease"));
        assertThat(externalRelease.getDescription(), nullValue());
    }

}
