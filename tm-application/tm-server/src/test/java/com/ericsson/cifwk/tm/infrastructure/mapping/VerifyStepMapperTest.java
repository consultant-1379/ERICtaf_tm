package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class VerifyStepMapperTest {

    private VerifyStepMapper verifyStepMapper;

    @Before
    public void setUp() throws Exception {
        verifyStepMapper = new VerifyStepMapper();
    }

    @Test
    public void testEntity() {
        VerifyStep verifyStep = TestEntityFactory.buildVerifyStep()
                .withId(2L)
                .withVerifyStep("verifyStep2")
                .build();
        VerifyStepInfo dto = verifyStepMapper.mapEntity(verifyStep, VerifyStepInfo.class);

        assertThat(dto.getId(), equalTo(2L));
        assertThat(dto.getName(), equalTo("verifyStep2"));
    }

    @Test
    public void testDto() {
        VerifyStepInfo dto = TestDtoFactory.getVerifyStep(2);
        VerifyStep entity = verifyStepMapper.mapDto(dto, VerifyStep.class);

        assertThat(entity.getId(), equalTo(2L));
        assertThat(entity.getVerifyStep(), equalTo("verifyStep2"));
    }

}
