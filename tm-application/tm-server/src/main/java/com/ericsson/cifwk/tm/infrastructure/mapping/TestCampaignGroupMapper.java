/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestCampaignGroupMapper implements DtoMapper<TestCampaignGroupInfo, TestCampaignGroup>,
        EntityMapper<TestCampaignGroup, TestCampaignGroupInfo> {

    @Inject
    private ProductMapper productMapper;

    @Inject
    private TestCampaignMapper testCampaignMapper;

    @Override
    public TestCampaignGroup mapDto(TestCampaignGroupInfo dto, Class<? extends TestCampaignGroup> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestCampaignGroup mapDto(TestCampaignGroupInfo dto, TestCampaignGroup entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setProduct(productMapper.mapDto(dto.getProduct(), Product.class));
        return entity;
    }

    @Override
    public TestCampaignGroupInfo mapEntity(TestCampaignGroup entity, Class<? extends TestCampaignGroupInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestCampaignGroupInfo mapEntity(TestCampaignGroup entity, TestCampaignGroupInfo dto) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        ProductInfo productInfo = productMapper.mapEntity(entity.getProduct(), new ProductInfo());
        dto.setProduct(productInfo);
        if (entity.getTestCampaigns() != null) {
            Set<TestCampaignInfo> testCampaignInfos = entity.getTestCampaigns().stream()
                    .map(item -> testCampaignMapper.mapEntity(item, TestCampaignInfo.class, TestCampaignView.Detailed.class))
                    .collect(Collectors.toSet());

            dto.setTestCampaigns(testCampaignInfos);
        }
        return dto;
    }

    @Override
    public TestCampaignGroupInfo mapEntity(TestCampaignGroup entity, Class<? extends TestCampaignGroupInfo> dtoClass,
                                           Class<? extends DtoView<TestCampaignGroupInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestCampaignGroupInfo mapEntity(TestCampaignGroup entity, TestCampaignGroupInfo dto,
                                           Class<? extends DtoView<TestCampaignGroupInfo>> view) {
        return mapEntity(entity, dto);
    }
}
