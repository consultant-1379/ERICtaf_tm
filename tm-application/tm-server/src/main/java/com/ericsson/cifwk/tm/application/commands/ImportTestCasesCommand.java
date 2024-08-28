/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.services.ExcelObject;
import com.ericsson.cifwk.tm.application.services.ExcelService;
import com.ericsson.cifwk.tm.application.services.TestCaseSilentService;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseImportMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.responses.BadRequest;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.xlsx4j.exceptions.Xlsx4jException;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;


public class ImportTestCasesCommand implements Command<List<FormDataBodyPart>> {

    @Inject
    private ExcelService excelService;

    @Inject
    private TestCaseImportMapper testCaseImportMapper;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private UserHelper userHelper;

    @Inject
    private TestCaseSilentService testCaseSilentService;

    private Logger log = Logger.getLogger(ImportTestCasesCommand.class);

    private List<BadRequest> validationError;

    @Override
    public Response apply(List<FormDataBodyPart> parts) {
        List<TestCaseInfo> testCaseInfos = Lists.newArrayList();
        List<TestCase> entities = Lists.newArrayList();
        validationError = Lists.newArrayList();
        getTestCaseInfos(parts, testCaseInfos);

        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            TestCase entity = testCaseRepository.findByExternalId(testCaseInfo.getTestCaseId());
            if (entity != null) {
                testCaseInfo.setId(entity.getId());
                addEntities(entities, testCaseInfo, entity);
            } else {
                TestCase testCase = TestCaseFactory.createTestCase();
                addEntities(entities, testCaseInfo, testCase);
            }
        }

        if (!validationError.isEmpty()) {
            throw new BadRequestException(Responses.badRequest(validationError));
        }

        List<TestCaseInfo> mappedTestCaseInfos = Lists.newArrayList();
        for (TestCase testCase : entities) {
            testCaseRepository.save(testCase);
            mappedTestCaseInfos.add(testCaseMapper.mapEntity(testCase, new TestCaseInfo(),
                    TestCaseView.SimpleRequirements.class));
        }

        return Responses.ok(mappedTestCaseInfos);

    }

    private void addEntities(List<TestCase> entities, TestCaseInfo testCaseInfo, TestCase testCase) {
        TestCaseVersion currentVersion = testCase.getCurrentVersion();
        testCaseMapper.mapDto(testCaseInfo, testCase);
        List<BadRequest> foundIssues = testCaseSilentService.populate(currentVersion, testCaseInfo);
        userHelper.setUpdateUser(testCase);
        validationError.addAll(foundIssues);
        entities.add(testCase);
    }

    private void getTestCaseInfos(List<FormDataBodyPart> parts, List<TestCaseInfo> testCaseInfos) {
        try {
            for (FormDataBodyPart input : parts) {
                InputStream inputStream = input.getValueAs(InputStream.class);

                ExcelObject xlsxData = excelService.getXLSXData(inputStream);

                List<TestCaseInfo> mappedTestCaseInfos = testCaseImportMapper.mapTestCases(xlsxData);

                HashSet<String> seen = new HashSet<>(); // remove duplicates
                mappedTestCaseInfos.removeIf(e -> !seen.add(e.getTestCaseId()));

                testCaseInfos.addAll(mappedTestCaseInfos);

            }
        } catch (Docx4JException | Xlsx4jException e) {
            log.error(e.getMessage());
            throw new BadRequestException(Responses.badRequest("Test cases failed to be imported"));
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
            throw new BadRequestException(Responses.badRequest("Test cases have deleted columns, " +
                    "Do not press delete in a cell. Please insert a blank space i.e. ' ' " +
                    "if you wish this cell to be blank"));
        }
    }
}
