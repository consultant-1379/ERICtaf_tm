/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.ModificationService;
import com.ericsson.cifwk.tm.application.services.validation.TestCaseValidation;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.VersionModification;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@QuerySet
public class TestCaseVersionQuerySet {

    @Inject
    private TestCaseVersionRepository testCaseVersionRepository;

    @Inject
    private TestCaseVersionMapper testCaseVersionMapper;

    @Inject
    private ModificationService modificationService;

    @Inject
    private TestCaseValidation testCaseValidation;

    public Response getTestCaseVersion(
            String testCaseId,
            String version,
            boolean fixVersionId,
            Class<? extends DtoView<TestCaseInfo>> view) {

        if (!testCaseValidation.validateVersionFormat(version)) {
            return Responses.badRequest("The version should be of the format (0.0)");
        }
        Map<String, Long> versionMap = testCaseVersionMapper.getVersion(version);
        TestCaseVersion entity = testCaseVersionRepository.findByTestCaseAndSequence(testCaseId,
                versionMap.get(TestCaseVersionMapper.MAJOR),
                versionMap.get(TestCaseVersionMapper.MINOR));
        if (entity == null || entity.isDeleted()) {
            return Responses.notFound();
        }

        TestCaseInfo dto = testCaseVersionMapper.mapEntity(entity, TestCaseInfo.class, view);
        fixDtoFromTestCase(entity, dto, fixVersionId);

        List<TestCaseVersion> testCaseVersions = testCaseVersionRepository.findByTestCase(testCaseId);

        List<VersionModification> modifications = testCaseVersions.stream()
                .flatMap(item -> modificationService.getVersionModifications(item).stream())
                .collect(Collectors.toList());
        dto.setModifications(modifications);

        Set<ReferenceDataItem> mappedTestCases = testCaseVersions.stream()
                .map(item -> new ReferenceDataItem(item.getId().toString(), item.getVersion()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setTestCaseVersions(mappedTestCases);

        return Responses.nullable(dto);
    }

    public Response getVersions(
            String testCaseId,
            Class<? extends DtoView<TestCaseInfo>> view) {
        List<TestCaseVersion> testCaseVersions = testCaseVersionRepository.findByTestCase(testCaseId);
        List<TestCaseInfo> dtos = Lists.newArrayList();
        for (TestCaseVersion version : testCaseVersions) {
            TestCaseInfo dto = testCaseVersionMapper.mapEntity(version, TestCaseInfo.class, view);
            fixDtoFromTestCase(version, dto, false);
            dtos.add(dto);
        }
        return Responses.ok(dtos);
    }

    private void fixDtoFromTestCase(TestCaseVersion version, TestCaseInfo dto, boolean fixVersionId) {
        TestCase testCase = version.getTestCase();
        if (fixVersionId) {
            dto.setId(testCase.getId());
        }
        dto.setTestCaseId(testCase.getTestCaseId());
    }

}
