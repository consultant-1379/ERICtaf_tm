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
import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.ReportingService;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReportObjectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.VersionModification;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.responses.CompletionHelper;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@QuerySet
public class TestCaseQuerySet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCaseQuerySet.class);

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TestCaseVersionMapper testCaseVersionMapper;

    @Inject
    private TestCaseVersionRepository testCaseVersionRepository;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private ModificationService modificationService;

    @Inject
    private ReportObjectMapper reportObjectMapper;

    @Inject
    private ReportingService reportingService;

    public Response testCasesExist(String id) {
        Collection<String> foundIds = testCaseRepository.findTestCaseIds(Lists.newArrayList(id));
        if (foundIds.contains(id)) {
            return Responses.ok();
        } else {
            return Responses.notFound();
        }
    }

    public Response getTestCasesIds(List<String> ids) {
        Collection<String> foundIds = testCaseRepository.findTestCaseIds(ids);
        return Responses.ok(foundIds);
    }


    public Response getTestCaseVersion(String testCaseId, String versionId,
                                       Class<? extends DtoView<TestCaseInfo>> view) {
        if (Strings.isNullOrEmpty(testCaseId) || Strings.isNullOrEmpty(versionId)) {
            return Responses.badRequest("Empty id field");
        }

        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null || testCase.isDeleted()) {
            return Responses.notFound();
        }

        long id = Long.parseLong(versionId);
        List<TestCaseVersion> versions = testCase.getVersions();
        for (TestCaseVersion version : versions) {
            if (version.getId().equals(id)) {
                TestCaseInfo dto = testCaseVersionMapper.mapEntity(version, TestCaseInfo.class, view);
                return Responses.ok(dto);
            }
        }

        return Responses.notFound();
    }

    public Response getTestCase(String id, Class<? extends DtoView<TestCaseInfo>> view) {
        TestCase entity = testCaseRepository.findByAnyId(id);
        if (entity == null || entity.isDeleted()) {
            return Responses.notFound();
        }
        TestCaseInfo dto = testCaseMapper.mapEntity(entity, TestCaseInfo.class, view);

        List<TestCaseVersion> testCaseVersions = testCaseVersionRepository.findByTestCase(entity.getTestCaseId());
        List<VersionModification> modifications = testCaseVersions.stream()
                .flatMap(item -> modificationService.getVersionModifications(item).stream())
                .collect(Collectors.toList());

        dto.setModifications(modifications);
        return Responses.nullable(dto);
    }

    public Response getTestCases(Iterable<Long> ids, final Class<? extends DtoView<TestCaseInfo>> view) {
        TestCase[] testCases = testCaseRepository.find(Iterables.toArray(ids, Long.class));
        List<TestCaseInfo> dtos = FluentIterable.from(Arrays.asList(testCases))
                .filter(new Predicate<TestCase>() {
                    @Override
                    public boolean apply(TestCase input) {
                        return input != null;
                    }
                })
                .transform(new Function<TestCase, TestCaseInfo>() {
                    @Override
                    public TestCaseInfo apply(TestCase input) {
                        return testCaseMapper.mapEntity(input, TestCaseInfo.class, view);
                    }
                })
                .toList();
        return Responses.ok(dtos);
    }

    public Response getTestCasesByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo,
            final Class<? extends DtoView<TestCaseInfo>> view) {
        Paginated<TestCase> paginated = testCaseService.customSearch(query, page, perPage);

        Map<String, QueryField> testCaseFields = searchMapping.getTestCaseFields();
        QueryInfo queryInfo = query.convertToQueryInfo(testCaseFields);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestCase, TestCaseInfo>() {
                    @Override
                    public TestCaseInfo apply(TestCase testCase) {
                        return testCaseMapper.mapEntity(testCase, TestCaseInfo.class, view);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    public Response getCompletion(Long productId, List<Long> featureId, List<Long> componentIds, String search, int limit) {
        if (Strings.isNullOrEmpty(search)) {
            return Responses.ok(CompletionInfo.empty());
        }
        List<TestCase> testCases = testCaseRepository.findMatchingTestCaseId(productId, featureId, componentIds, search, limit);
        CompletionInfo completion = CompletionHelper.completion(search, testCases,
                new Function<TestCase, String>() {
                    @Override
                    public String apply(TestCase testCase) {
                        return testCase.getTestCaseId();
                    }
                }
        );
        return Responses.ok(completion);
    }

    public Response generateDocReport(Long testCampaignGroupId) {
        LOGGER.info("Report generating for TestCampaignGroupId :" + testCampaignGroupId);
        try {
            List<ReportObject> listOfReportObj;
            listOfReportObj = reportObjectMapper.getTestCampaignGroupInfo(testCampaignGroupId);
            XWPFDocument document = reportingService.docReport(listOfReportObj, false);
            StreamingOutput streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException {
                    LOGGER.info("Writing the document to OutputStream");
                    document.write(output);
                }
            };
            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = SprintValidationTestReport.docx")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Responses.badRequest("Unknown format: DOCX"));
        }
    }

    public Response generateSovStatusReport(Long testCampaignId) {
        LOGGER.info("Report generating for TestCampaignGroupId :" + testCampaignId);
        try {
            List<ReportObject> listOfReportObj;
            listOfReportObj = reportObjectMapper.getTestCampaignGroupInfo(testCampaignId);
            XWPFDocument document = reportingService.docReport(listOfReportObj, true);
            StreamingOutput streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException {
                    LOGGER.info("Writing the document to OutputStream");
                    document.write(output);
                }
            };
            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = SoVStatusReport.docx")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Responses.badRequest("Unknown format: DOCX"));
        }
    }
    public Response generateDocSubReport(Long id, String testCampaignId) {

        try {
            List<ReportObject> listOfReportObj;
            listOfReportObj = reportObjectMapper.getTestCampaignSubGroupInfo(id, testCampaignId);
            LOGGER.info("Report listOfReportObj for TestCampaignSubGroupId :" + listOfReportObj.toString());
            listOfReportObj.forEach(System.out::println);
            XWPFDocument document = reportingService.docReport(listOfReportObj, false);
            StreamingOutput streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException {
                    LOGGER.info("Writing the document to OutputStream");
                    document.write(output);
                }
            };
            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = SprintValidationTestReport.docx")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Responses.badRequest("Unknown format: DOCX"));
        }
    }
    public Response generateSovStatusSubReport(Long id, String testCampaignId) {

        try {
            List<ReportObject> listOfReportObj;
            listOfReportObj = reportObjectMapper.getTestCampaignSubGroupInfo(id, testCampaignId);
            LOGGER.info("Report listOfReportObj for TestCampaignSubGroupId :" + listOfReportObj.toString());
            listOfReportObj.forEach(System.out::println);
            XWPFDocument document = reportingService.docReport(listOfReportObj, true);
            StreamingOutput streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException {
                    LOGGER.info("Writing the document to OutputStream");
                    document.write(output);
                }
            };
            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = SoVStatusReport.docx")
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Responses.badRequest("Unknown format: DOCX"));
        }
    }
}
