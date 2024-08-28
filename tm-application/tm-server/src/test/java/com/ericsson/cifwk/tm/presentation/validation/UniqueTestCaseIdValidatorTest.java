/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniqueTestCaseIdValidatorTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @Mock
    private Provider<TestCaseRepository> testCaseRepositoryProvider;

    @Mock
    private TestCaseInfo json;

    @Mock
    private TestCase entity;

    @InjectMocks
    private UniqueTestCaseIdValidator validator = new UniqueTestCaseIdValidator();

    @Before
    public void setUp() {
        when(testCaseRepositoryProvider.get()).thenReturn(testCaseRepository);
        when(testCaseRepository.findByExternalId("CIP-4381_Func_1")).thenReturn(entity);
        when(entity.getId()).thenReturn(1L);
    }

    @Test
    public void isValid() {

        assertTrue(validator.isValid(json, null));

        when(json.getTestCaseId()).thenReturn("");
        assertTrue(validator.isValid(json, null));

        when(json.getTestCaseId()).thenReturn("CIP-4381_Func_NEW");
        assertTrue(validator.isValid(json, null));

        when(json.getTestCaseId()).thenReturn("CIP-4381_Func_1");
        when(json.getId()).thenReturn(1L);
        assertTrue(validator.isValid(json, null));

        when(json.getId()).thenReturn(2L);
        assertFalse(validator.isValid(json, null));
    }

}
