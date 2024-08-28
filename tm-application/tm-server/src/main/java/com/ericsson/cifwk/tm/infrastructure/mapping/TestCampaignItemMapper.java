package com.ericsson.cifwk.tm.infrastructure.mapping;
/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.util.Map;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class TestCampaignItemMapper implements
        EntityMapper<TestCampaignItem, TestCampaignItemInfo>, DtoMapper<TestCampaignItemInfo, TestCampaignItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestCampaignItemMapper.class);

    private final TestCaseVersionRepository testCaseVersionRepository;
    private final TestCaseVersionMapper testCaseVersionMapper;
    private final UserRepository userRepository;
    private final UserDirectory userDirectory;
    private final EnumReferenceMapper referenceMapper;
    private final UserMapper userMapper;

    @Inject
    public TestCampaignItemMapper(
            TestCaseVersionRepository testCaseVersionRepository,
            TestCaseVersionMapper testCaseVersionMapper,
            UserRepository userRepository,
            UserDirectory userDirectory,
            EnumReferenceMapper referenceMapper,
            UserMapper userMapper) {
        this.testCaseVersionRepository = testCaseVersionRepository;
        this.testCaseVersionMapper = testCaseVersionMapper;
        this.userRepository = userRepository;
        this.userDirectory = userDirectory;
        this.referenceMapper = referenceMapper;
        this.userMapper = userMapper;
    }

    /**
     * Only for entity mapping in test plans.
     * @see TestCampaignMapper
     */
    TestCampaignItemMapper(
            TestCaseVersionMapper testCaseVersionMapper,
            EnumReferenceMapper referenceMapper,
            UserMapper userMapper) {
        this.testCaseVersionMapper = testCaseVersionMapper;
        this.referenceMapper = referenceMapper;

        this.testCaseVersionRepository = null;
        this.userRepository = null;
        this.userDirectory = null;
        this.userMapper = userMapper;
    }

    @Override
    public TestCampaignItem mapDto(TestCampaignItemInfo dto, TestCampaignItem entity) {
        if (dto == null) {
            return null;
        }

        entity.setId(dto.getId());
        entity.setUser(dto.getUser() == null ? null : findOrCreateUser(dto.getUser().getUserId()));
        entity.setTestCaseVersion(findTestCase(dto));
        return entity;
    }

    @Override
    public TestCampaignItemInfo mapEntity(TestCampaignItem entity, TestCampaignItemInfo dto,
                                          Class<? extends DtoView<TestCampaignItemInfo>> view) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        TestCaseVersion testCaseVersion = entity.getTestCaseVersion();

        if (testCaseVersion != null) {
            TestCaseInfo testCaseInfo =
                    testCaseVersionMapper.mapEntity(testCaseVersion, new TestCaseInfo(), getTestCaseView(view));
            dto.setTestCase(testCaseInfo);

            testCaseInfo.setId(testCaseVersion.getTestCase().getId());
            testCaseInfo.setVersionId(testCaseVersion.getId());
            testCaseInfo.setTestCaseId(testCaseVersion.getTestCase().getTestCaseId());
        }

        User user = entity.getUser();

        if (user != null) {
            UserInfo userInfo = userMapper.mapEntity(user, UserInfo.class);
            dto.setUser(userInfo);
        }

        if (TestCampaignItemView.Detailed.class.equals(view)) {
            return mapDetailedEntity(entity, dto);
        } else {
            return dto;
        }
    }

    private Class<? extends DtoView<TestCaseInfo>> getTestCaseView(
            Class<? extends DtoView<TestCampaignItemInfo>> view) {
        if (TestCampaignItemView.DetailedTestCase.class.equals(view)) {
            return TestCaseView.Detailed.class;
        } else {
            return TestCaseView.Simple.class;
        }
    }

    @Override
    public TestCampaignItem mapDto(TestCampaignItemInfo dto, Class<? extends TestCampaignItem> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public TestCampaignItemInfo mapEntity(TestCampaignItem entity, Class<? extends TestCampaignItemInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public TestCampaignItemInfo mapEntity(TestCampaignItem entity, TestCampaignItemInfo dto) {
        return mapEntity(entity, dto, null);
    }

    @Override
    public TestCampaignItemInfo mapEntity(TestCampaignItem entity, Class<? extends TestCampaignItemInfo> dtoClass,
                                          Class<? extends DtoView<TestCampaignItemInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    private User findOrCreateUser(String username) {
        User user = userRepository.findByExternalId(username);
        if (user == null) {
            user = new User(username);
        }

        if (!user.hasExternalAttributes()) {
            UserInfo userInfo = userDirectory.findInLDAP(user.getExternalId(), LDAPSearchType.USER);
            if (userInfo != null) {
                user.setExternalAttributes(userInfo.getEmail(), userInfo.getName(), userInfo.getSurname());
            } else {
                LOGGER.error("Unable to find user {} in LDAP", user.getExternalId());
            }
        }
        return user;
    }

    private TestCaseVersion findTestCase(TestCampaignItemInfo dto) {
        Long testCasePk = dto.getTestCase().getId();
        String testCaseId = dto.getTestCase().getTestCaseId();
        Long testCaseVersionPk = dto.getTestCase().getVersionId();
        Map<String, Long> versions = testCaseVersionMapper.getVersion(dto.getTestCase().getVersion());
        Long majorVersion = versions.get(TestCaseVersionMapper.MAJOR);
        Long minorVersion = versions.get(TestCaseVersionMapper.MINOR);

        if (testCaseVersionPk != null) {
            return testCaseVersionRepository.find(testCaseVersionPk);
        } else if (majorVersion != null && minorVersion != null) {
            if (testCasePk != null) {
                return testCaseVersionRepository.findByTestCaseAndSequence(testCasePk, majorVersion, minorVersion);
            } else if (testCaseId != null) {
                return testCaseVersionRepository.findByTestCaseAndSequence(testCaseId, majorVersion, minorVersion);
            }
        }
        return null;
    }

    private TestCampaignItemInfo mapDetailedEntity(TestCampaignItem entity, TestCampaignItemInfo dto) {
        dto.setTestCampaign(referenceMapper.mapEntity(entity.getTestCampaign(), new ReferenceDataItem()));

        return dto;
    }

}
