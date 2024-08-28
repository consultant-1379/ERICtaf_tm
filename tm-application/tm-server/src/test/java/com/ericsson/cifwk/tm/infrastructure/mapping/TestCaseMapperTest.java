/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCaseMapperTest {

    private TestCaseMapper mapper;
    private TestCaseInfo dto;

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @Mock
    TestCampaignMapper testCampaignMapper;

    @Mock
    FeatureMapper featureMapper;

    @Mock
    ProjectMapper projectMapper;

    @Mock
    ReviewGroupMapper reviewGroupMapper;

    @Mock
    UserMapper userMapper;

    @Mock
    private TestCaseVersionRepository testCaseVersionRepository;

    @Before
    public void setUp() {
        TestCaseVersionMapper testCaseVersionMapper = new TestCaseVersionMapper(
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                testCampaignRepository,
                testCampaignMapper,
                projectMapper,
                featureMapper,
                reviewGroupMapper,
                userMapper,
                testCaseVersionRepository
        );

        mapper = new TestCaseMapper(
                testCaseVersionMapper,
                new TestCaseViewFactory()
        );

        dto = new TestCaseInfo();
        dto.setPriority(new ReferenceDataItem(Priority.BLOCKER.name(), Priority.BLOCKER.toString()));
        dto.setDescription("description");
        dto.setTestCaseId("testCaseId");
        dto.setPackageName("package");
        dto.setPrecondition("precondition");

        ReferenceDataItem component = new ReferenceDataItem(null, "component");
        dto.setTechnicalComponents(Sets.newHashSet(component));
        dto.setTitle("title");
        dto.setType(new ReferenceDataItem("3", "Functional"));
        dto.setExecutionType(new ReferenceDataItem("2", "Automated"));
        dto.setTestCaseStatus(new ReferenceDataItem("1", "APPROVED"));
        dto.addGroup(new ReferenceDataItem("11", "group11"));
        dto.addGroup(new ReferenceDataItem("22", "group22"));
        dto.addContext(new ReferenceDataItem("1", "context1"));
        dto.addContext(new ReferenceDataItem("2", "context2"));
    }

    @Test
    public void testNull() {
        TestCaseInfo info = mapper.mapEntity(null, TestCaseInfo.class);

        assertThat(info, nullValue());
    }

    @Test
    public void testMapDto() {
        TestCase entity = new TestCase();
        entity.addNewMinorVersion();
        mapper.mapDto(dto, entity);

        TestCaseVersion currentVersion = entity.getCurrentVersion();

        assertEquals("title", currentVersion.getTitle());
        assertEquals("description", currentVersion.getDescription());
        assertNotNull(currentVersion.getTestCase().getTestCaseId());
        assertEquals(Priority.BLOCKER, currentVersion.getPriority());
        assertEquals("precondition", currentVersion.getPrecondition());
        assertEquals(TestExecutionType.AUTOMATED, currentVersion.getExecutionType());
        assertEquals(TestCaseStatus.APPROVED, currentVersion.getTestCaseStatus());

        // checking optional fields
        ImmutableList<TestField> testFields = FluentIterable.from(currentVersion.getOptionalFields())
                .toSortedList(new Comparator<TestField>() {
                    @Override
                    public int compare(TestField o1, TestField o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
        assertEquals(2, testFields.size());
        assertField("CONTEXT", "context1", testFields.get(0));
        assertField("CONTEXT", "context2", testFields.get(0));
        assertField("PACKAGE", "package", testFields.get(1));
    }

    private void assertField(String expectedName, String expectedSubvalue, TestField field) {
        assertEquals(expectedName, field.getName());
        MatcherAssert.assertThat(field.getValue(), containsString(expectedSubvalue));
    }

    @Test
    public void testSimpleEntity() {
        TestCaseInfo dto = TestDtoFactory.getTestCase(1);
        TestCase testCase = TestCaseFactory.createTestCase();
        TestCase result = mapper.mapDto(dto, testCase);

        assertThat(result.getTestCaseId(), equalTo("testCaseId1"));
    }

}
