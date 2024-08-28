/*
* COPYRIGHT Ericsson (c) 2014.
*
* The copyright to the computer program(s) herein is the property of
* Ericsson Inc. The programs may be used and/or copied only with written
* permission from Ericsson Inc. or in accordance with the terms and
* conditions stipulated in the agreement/contract under which the
* program(s) have been supplied.
*/
package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.annotations.Service;
import com.ericsson.cifwk.tm.application.params.TestCampaignCriteria;
import com.ericsson.cifwk.tm.application.services.TestCampaignService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidation;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.events.UserAssignedEvent;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.Diff;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignItemMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.Arrays.asList;



@Service
public class TestCampaignServiceImpl implements TestCampaignService {

    private final TestCampaignRepository testCampaignRepository;
    private final ProjectRepository projectRepository;
    private final TestCampaignMapper testCampaignMapper;
    private final TestCampaignItemMapper testCampaignItemMapper;
    private final EventBus eventBus;
    private final SearchMapping searchMapping;
    private final ProductFeatureRepository featureRepository;
    private final TechnicalComponentRepository componentRepository;
    private final TestCampaignGroupRepository testCampaignGroupRepository;
    private final DropRepository dropRepository;
    private final DropMapper dropMapper;

    @Inject
    public TestCampaignServiceImpl(
            TestCampaignRepository testCampaignRepository,
            ProjectRepository projectRepository,
            TestCampaignMapper testCampaignMapper,
            TestCampaignItemMapper testCampaignItemMapper,
            EventBus eventBus,
            SearchMapping searchMapping,
            ProductFeatureRepository featureRepository,
            TechnicalComponentRepository componentRepository,
            DropRepository dropRepository,
            DropMapper dropMapper,
            TestCampaignGroupRepository testCampaignGroupRepository) {
        this.testCampaignRepository = testCampaignRepository;
        this.projectRepository = projectRepository;
        this.testCampaignMapper = testCampaignMapper;
        this.testCampaignItemMapper = testCampaignItemMapper;
        this.eventBus = eventBus;
        this.searchMapping = searchMapping;
        this.featureRepository = featureRepository;
        this.componentRepository = componentRepository;
        this.dropRepository = dropRepository;
        this.dropMapper = dropMapper;
        this.testCampaignGroupRepository = testCampaignGroupRepository;
    }

    @Override
    public List<TestCampaign> findAll(String projectId) {
        Search search = getSearchWithProjectId(Strings.emptyToNull(projectId));
        return testCampaignRepository.search(search);
    }

    @Override
    public List<TestCampaign> findAllOpen(String product) {
        Search search = new Search();
        addProductIdFilter(search, Strings.emptyToNull(product));
        search.addFilterEqual("locked", false);
        return testCampaignRepository.search(search);
    }

    private Search getSearchWithProjectId(String projectId) {
        Search search = new Search();
        addProjectIdFilter(search, projectId);
        return search;
    }

    private void addProjectIdFilter(Search search, String projectId) {
        if (projectId != null) {
            search.addFilterEqual("project.externalId", projectId);
        }
    }

    private void addProductIdFilter(Search search, String productId) {
        if (productId != null) {
            search.addFilterEqual("feature.product.externalId", productId);
        }
    }

    @Override
    public Paginated<TestCampaign> customSearch(Query query, int page, int perPage) {
        Map<String, QueryField> testPlanFields = searchMapping.getTestPlanFields();
        Search search = query.convertToSearch(TestCampaign.class, testPlanFields);
        return testCampaignRepository.searchPaginated(search, page, perPage);
    }

    @Override
    public Paginated<TestCampaign> customSearch(TestCampaignCriteria criteria, int page, int perPage) {

        Map<String, QueryField> testPlanFields = searchMapping.getTestPlanFields();
        Query query = criteria.getQuery();
        Search search = query.convertToSearch(TestCampaign.class, testPlanFields);
        addFilters(search, criteria.getProductId(), criteria.getDropId(), criteria.getFeatureIds(), criteria.getComponentIds());
        return testCampaignRepository.searchPaginated(search, page, perPage);
    }

    @Override
    public List<TestCampaign> copyTestCampaigns(CopyTestCampaignsRequest request) throws ServiceValidationException {
        Map<String, QueryField> testPlanFields = searchMapping.getTestPlanFields();
        Query query = Query.fromQueryString(request.getQuery());
        Search search = query.convertToSearch(TestCampaign.class, testPlanFields);
        List<Long> featureIds = request.getFeatureIds();
        addFilters(search, request.getProductId(), request.getFromDropId(), featureIds, request.getComponentIds());
        List<TestCampaign> testCampaigns = new ArrayList<>(
                new HashSet<>(testCampaignRepository.search(search)));

        if (testCampaigns.isEmpty()) {
            throw new ServiceValidationException("No Test Campaigns to copy");
        }

        List<TestCampaignInfo> dtos = testCampaigns.stream()
                .map(entity -> testCampaignMapper.mapEntity(entity, TestCampaignInfo.class, TestCampaignView.Detailed.class))
                .collect(Collectors.toList());

        Drop copyToDrop = dropRepository.find(request.getCopyToDropId());
        DropInfo copyToDropDto = dropMapper.mapEntity(copyToDrop, DropInfo.class);

        TestCampaign[] copies = new TestCampaign[dtos.size()];
        for (int i = 0; i < copies.length; i++) {
            TestCampaignInfo originalDto = dtos.get(i);
            Long parentId = getParentId(originalDto);
            originalDto.setId(null);
            originalDto.setDrop(copyToDropDto);
            copies[i] = populate(new TestCampaign(), originalDto);
            copies[i].setParentId(parentId);
            TestCampaign savedCopy = testCampaignRepository.save(copies[i]);
            copyTestCampaignItems(savedCopy, originalDto);
        }
        copies = testCampaignRepository.save(copies);

        return Lists.newArrayList(copies);
    }

    private Long getParentId(TestCampaignInfo testCampaignInfo) {
        Long parentId = testCampaignInfo.getParentId();
        return parentId == null ? testCampaignInfo.getId() : parentId;
    }

    private void addFilters(Search search, Long productId, Long dropId, List<Long> featureIds, List<Long> componentIds) {
        if (productId != null) {
            search.addFilterEqual("features.product.id", productId);
        }
        if (dropId != null) {
            search.addFilterEqual("drop.id", dropId);
        }
        if (!featureIds.isEmpty()) {
            search.addFilterSome("features", Filter.in("id", featureIds));
        }
        if (!componentIds.isEmpty()) {
            search.addFilterSome("components", Filter.in("id", componentIds));
        }
    }

    protected void addToArrayMap(Map<String, List<String>> map, String key, String value) {
        if (!map.containsKey(key)) {
            map.put(key, Lists.<String>newArrayList());
        }

        map.get(key).add(value);
    }

    @Override
    public TestCampaign populate(TestCampaign entity, TestCampaignInfo input) throws ServiceValidationException {
        testCampaignMapper.mapDto(input, entity);

        // project lookup
        ProjectInfo projectInfo = input.getProject();
        if (projectInfo != null) {
            Project project = projectRepository.findByExternalId(projectInfo.getExternalId());
            entity.setProject(project);
        }

        DropInfo dropInfo = input.getDrop();
        // possible to migrate test campaign between products, some products have drops, others not
        if (dropInfo == null) {
            entity.setDrop(null);
            entity.setComponents(Sets.newHashSet());
        } else {
            Drop drop = dropRepository.find(dropInfo.getId());
            entity.setDrop(drop);
        }

        Set<FeatureInfo> featureInfos = input.getFeatures();
        List<Long> featureInfoIds = featureInfos.stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        List<ProductFeature> features = featureRepository.findByIds(featureInfoIds);
        entity.setFeatures(Sets.newHashSet(features));

        Set<TechnicalComponentInfo> technicalComponentInfos = input.getComponents();
        List<Long> ids = technicalComponentInfos.stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());
        List<TechnicalComponent> components = componentRepository.findByIds(ids);
        entity.setComponents(Sets.newHashSet(components));

        Set<ReferenceDataItem> groupIdsAsStrings = input.getGroups();

        List<Long> groupIds = groupIdsAsStrings.stream().filter(Objects::nonNull)
                .map(item -> Long.valueOf(item.getId()))
                .collect(Collectors.toList());

        TestCampaignGroup[] groups = testCampaignGroupRepository.find(groupIds.toArray(new Long[groupIds.size()]));

        if (groups != null) {
            boolean hasNullScopes = Iterables.any(Arrays.asList(groups), Predicates.isNull());
            ServiceValidation.check(!hasNullScopes, "Some groups don't exist");

            entity.clearGroups();
            entity.addGroup(asList(groups));
        }

        return entity;
    }

    private void copyTestCampaignItems(TestCampaign savedCopy, TestCampaignInfo input) {
        Set<TestCampaignItemInfo> testCampaignItemInfos = input.getTestCampaignItems();
        if (!testCampaignItemInfos.isEmpty()) {
            Set<TestCampaignItem> testCampaignItems = testCampaignItemInfos.stream()
                    .map(dto -> testCampaignItemMapper.mapDto(dto, TestCampaignItem.class))
                    .collect(Collectors.toSet());

            testCampaignItems.forEach(i -> i.setId(null));
            testCampaignItems.forEach(i -> savedCopy.addTestCampaignItem(i));
        }
    }

    @Override
    public void notifyAboutAssignment(Map<String, List<String>> usersToNotify, Long testPlanId, String hostname) {
        for (Map.Entry<String, List<String>> userAssignments : usersToNotify.entrySet()) {
            UserAssignedEvent userAssignedEvent = new UserAssignedEvent(testPlanId,
                    userAssignments.getKey(),
                    userAssignments.getValue(),
                    hostname);

            eventBus.post(userAssignedEvent);
        }
    }

    @Override
    public Map<String, List<String>> getUsersToNotify(TestCampaign entity, TestCampaignInfo input) {
        Map<String, List<String>> notifiedUsers = Maps.newHashMap();
        Map<String, List<String>> usersToNotify = Maps.newHashMap();
        for (TestCampaignItem testCampaignItem : entity.getTestCampaignItems()) {
            if (testCampaignItem.getUser() != null) {
                addToArrayMap(notifiedUsers, testCampaignItem.getUser().getExternalId(),
                        testCampaignItem.getTestCaseVersion().getTestCase().getTestCaseId());
            }
        }

        Diff<TestCampaignItem> testPlanItemDiff = Mapping.mapDiffAudited(
                entity.getTestCampaignItems(),
                input.getTestCampaignItems(),
                testCampaignItemMapper,
                TestCampaignItem.class
        );
        entity.clearTestCampaignItems();
        for (TestCampaignItem testCampaignItem : testPlanItemDiff.getAdded()) {
            if (!testCampaignItem.getTestCaseVersion().isDeleted()) {
                entity.addTestCampaignItem(testCampaignItem);
                if (testCampaignItem.getUser() != null) {
                    addNotifiedUsers(notifiedUsers, usersToNotify, testCampaignItem);
                }
            }
        }

        return usersToNotify;
    }

    private void addNotifiedUsers(Map<String, List<String>> notifiedUsers, Map<String, List<String>> usersToNotify,
                                  TestCampaignItem testCampaignItem) {
        String userExternalId = testCampaignItem.getUser().getExternalId();
        String testCaseId = testCampaignItem.getTestCaseVersion().getTestCase().getTestCaseId();
        if (!notifiedUsers.containsKey(userExternalId) ||
                !notifiedUsers.get(userExternalId).contains(testCaseId)) {
            addToArrayMap(usersToNotify, userExternalId, testCaseId);
        }
    }


}
