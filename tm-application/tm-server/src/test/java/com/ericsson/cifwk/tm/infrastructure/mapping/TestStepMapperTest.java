package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestStep;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TestStepMapperTest {

    private TestStepMapper testStepMapper;

    @Before
    public void setUp() throws Exception {
        testStepMapper = new TestStepMapper(new VerifyStepMapper());
    }

    @Test
    public void testEntity() {
        TestStep testStep = buildTestStep()
                .withId(2L)
                .withExecute("testStep2")
                .withComment("comment2")
                .withData("data2")
                .build();
        TestStepInfo dto = testStepMapper.mapEntity(testStep, TestStepInfo.class);

        assertThat(dto.getId(), equalTo(2L));
        assertThat(dto.getName(), equalTo("testStep2"));
        assertThat(dto.getComment(), equalTo("comment2"));
        assertThat(dto.getData(), equalTo("data2"));
    }

    @Test
    public void testDto() {
        TestStepInfo dto = TestDtoFactory.getTestStep(2);
        TestStep entity = testStepMapper.mapDto(dto, TestStep.class);

        assertThat(entity.getId(), equalTo(2L));
        assertThat(entity.getTitle(), equalTo("testStep2"));
        assertThat(entity.getComment(), equalTo("comment2"));
        assertThat(entity.getData(), equalTo("data2"));
    }

}
