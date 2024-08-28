package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.DropUpdateService;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.integration.ciportal.DropRetrievalService;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.utils.DiffHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DropUpdateServiceImpl implements DropUpdateService {

    @Inject
    private DropRetrievalService dropRetrievalService;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Override
    public void updateDrops() {
        List<Product> products = productRepository.findAll();
        products.forEach(p -> updateDropsByProduct(p));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public TestCampaign save(TestCampaign testCampaign) {
        return testCampaignRepository.save(testCampaign);
    }

    private void updateDropsByProduct(Product product) {
        if (!product.isDropCabable()) {
            return;
        }
        dropRepository.disableFilter();
        List<Drop> savedDrops = dropRepository.findByProduct(product.getId());
        List<DropInfo> savedDropDtos = entitiesToDto(savedDrops);

        List<DropInfo> remoteDropDtos = dropRetrievalService.getDrops(product.getName());
        DiffHelper<DropInfo> diffHelper = new DiffHelper<>(Sets.newHashSet(savedDropDtos), Sets.newHashSet(remoteDropDtos));

        Set<DropInfo> newDropDtos = diffHelper.getOnlyInB();

        if (!newDropDtos.isEmpty()) {
            Drop[] newDropArray = dtosToEntity(newDropDtos);
            newDropArray = dropRepository.save(newDropArray);
            List<Drop> newDropList = Lists.newArrayList(newDropArray);
            product.addDrops(newDropList);
            save(product);

            Optional<Drop> optionalLastDrop = getLastDrop(savedDrops);

            if (optionalLastDrop.isPresent() && !optionalLastDrop.get().isDefaultDrop()) {
                Drop lastDrop = optionalLastDrop.get();
                List<TestCampaign> lastDropTestCampaigns = testCampaignRepository.findByDrop(lastDrop.getId());

                newDropList.forEach(newDrop ->
                        lastDropTestCampaigns.stream()
                                .filter(item -> item.isAutoCreate())
                                .forEach(original -> {
                                    TestCampaign copy = copyTestCampaign(original, newDrop);
                                    copy = save(copy);
                                    copyTestCampaignItems(original, copy);
                                    save(copy);
                                }));
            }
        }
        dropRepository.enableFilter();
    }

    private TestCampaign copyTestCampaign(TestCampaign original, Drop newDrop) {
        TestCampaign copy = new TestCampaign();
        copy.setName(original.getName());
        copy.setDescription(original.getDescription());
        copy.setEnvironment(original.getEnvironment());
        copy.setStartDate(original.getStartDate());
        copy.setEndDate(original.getEndDate());
        copy.setSystemVersion(original.getSystemVersion());
        copy.setProject(original.getProject());
        copy.setDrop(newDrop);
        copy.setAutoCreate(original.isAutoCreate());
        copy.setFeatures(Sets.newHashSet(original.getFeatures()));
        copy.setComponents(Sets.newHashSet(original.getComponents()));

        return copy;
    }

    private void copyTestCampaignItems(TestCampaign original, TestCampaign copy) {
        Set<TestCampaignItem> testCampaignItems = original.getTestCampaignItems().stream().map(i -> {
            TestCampaignItem copiedItem = new TestCampaignItem();
            copiedItem.setTestCaseVersion(i.getTestCaseVersion());
            copiedItem.setUser(i.getUser());
            copiedItem.setDeleted(i.isDeleted());

            return copiedItem;
        }).collect(Collectors.toSet());

        testCampaignItems.forEach(i -> copy.addTestCampaignItem(i));
    }

    private Optional<Drop> getLastDrop(List<Drop> savedDrops) {
        if (!savedDrops.isEmpty()) {
            Collections.sort(savedDrops, (Drop d1, Drop d2) -> d2.getArtifactVersion().compareTo(d1.getArtifactVersion()));
            return Optional.of(savedDrops.get(0));
        }
        return Optional.empty();
    }

    private List<DropInfo> entitiesToDto(List<Drop> entities) {
        return entities.stream()
                .map(entity -> new DropInfo(entity.getProduct().getName(), entity.getName()))
                .collect(Collectors.toList());
    }

    private Drop[] dtosToEntity(Set<DropInfo> dtos) {
        List<Drop> entities = dtos.stream().map(dto -> {
            Product product = productRepository.findByName(dto.getProductName());
            return new Drop(product, dto.getName());
        })
                .collect(Collectors.toList());

        return entities.toArray(new Drop[entities.size()]);
    }

}
