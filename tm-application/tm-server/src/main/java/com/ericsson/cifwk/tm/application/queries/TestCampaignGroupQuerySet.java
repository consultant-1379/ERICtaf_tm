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
import com.ericsson.cifwk.tm.application.queries.csv.TestCaseCSVCreator;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignGroupMapper;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupItemInfo;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@QuerySet
public class TestCampaignGroupQuerySet {

    @Inject
    private TestCampaignGroupRepository testCampaignGroupRepository;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private TestCampaignGroupMapper testCampaignGroupMapper;

    public Response getTestCampaignGroupsByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> groupFields = searchMapping.getTestCampaignGroupFields();
        QueryInfo queryInfo = query.convertToQueryInfo(groupFields);
        Search search = query.convertToSearch(TestCampaignGroup.class, groupFields);
        Paginated<TestCampaignGroup> paginated = testCampaignGroupRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestCampaignGroup, TestCampaignGroupInfo>() {
                    @Override
                    public TestCampaignGroupInfo apply(TestCampaignGroup testCampaignGroup) {
                        return testCampaignGroupMapper.mapEntity(testCampaignGroup, TestCampaignGroupInfo.class);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    public Response getTestCampaignGroup(Long id) {
        TestCampaignGroupInfo testCampaignGroupInfo = getTestCampaignGroupInfo(id);
        return Responses.ok(testCampaignGroupInfo);
    }

    private TestCampaignGroupInfo getTestCampaignGroupInfo(Long id) {
        Optional<TestCampaignGroup> testCampaignGroup = Optional.ofNullable(testCampaignGroupRepository.find(id));

        if (!testCampaignGroup.isPresent()) {
            throw new NotFoundException();
        }

        return testCampaignGroupMapper.mapEntity(testCampaignGroup.get(),
                TestCampaignGroupInfo.class);
    }

    public Response getTestCampaignGroupTestCasesCSV(Long id) {
        TestCampaignGroupInfo testCampaignGroupInfo = getTestCampaignGroupInfo(id);

        StreamingOutput stream;
        TestCaseCSVCreator testCaseCSVFileCreator = new TestCaseCSVCreator();

        String fileName = "TestCampaignGroup#" + testCampaignGroupInfo.getName();
        String fileExtension = ".csv";

        List<TestCampaignGroupItemInfo> testCampaignGroupItemInfos = testCampaignGroupInfo.getTestCampaigns().stream()
                .flatMap(testCampaignInfo -> testCampaignInfo.getTestCampaignItems()
                        .stream().map(item -> {
                                if (testCampaignInfo.getDrop() == null) {
                                    testCampaignInfo.setDrop(new DropInfo(1L, ""));
                                }
                                return new TestCampaignGroupItemInfo(testCampaignInfo.getName(),
                                    testCampaignInfo.getDrop().getName(), testCampaignInfo.getEnvironment(), item);
                            })
                )
                .collect(Collectors.toList());

        stream = testCaseCSVFileCreator.createCSV(testCampaignGroupItemInfos);

        return Responses.file(stream, fileName, fileExtension);
    }
}
