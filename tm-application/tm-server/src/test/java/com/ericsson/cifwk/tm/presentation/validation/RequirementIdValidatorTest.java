package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequirementIdValidatorTest {

    @Mock
    private RequirementService requirementService;

    @Mock
    private Provider<RequirementService> requirementServiceProvider;

    @InjectMocks
    private RequirementIdValidator validator = new RequirementIdValidator();

    @Before
    public void setUp() {
        when(requirementServiceProvider.get()).thenReturn(requirementService);
        when(requirementService.findOrImport("CIP-4381")).thenReturn(mock(Requirement.class));
        validator.initialize(null);
    }

    @Test
    public void isValid() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("CIP-4381", null));
        assertFalse(validator.isValid("NON-EXISTING-123", null));
    }

}
